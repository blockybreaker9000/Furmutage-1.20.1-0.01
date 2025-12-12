package net.jerika.furmutage.entity.custom;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.jerika.furmutage.ai.DroneFlyingRandomStrollGoal;
import net.jerika.furmutage.ai.DroneMoveControl;
import net.jerika.furmutage.ai.DroneRangedAttackGoal;
import net.jerika.furmutage.ai.DroneGrenadeAttackGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TSCDroneEntity extends Monster implements RangedAttackMob {
    // Drone type: 0 = Bullet (default), 1 = Melee, 2 = Grenade
    private static final EntityDataAccessor<Integer> DRONE_TYPE =
            SynchedEntityData.defineId(TSCDroneEntity.class, EntityDataSerializers.INT);
    
    public TSCDroneEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new DroneMoveControl(this);
        this.setNoGravity(true);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        // Randomly assign drone type: 25% melee, 25% grenade, 50% bullet
        int random = this.random.nextInt(100);
        int droneType;
        if (random < 25) {
            droneType = 1; // Melee
        } else if (random < 50) {
            droneType = 2; // Grenade
        } else {
            droneType = 0; // Bullet (default)
        }
        this.entityData.define(DRONE_TYPE, droneType);
    }

    public int getDroneType() {
        return this.entityData.get(DRONE_TYPE);
    }

    public boolean isMeleeType() {
        return this.getDroneType() == 1;
    }

    public boolean isGrenadeType() {
        return this.getDroneType() == 2;
    }

    public boolean isBulletType() {
        return this.getDroneType() == 0;
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    protected float getFlyingSpeed() {
        // Return the movement speed as flying speed since FLYING_SPEED attribute doesn't exist in 1.20.1
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        // Idle animation: play continuously
        if (!this.idleAnimationState.isStarted()) {
            this.idleAnimationState.start(this.tickCount);
        }

        // Attack animation
        if (this.swinging && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 15; // Animation length
            attackAnimationState.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout;
        }

        if (!this.swinging && attackAnimationTimeout <= 0) {
            attackAnimationState.stop();
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 8f, 1f);
        } else {
            f = 0f;
        }
        this.walkAnimation.update(f, 0.2f);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        
        // Register different attack goals based on drone type
        if (this.isMeleeType()) {
            // Melee-only drone
            this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
        } else if (this.isGrenadeType()) {
            // Grenade-throwing drone (throws every 20 seconds)
            this.goalSelector.addGoal(2, new DroneGrenadeAttackGoal(this, 1.0D, 100)); // 100 ticks = 5 seconds
        } else {
            // Default bullet-shooting drone
            this.goalSelector.addGoal(2, new DroneRangedAttackGoal(this, 1.0D, 20, 16.0F));
        }
        
        this.goalSelector.addGoal(3, new DroneFlyingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Villager.class, true, false));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, IronGolem.class, true, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BEE_LOOP;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    @Override
    public boolean fireImmune() {
        return true; // Fire and lava resistant
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        // Cancel fire and lava damage
        if (pSource == this.level().damageSources().onFire() ||
            pSource == this.level().damageSources().inFire() ||
            pSource == this.level().damageSources().lava() ||
            pSource.getMsgId().contains("fire") ||
            pSource.getMsgId().contains("lava")) {
            return false;
        }
        
        // Cancel damage from TSCDroneBulletProjectile (friendly fire immunity)
        if (pSource.getDirectEntity() instanceof net.jerika.furmutage.entity.TSCDroneBulletProjectile) {
            return false;
        }
        
        // Cancel damage from TSCShockGrenadeProjectile explosions
        if (pSource.getDirectEntity() instanceof net.jerika.furmutage.entity.TSCShockGrenadeProjectile ||
            (pSource.getEntity() instanceof net.jerika.furmutage.entity.TSCShockGrenadeProjectile) ||
            (pSource.getMsgId().contains("explosion") && pSource.getEntity() instanceof net.jerika.furmutage.entity.TSCShockGrenadeProjectile)) {
            return false;
        }
        
        return super.hurt(pSource, pAmount);
    }

    @Override
    protected boolean isFlapping() {
        return true;
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false; // Flying entities don't take fall damage
    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        // This is called by RangedAttackGoal, but we handle it in DroneRangedAttackGoal
        // So this can be empty or delegate to the goal
    }
}

