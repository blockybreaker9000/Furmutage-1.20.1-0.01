package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.entity.ModEntities;
import net.jerika.furmutage.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class LatexMutantFamilyEntity extends Animal {
    public LatexMutantFamilyEntity(EntityType<? extends Animal> aEntityType, Level pLevel) {
        super(aEntityType, pLevel);
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

        this.goalSelector.addGoal(1, new BreedGoal(this, 1.50));
        this.goalSelector.addGoal(1, new TemptGoal(this, 1.20, Ingredient.of(ModItems.ROSELIGHT.get()), false));
        this.goalSelector.addGoal(1, new FollowParentGoal(this, 1.10));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.10));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttribute() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 5)
                .add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(Attributes.ARMOR_TOUGHNESS, 1)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5)
                .add(Attributes.ATTACK_DAMAGE, 2);
    }
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.MUGLING.get().create(pLevel);
    }

    @Override
    public boolean isFood(ItemStack pstack) {
        return pstack.is(ModItems.ROSELIGHT.get());
    }
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SILVERFISH_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.RABBIT_HURT;
    }
    @Nullable
    protected SoundEvent getDeathSound(DamageSource pDamageSource) {
        return SoundEvents.RABBIT_DEATH;
    }

}
