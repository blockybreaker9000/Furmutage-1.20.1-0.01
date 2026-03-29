package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.ai.ExitWaterGoal;
import net.jerika.furmutage.ai.AttributeSpeedMeleeAttackGoal;
import net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.jerika.furmutage.sound.ModSounds;

/**
 * A giant pure white latex entity - 6 blocks tall
 * Uses Changed mod's animation system
 */
public class GiantPureWhiteLatexEntity extends ChangedEntity {

    public GiantPureWhiteLatexEntity(EntityType<? extends ChangedEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        // Allow stepping over 3 blocks high
        this.setMaxUpStep(3.0f);
    }

    @Override
    public void tick() {
        super.tick();

        // Destroy leaves within 1 block of the entity's hitbox
        if (!this.level().isClientSide && this.tickCount % 5 == 0) { // Check every 5 ticks for performance
            destroyNearbyLeaves();
        }

        if (!this.level().isClientSide) {
            // Crawl when chasing a target – use SWIMMING pose to get to the player
            updateCrawlToPlayer();
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
     * Keep the giant crawling (SWIMMING pose) at all times.
     */
    private void updateCrawlToPlayer() {
        this.setPose(Pose.SWIMMING);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ExitWaterGoal(this, true));
        this.goalSelector.addGoal(2, new AttributeSpeedMeleeAttackGoal(this, false));
        this.goalSelector.addGoal(3, new ChangedEntityImprovedPathfindingGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.3D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        
        // Hostile targeting
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue(50.0D);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(16.0D);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ChangedEntity.createLatexAttributes();
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

    @Override
    protected float getSoundVolume() {
        // Make the giant roar more subtle
        return super.getSoundVolume() * 0.5f;
    }

    @Override
    public int getAmbientSoundInterval() {
        // Play ambient roar less frequently
        return super.getAmbientSoundInterval() / 2;
    }

    /**
     * Natural spawning is disabled; spawn egg and {@code /summon} still work.
     */
    public static boolean checkGiantPureWhiteLatexSpawnRules(EntityType<GiantPureWhiteLatexEntity> entityType,
                                                             ServerLevelAccessor world,
                                                             MobSpawnType reason,
                                                             BlockPos pos,
                                                             RandomSource random) {
        if (reason != MobSpawnType.NATURAL) {
            return true;
        }
        return false;
    }

}

