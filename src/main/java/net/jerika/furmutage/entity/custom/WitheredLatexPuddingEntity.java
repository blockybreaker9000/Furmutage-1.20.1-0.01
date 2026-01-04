package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.ai.ChangedEntityImprovedPathfindingGoal;
import net.jerika.furmutage.ai.ChangedStyleLeapAtTargetGoal;
import net.jerika.furmutage.ai.latex_beast_ai.PuddingSprintAttackGoal;
import net.ltxprogrammer.changed.entity.beast.WhiteLatexEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
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

public class WitheredLatexPuddingEntity extends Monster {
    public WitheredLatexPuddingEntity(EntityType<? extends Monster> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

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


    @Override
    protected void registerGoals(){
        // Changed mod style AI goals - matching priority order
        // Priority 1: Custom sprint attack goal (PuddingSprintAttackGoal replaces MeleeAttackGoal)
        this.goalSelector.addGoal(1, new PuddingSprintAttackGoal(this, true));
        
        // Priority 2: Improved pathfinding for jumping onto blocks, climbing ladders, and gap jumping
        this.goalSelector.addGoal(2, new ChangedEntityImprovedPathfindingGoal(this));
        
        // Priority 3: Random stroll
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.3, 120, false));
        
        // Priority 4: Leap at target (only when target is above)
        this.goalSelector.addGoal(4, new ChangedStyleLeapAtTargetGoal(this, 0.4f));
        
        // Priority 5: Open doors (if has ground navigation)
        if (GoalUtils.hasGroundPathNavigation(this))
            this.goalSelector.addGoal(5, new OpenDoorGoal(this, true));
        
        // Priority 6: Float in water
        this.goalSelector.addGoal(6, new FloatGoal(this));
        
        // Priority 7: Look at player
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 7.0F));
        
        // Priority 8: Random look around
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        
        // Priority 8: Look at villager
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Villager.class, 7.0F, 0.2F));
        
        // Target priorities - Changed mod style
        // Priority 1: Hurt by target (alert white latex entities)
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, WhiteLatexEntity.class).setAlertOthers());
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        
        // Priority 2: Target players
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
        
        // Priority 3: Target villagers
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, true, false));
        
        // Priority 4: Target iron golems
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, true, false));
    }
    public static AttributeSupplier.Builder createMobAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.MOVEMENT_SPEED, 1.0)
                .add(Attributes.ARMOR_TOUGHNESS, 2)
                .add(Attributes.ATTACK_DAMAGE, 3)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 60.0)
                .add(Attributes.JUMP_STRENGTH, 2.0);
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

    public int getSwingTime() {
        return swingTime;
    }

    public void setSwingTime(int swingTime) {
        this.swingTime = swingTime;
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
        return super.hurt(pSource, pAmount);
    }

    @Override
    public void travel(net.minecraft.world.phys.Vec3 pTravelVector) {
        if (this.isEffectiveAi() && (this.isInWater() || this.isInLava())) {
            // Increase movement speed in water and lava
            float speedMultiplier = this.isInLava() ? 1.3f : 1.0f; // Slightly faster in lava
            this.moveRelative(speedMultiplier, pTravelVector);
            this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(this.isInLava() ? 1.2 : 1.0));
        } else {
            super.travel(pTravelVector);
        }
    }
}

