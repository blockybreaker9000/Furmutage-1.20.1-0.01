package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.ai.BacteriaJumpAttackGoal;
import net.jerika.furmutage.ai.VerticalLungeAttackGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LatexBacteriaEntity extends Monster {
    private static final EntityDataAccessor<Boolean> JUMPING = 
            SynchedEntityData.defineId(LatexBacteriaEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState jumpAnimationState = new AnimationState();
    public int jumpAnimationTimeout = 0;

    public LatexBacteriaEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        // Idle animation: play when stationary
        if (this.getDeltaMovement().horizontalDistanceSqr() < 1.0E-6 && this.onGround()) {
            if (!this.idleAnimationState.isStarted()) {
                this.idleAnimationState.start(this.tickCount);
            }
        } else {
            this.idleAnimationState.stop();
        }

        // Walk animation: play when moving
        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6 && this.onGround() && !this.isJumping()) {
            if (!this.walkAnimationState.isStarted()) {
                this.walkAnimationState.start(this.tickCount);
            }
        } else {
            this.walkAnimationState.stop();
        }

        // Jump animation
        if (this.isJumping() && jumpAnimationTimeout <= 0) {
            jumpAnimationTimeout = 20; // Animation length
            jumpAnimationState.start(this.tickCount);
        } else {
            --this.jumpAnimationTimeout;
        }

        if (!this.isJumping() && jumpAnimationTimeout <= 0) {
            jumpAnimationState.stop();
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
        this.walkAnimation.update(f, 0.5f); // Fast walk animation speed
    }

    public void setJumping(boolean jumping) {
        this.entityData.set(JUMPING, jumping);
        if (!jumping && this.onGround()) {
            // Reset jump animation when landing
            jumpAnimationTimeout = 0;
        }
    }

    public boolean isJumping() {
        return this.entityData.get(JUMPING);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(JUMPING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new VerticalLungeAttackGoal(this)); // Uncommon vertical lunge when player is 5 blocks high
        this.goalSelector.addGoal(2, new BacteriaJumpAttackGoal(this, 1.2D, true)); // Fast movement speed
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        // Target players and villagers
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1)
                .add(Attributes.JUMP_STRENGTH, 5.0)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SILVERFISH_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return SoundEvents.SILVERFISH_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_DEATH;
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
        return false; // Bacteria ignores fall damage
    }
}

