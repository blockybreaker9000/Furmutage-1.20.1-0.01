package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.ai.latex_beast_ai.ChangedStyleLeapAtTargetGoal;
import net.jerika.furmutage.ai.latex_beast_ai.DarkLatexChargerMutantAi;
import net.ltxprogrammer.changed.entity.beast.WhiteLatexEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DarkLatexChargerMutantEntity extends Monster {
    public DarkLatexChargerMutantEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setMaxUpStep(1.0f);
    }

    private static final EntityDataAccessor<Boolean> CHARGER_ATTACK =
            SynchedEntityData.defineId(DarkLatexChargerMutantEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;

    public final AnimationState idleAnimationState = new AnimationState();

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if (isChargerAttack() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 10;
            attackAnimationState.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout;
        }
        if (!isChargerAttack() && attackAnimationTimeout <= 0) {
            attackAnimationState.stop();
        }

        if (!attackAnimationState.isStarted() && attackAnimationTimeout <= 0) {
            if (!idleAnimationState.isStarted()) {
                idleAnimationState.start(this.tickCount);
            }
        } else {
            idleAnimationState.stop();
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        super.updateWalkAnimation(pPartialTick);
    }

    public void setChargerAttack(boolean attacking) {
        this.entityData.set(CHARGER_ATTACK, attacking);
    }

    public boolean isChargerAttack() {
        return this.entityData.get(CHARGER_ATTACK);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CHARGER_ATTACK, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new DarkLatexChargerMutantAi(this, 1.4, true));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.4, 120, false));
        this.goalSelector.addGoal(3, new ChangedStyleLeapAtTargetGoal(this, 0.5f));
        if (GoalUtils.hasGroundPathNavigation(this)) {
            this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
        }
        this.goalSelector.addGoal(5, new FloatGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Villager.class, 8.0F, 0.2F));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, WhiteLatexEntity.class).setAlertOthers());
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, null));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, 10, true, false, null));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, 10, true, false, null));
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 380)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ARMOR_TOUGHNESS, 8)
                .add(Attributes.ATTACK_DAMAGE, 8)
                .add(Attributes.ATTACK_KNOCKBACK, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 50.0)
                .add(Attributes.JUMP_STRENGTH, 4.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 4.0);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOGLIN_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return SoundEvents.ZOGLIN_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOGLIN_DEATH;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource == this.level().damageSources().onFire() ||
                pSource == this.level().damageSources().inFire() ||
                pSource == this.level().damageSources().lava() ||
                (pSource.getMsgId() != null && (pSource.getMsgId().contains("fire") || pSource.getMsgId().contains("lava")))) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.0f;
    }

    @Override
    public void travel(net.minecraft.world.phys.Vec3 pTravelVector) {
        if (this.isEffectiveAi() && (this.isInWater() || this.isInLava())) {
            float speedMultiplier = this.isInLava() ? 1.3f : 1.0f;
            this.moveRelative(speedMultiplier, pTravelVector);
            this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(this.isInLava() ? 1.2 : 1.0));
        } else {
            super.travel(pTravelVector);
        }
    }
}
