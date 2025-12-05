package net.jerika.furmutage.entity.custom;

import net.ltxprogrammer.changed.entity.beast.WhiteLatexEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LatexMutantFamilyEntity extends Monster {
    public LatexMutantFamilyEntity(EntityType<? extends Monster> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide()) {

        }
    }

    private void setupAnimationState() {
        if (this.idleAnimationTimeout <= 0)
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
        this.idleAnimationState.start(this.tickCount);
        {
            --this.idleAnimationTimeout;
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
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.10));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
    }
    public static AttributeSupplier.Builder createAttribute() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 500)
                .add(Attributes.MOVEMENT_SPEED, -0.5)
                .add(Attributes.ARMOR_TOUGHNESS, 10)
                .add(Attributes.ATTACK_KNOCKBACK, 2.5)
                .add(Attributes.ATTACK_DAMAGE, 20);
    }


    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SILVERFISH_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return SoundEvents.RABBIT_HURT;
    }
    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.RABBIT_DEATH;
    }

}

