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
import net.jerika.furmutage.ai.mugling.MuglingGroupGoal;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class MuglingEntity extends Animal {
    public MuglingEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

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
        
        // Panic when hurt - highest priority for survival
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
        
        // Avoid other mobs (but not other Muggings) - smart fleeing
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Monster.class, 10.0F, 1.4D, 1.8D) {
            @Override
            public void start() {
                super.start();
                // Increase speed when fleeing from monsters
                MuglingEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.7);
            }
            
            @Override
            public void stop() {
                super.stop();
                // Return to normal speed
                MuglingEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5);
            }
        });
        
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Animal.class, 8.0F, 1.3D, 1.6D) {
            @Override
            public boolean canUse() {
                // Only avoid animals that are not Muggings
                return super.canUse() && !(this.toAvoid instanceof MuglingEntity);
            }
        });

        // Group together with other Muggings for safety
        this.goalSelector.addGoal(3, new MuglingGroupGoal(this, 1.1D, 3.0F, 12.0F));
        
        // Breeding and social behaviors
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.50));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.20, Ingredient.of(ModItems.ROSELIGHT.get()), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.10));
        
        // Exploration and awareness
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.10));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 5)
                .add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(Attributes.ARMOR_TOUGHNESS, 1)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 60.0D);
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
