package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.ai.ExitWaterGoal;
import net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.jerika.furmutage.sound.ModSounds;

/**
 * A giant pure white latex entity - 6 blocks tall
 * Uses Changed mod's animation system
 */
public class GiantPureWhiteLatexEntity extends ChangedEntity {

    // Simple \"stuck\" tracker so we can decide when to crawl
    private double lastPosX, lastPosY, lastPosZ;
    private int stuckTicks;

    public GiantPureWhiteLatexEntity(EntityType<? extends ChangedEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        // Allow stepping over 3 blocks high
        this.setMaxUpStep(3.0f);
    }

    @Override
    public void tick() {
        super.tick();

        // Apply permanent Slowness 2 effect without particles
        if (!this.level().isClientSide) {
            // Check if slowness effect exists, if not add it
            if (!this.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                // Duration: very long (999999 ticks), Amplifier: 1 (slowness 2), no particles/icon
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 999999, 3, false, false, false));
            } else {
                // Refresh the effect to keep it permanent
                MobEffectInstance existing = this.getEffect(MobEffects.MOVEMENT_SLOWDOWN);
                if (existing != null && existing.getDuration() < 999990) {
                    this.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                    this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 999999, 3, false, false, false));
                }
            }
        }

        // Destroy leaves within 1 block of the entity's hitbox
        if (!this.level().isClientSide && this.tickCount % 5 == 0) { // Check every 5 ticks for performance
            destroyNearbyLeaves();
        }

        if (!this.level().isClientSide) {
            // Try to crouch in tight 2-block-high spaces when near a target player
            updateCrouchForTightSpaces();
            // If movement is blocked in very tight spaces, try crawling (1-block-high gaps)
            updateCrawlWhenStuck();
        }
    }

    private void destroyNearbyLeaves() {
        // Get the entity's bounding box and inflate it by 1 block
        AABB boundingBox = this.getBoundingBox().inflate(1.0);
        
        // Get the min and max block positions
        BlockPos minPos = BlockPos.containing(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        BlockPos maxPos = BlockPos.containing(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        
        // Iterate through all blocks in the area
        for (int x = minPos.getX(); x <= maxPos.getX(); x++) {
            for (int y = minPos.getY(); y <= maxPos.getY(); y++) {
                for (int z = minPos.getZ(); z <= maxPos.getZ(); z++) {
                    BlockPos checkPos = new BlockPos(x, y, z);
                    BlockState blockState = this.level().getBlockState(checkPos);
                    
                    // Check if the block is a leaf
                    if (blockState.is(BlockTags.LEAVES)) {
                        // Destroy the leaf block (drops items, plays sounds)
                        this.level().destroyBlock(checkPos, true);
                    }
                }
            }
        }
    }

    /**
     * If the giant is close to its target but not moving for a short time and only has
     * about 1 block of headroom, switch to SWIMMING pose to simulate crawling through
     * 1-block-high tunnels.
     */
    private void updateCrawlWhenStuck() {
        LivingEntity target = this.getTarget();
        if (target == null) {
            // No target – stand up if we were crawling
            if (this.getPose() == Pose.SWIMMING) {
                this.setPose(Pose.STANDING);
            }
            stuckTicks = 0;
            return;
        }

        // Only care when fairly close to the target
        double maxDistanceSq = 25.0D; // 5 blocks
        if (this.distanceToSqr(target) > maxDistanceSq) {
            if (this.getPose() == Pose.SWIMMING) {
                this.setPose(Pose.STANDING);
            }
            stuckTicks = 0;
            lastPosX = this.getX();
            lastPosY = this.getY();
            lastPosZ = this.getZ();
            return;
        }

        // Check horizontal movement since last tick
        double dx = this.getX() - lastPosX;
        double dz = this.getZ() - lastPosZ;
        double horizSq = dx * dx + dz * dz;

        // Update last position for next tick
        lastPosX = this.getX();
        lastPosY = this.getY();
        lastPosZ = this.getZ();

        // If barely moving while pathfinding toward a nearby target, count as \"stuck\"
        if (horizSq < 0.0005D && this.getNavigation().isInProgress()) {
            stuckTicks++;
        } else {
            stuckTicks = 0;
            // If we were crawling due to being stuck and we're moving again, stand up or crouch as other logic dictates
            if (this.getPose() == Pose.SWIMMING) {
                this.setPose(Pose.STANDING);
            }
            return;
        }

        // Need to be stuck for a short time before we decide to crawl
        if (stuckTicks < 20) { // about 1 second
            return;
        }

        // Measure very tight headroom (about 1 block) above the giant's feet
        BlockPos basePos = this.blockPosition();
        int airAbove = 0;
        int maxCheck = 4; // just a few blocks above

        for (int dy = 1; dy <= maxCheck; dy++) {
            BlockPos checkPos = basePos.above(dy);
            BlockState state = this.level().getBlockState(checkPos);
            if (state.isAir() || state.getCollisionShape(this.level(), checkPos).isEmpty()) {
                airAbove++;
            } else {
                break;
            }
        }

        boolean veryTightCeiling = airAbove <= 1;

        if (veryTightCeiling) {
            // Use SWIMMING pose for a crawling-sized hitbox
            if (this.getPose() != Pose.SWIMMING) {
                this.setPose(Pose.SWIMMING);
            }
        } else if (this.getPose() == Pose.SWIMMING) {
            // Plenty of headroom again – stand up, crouch logic will re-apply if needed
            this.setPose(Pose.STANDING);
        }
    }

    /**
     * When this giant is close to its target but under a low ceiling (about 2 blocks of headroom),
     * switch to CROUCHING pose so it can fit through 2x2 tunnels.
     */
    private void updateCrouchForTightSpaces() {
        LivingEntity target = this.getTarget();
        if (target == null) {
            // No target – stand up if we were crouching specifically for tight spaces
            if (this.getPose() == Pose.CROUCHING) {
                this.setPose(Pose.STANDING);
            }
            return;
        }

        // Only bother when reasonably close to the target
        double maxDistanceSq = 16.0D; // 4 blocks
        if (this.distanceToSqr(target) > maxDistanceSq) {
            if (this.getPose() == Pose.CROUCHING) {
                this.setPose(Pose.STANDING);
            }
            return;
        }

        // Measure vertical headroom above the giant's feet
        BlockPos basePos = this.blockPosition();
        int airAbove = 0;
        int maxCheck = 6; // up to 6 blocks above feet

        for (int dy = 1; dy <= maxCheck; dy++) {
            BlockPos checkPos = basePos.above(dy);
            BlockState state = this.level().getBlockState(checkPos);
            if (state.isAir() || state.getCollisionShape(this.level(), checkPos).isEmpty()) {
                airAbove++;
            } else {
                break;
            }
        }

        // If there's only about 2 blocks of clear headroom, crouch
        boolean tightCeiling = airAbove <= 2;

        if (tightCeiling) {
            if (this.getPose() != Pose.CROUCHING) {
                this.setPose(Pose.CROUCHING);
            }
        } else if (this.getPose() == Pose.CROUCHING) {
            // Plenty of space again – stand back up
            this.setPose(Pose.STANDING);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ExitWaterGoal(this, 0.8D)); // High priority goal to exit water
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new ChangedEntityImprovedPathfindingGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        
        // Hostile targeting
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ChangedEntity.createLatexAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0005D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 80.0D);
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.REPLICATION;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // Idle roar when breathing / idling
        return ModSounds.LATEX_BEAST_ROAR.get();
    }

    /**
     * Natural spawn rules:
     * - Underground only (Y from 10 down to -64)
     * - Not in worldgen regions (avoid structure gen)
     * - Dark enough (similar to monsters)
     * - At least 10 blocks of clear vertical space above the spawn position so the giant can fit.
     */
    public static boolean checkGiantPureWhiteLatexSpawnRules(EntityType<GiantPureWhiteLatexEntity> entityType,
                                                             ServerLevelAccessor world,
                                                             MobSpawnType reason,
                                                             BlockPos pos,
                                                             RandomSource random) {
        if (reason != MobSpawnType.NATURAL) {
            return true; // allow eggs/commands
        }

        int y = pos.getY();
        if (y > 10 || y < -64) {
            return false; // underground band only
        }

        // Must not be exposed to sky and must be dark
        if (world.getBrightness(LightLayer.SKY, pos) > random.nextInt(32)) {
            return false;
        }
        if (world.getBrightness(LightLayer.BLOCK, pos) > 0) {
            return false;
        }

        // Require at least 10 blocks of clear vertical space above
        int clear = 0;
        for (int dy = 1; dy <= 12; dy++) {
            BlockPos check = pos.above(dy);
            BlockState state = world.getBlockState(check);
            if (state.isAir() || state.getCollisionShape(world, check).isEmpty()) {
                clear++;
            } else {
                break;
            }
        }
        if (clear < 10) {
            return false;
        }

        // Use standard monster rules for final checks (valid spawn floor, etc.)
        return net.minecraft.world.entity.monster.Monster.checkMonsterSpawnRules(entityType, world, reason, pos, random);
    }

}

