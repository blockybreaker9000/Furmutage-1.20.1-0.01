package net.jerika.furmutage.entity.custom;

import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.AttributePresets;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;

public class LooseSquidDogLimbEntity extends ChangedEntity implements GenderedEntity {

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState ceilinghangAnimationState = new AnimationState();

    private static final int TICKS_WITHOUT_TARGET_TO_TELEPORT = 600; // 30 seconds
    private static final int DISMOUNT_PLAYER_RANGE = 5;
    private static final int CEILING_SCAN_HEIGHT = 16;

    public LooseSquidDogLimbEntity(EntityType<? extends LooseSquidDogLimbEntity> type, Level level) {
        super(type, level);
        this.setMaxUpStep(1.0F);
    }

    /** Prevents suffocation - entity can hang from ceilings without taking damage. */
    @Override
    public boolean isInWall() {
        return false;
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
        return false; // Tentacle ignores fall damage
    }

    /** Returns true when there is a solid block directly above the entity (ceiling to hang from). */
    public boolean isHanging() {
        BlockPos above = this.blockPosition().above();
        BlockState state = this.level().getBlockState(above);
        return !state.isAir() && state.isSolidRender(this.level(), above);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            setupAnimationStates();
        } else {
            LivingEntity target = getTarget();
            if (target != null) {
                lastTargetTime = tickCount;
            }
            int ticksWithoutTarget = tickCount - lastTargetTime;

            // Dismount: if hanging and a player is within 5 blocks below, drop down
            if (isHanging()) {
                Player playerBelow = findPlayerBelow(DISMOUNT_PLAYER_RANGE);
                if (playerBelow != null) {
                    dismountFromCeiling();
                } else {
                    this.setNoGravity(true);
                    this.setDeltaMovement(Vec3.ZERO);
                    this.getNavigation().stop();
                }
            } else {
                this.setNoGravity(false);
                // Teleport onto roof: no target for 30 sec and blocks above → move to ceiling
                if (ticksWithoutTarget >= TICKS_WITHOUT_TARGET_TO_TELEPORT && hasBlocksAbove()) {
                    BlockPos ceiling = findCeilingAbove();
                    if (ceiling != null) {
                        teleportToCeiling(ceiling);
                    }
                }
            }
        }
    }

    private int lastTargetTime = 0;

    private boolean hasBlocksAbove() {
        BlockPos pos = blockPosition();
        for (int i = 1; i <= CEILING_SCAN_HEIGHT; i++) {
            BlockState state = level().getBlockState(pos.above(i));
            if (!state.isAir() && state.isSolidRender(level(), pos.above(i))) {
                return true;
            }
        }
        return false;
    }

    private BlockPos findCeilingAbove() {
        BlockPos pos = blockPosition();
        for (int i = 1; i <= CEILING_SCAN_HEIGHT; i++) {
            BlockPos check = pos.above(i);
            BlockState state = level().getBlockState(check);
            if (!state.isAir() && state.isSolidRender(level(), check)) {
                return check;
            }
        }
        return null;
    }

    private void teleportToCeiling(BlockPos ceilingBlock) {
        BlockPos hangPos = ceilingBlock.below();
        if (level().getBlockState(hangPos).isAir() || !level().getBlockState(hangPos).isSolidRender(level(), hangPos)) {
            this.teleportTo(hangPos.getX() + 0.5, hangPos.getY(), hangPos.getZ() + 0.5);
        }
    }

    private Player findPlayerBelow(int range) {
        Player nearest = null;
        double nearestDist = range * range;
        for (Player player : level().players()) {
            if (!player.isAlive() || player.isSpectator()) continue;
            if (player.getY() >= this.getY()) continue;
            double dx = player.getX() - this.getX();
            double dy = player.getY() - this.getY();
            double dz = player.getZ() - this.getZ();
            double distSq = dx * dx + dy * dy + dz * dz;
            if (distSq <= range * range && distSq < nearestDist) {
                nearestDist = distSq;
                nearest = player;
            }
        }
        return nearest;
    }

    private void dismountFromCeiling() {
        this.setNoGravity(false);
        this.teleportTo(this.getX(), this.getY() - 1.0, this.getZ());
        this.setDeltaMovement(this.getDeltaMovement().add(0, -0.1, 0));
    }

    private void setupAnimationStates() {
        if (isHanging()) {
            idleAnimationState.stop();
            walkAnimationState.stop();
            if (!ceilinghangAnimationState.isStarted()) {
                ceilinghangAnimationState.start(this.tickCount);
            }
        } else {
            ceilinghangAnimationState.stop();
            if (this.getDeltaMovement().horizontalDistanceSqr() < 1.0E-6) {
                if (!idleAnimationState.isStarted()) {
                    idleAnimationState.start(this.tickCount);
                }
                walkAnimationState.stop();
            } else {
                idleAnimationState.stop();
                if (!walkAnimationState.isStarted()) {
                    walkAnimationState.start(this.tickCount);
                }
            }
        }
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);
        AttributePresets.catLike(attributes);
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(48.0D);
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue(8.0D);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0.9D);
        attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(0.8D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        // Hyper aggressive: melee attack first
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.9D));
        if (GoalUtils.hasGroundPathNavigation(this)) {
            this.goalSelector.addGoal(3, new OpenDoorGoal(this, true));
        }
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, null));
    }

    @Override
    public int getTicksRequiredToFreeze() {
        return 200;
    }

    @Override
    public Gender getGender() {
        return Gender.MALE;
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.REPLICATION;
    }

    @Override
    public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.getColor("#dddddd");
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ChangedEntity.createLatexAttributes();
    }

    public static boolean checkLooseSquidDogLimbSpawnRules(EntityType<LooseSquidDogLimbEntity> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (reason != MobSpawnType.NATURAL) {
            return true;
        }
        if (world instanceof WorldGenRegion) {
            return false;
        }
        int y = pos.getY();
        if (y > 10 || y < -64) {
            return false;
        }
        if (world.getBrightness(LightLayer.SKY, pos) > random.nextInt(80)) {
            return false;
        }
        if (world.getBrightness(LightLayer.BLOCK, pos) > 2) {
            return false;
        }
        return net.minecraft.world.entity.monster.Monster.checkMonsterSpawnRules(entityType, world, reason, pos, random);
    }
}
