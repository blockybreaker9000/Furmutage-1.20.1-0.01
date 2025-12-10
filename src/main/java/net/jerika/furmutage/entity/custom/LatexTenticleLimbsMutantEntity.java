package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.ai.LatexTenticleLimbsMutantAi;
import net.jerika.furmutage.ai.SpookyFollowPlayerGoal;
import net.ltxprogrammer.changed.entity.beast.WhiteLatexEntity;
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

public class LatexTenticleLimbsMutantEntity extends Monster {
    public LatexTenticleLimbsMutantEntity(EntityType<? extends Monster> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }




    private static final EntityDataAccessor<Boolean> LATEX_TENTICLE_LIMBS_MUTANT_ATTACK =
            SynchedEntityData.defineId(LatexTenticleLimbsMutantEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide()) {
        setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        // Idle animation: play continuously when stationary
        if (this.getDeltaMovement().horizontalDistanceSqr() < 1.0E-6) {
            if (!this.idleAnimationState.isStarted()) {
                this.idleAnimationState.start(this.tickCount);
            }
        } else {
            this.idleAnimationState.stop();
        }

        // Attack animation
        if(this.ismutantLimbsAttack() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 20; //animation Length
            attackAnimationState.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout;
        }

        if(!this.ismutantLimbsAttack() && attackAnimationTimeout <= 0) {
            attackAnimationState.stop();
        }
    }
    private static final int MOB_FLAG_AGGRESSIVE = 4;

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if(this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6f, 1f);
        } else {
            f = 0f;
        }
        this.walkAnimation.update(f, 0.2f);
    }

    public void setmutantLimbsAttack(boolean mutantLimbsAttack) {
        this.entityData.set(LATEX_TENTICLE_LIMBS_MUTANT_ATTACK, mutantLimbsAttack);
    }


    public boolean ismutantLimbsAttack() {
        return this.entityData.get(LATEX_TENTICLE_LIMBS_MUTANT_ATTACK);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LATEX_TENTICLE_LIMBS_MUTANT_ATTACK, false);
    }

    @Override
    protected void registerGoals(){
        this.goalSelector.addGoal(10, new FloatGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Monster.class)));
        // Spooky AI - follow players at a distance
        this.goalSelector.addGoal(2, new SpookyFollowPlayerGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.10));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new LatexTenticleLimbsMutantAi(this, 1.0, true));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, WhiteLatexEntity.class)));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true, false));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Villager.class, true, false));
        this.targetSelector.addGoal(1, new MeleeAttackGoal(this, (double)0.0F, true));
    }
    public static AttributeSupplier.Builder createMobAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 90)
                .add(Attributes.MOVEMENT_SPEED, 0.15)
                .add(Attributes.ARMOR_TOUGHNESS, 10)
                .add(Attributes.ATTACK_KNOCKBACK, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 3)
                .add(Attributes.FOLLOW_RANGE, 56.0)
                .add(Attributes.JUMP_STRENGTH, 5.0);
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
    protected SoundEvent getDeathSound() { return SoundEvents.ZOGLIN_DEATH;

    }

    @Override
    public boolean fireImmune() {
        return true; // Fire resistant
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.0f; // No water slowdown - fast in water
    }

    @Override
    public void travel(net.minecraft.world.phys.Vec3 pTravelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            // Increase movement speed in water
            this.moveRelative(1.0f, pTravelVector);
            this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(1.0));
        } else {
            super.travel(pTravelVector);
        }
    }

}

