package net.jerika.furmutage.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class LooseSquidDogLimbEntity extends Monster {

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState ceilinghangAnimationState = new AnimationState();

    private static final int TICKS_WITHOUT_TARGET_TO_TELEPORT = 600; // 30 seconds
    private static final int DISMOUNT_PLAYER_RANGE = 5;
    private static final int CEILING_SCAN_HEIGHT = 16;
    /** Ceiling must be at least this many blocks above the entity to hang; never hang from low ceilings. */
    private static final int MIN_CEILING_HEIGHT = 5;

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
        return false;
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

            if (isHanging() && getTarget() != null) {
                dismountFromCeiling();
            } else if (isHanging()) {
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
                boolean idleNoTarget = ticksWithoutTarget >= TICKS_WITHOUT_TARGET_TO_TELEPORT;
                boolean notPathfinding = !this.getNavigation().isInProgress();
                if (idleNoTarget && notPathfinding && hasCeilingHighEnough()) {
                    BlockPos ceiling = findCeilingAbove();
                    if (ceiling != null) {
                        teleportToCeiling(ceiling);
                    }
                }
            }
        }
    }

    private int lastTargetTime = 0;

    private boolean hasCeilingHighEnough() {
        BlockPos pos = blockPosition();
        for (int i = MIN_CEILING_HEIGHT; i <= CEILING_SCAN_HEIGHT; i++) {
            BlockState state = level().getBlockState(pos.above(i));
            if (!state.isAir() && state.isSolidRender(level(), pos.above(i))) {
                return true;
            }
        }
        return false;
    }

    private BlockPos findCeilingAbove() {
        BlockPos pos = blockPosition();
        for (int i = MIN_CEILING_HEIGHT; i <= CEILING_SCAN_HEIGHT; i++) {
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
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.75D));
        if (GoalUtils.hasGroundPathNavigation(this)) {
            this.goalSelector.addGoal(3, new OpenDoorGoal(this, true));
        }
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, null));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 10.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D);
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
        return Monster.checkMonsterSpawnRules(entityType, world, reason, pos, random);
    }
}
