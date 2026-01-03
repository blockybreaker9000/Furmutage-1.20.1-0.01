package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.ai.ChangedEntityImprovedPathfindingGoal;
import net.jerika.furmutage.ai.ExoMutantJumpClimbGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LatexExoMutantEntity extends Monster {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;
    public final AnimationState jumpAnimationState = new AnimationState();
    public int jumpAnimationTimeout = 0;
    public final AnimationState swimAnimationState = new AnimationState();

    public LatexExoMutantEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            setupAnimationStates();
        }

        // Wall climbing logic
        if (!this.level().isClientSide && this.horizontalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0.2D, this.getDeltaMovement().z);
        }
    }

    private void setupAnimationStates() {
        // Idle animation: play continuously when stationary (like pure white latex)
        if (this.getDeltaMovement().horizontalDistanceSqr() < 1.0E-6 && this.onGround()) {
            if (!this.idleAnimationState.isStarted()) {
                this.idleAnimationState.start(this.tickCount);
            }
        } else {
            this.idleAnimationState.stop();
        }

        // Attack animation - triggered when attacking
        if (this.swinging && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 12; // Animation length (0.6s * 20 ticks)
            attackAnimationState.start(this.tickCount);
        } else {
            --this.attackAnimationTimeout;
        }

        if (!this.swinging && attackAnimationTimeout <= 0) {
            attackAnimationState.stop();
        }

        // Jump animation - only trigger if entity is 2 blocks or higher from ground
        if (isAtLeast2BlocksHigh()) {
            if (!this.jumpAnimationState.isStarted()) {
                jumpAnimationTimeout = 10;
                this.jumpAnimationState.start(this.tickCount);
            }
        } else if (this.onGround() && jumpAnimationTimeout <= 0) {
            this.jumpAnimationState.stop();
        } else if (jumpAnimationTimeout > 0) {
            --this.jumpAnimationTimeout;
        }

        // Swimming animation - play when in water
        if (this.isInWater()) {
            if (!this.swimAnimationState.isStarted()) {
                this.swimAnimationState.start(this.tickCount);
            }
        } else {
            this.swimAnimationState.stop();
        }
    }

    /**
     * Checks if the entity is at least 2 blocks high from the ground
     */
    private boolean isAtLeast2BlocksHigh() {
        if (this.onGround()) {
            return false;
        }

        BlockPos entityPos = this.blockPosition();
        double entityY = this.getY();
        
        // Find the first solid block below the entity
        for (int y = 0; y <= 10; y++) {
            BlockPos checkPos = entityPos.below(y);
            BlockState state = this.level().getBlockState(checkPos);
            
            // Check if block is solid (not air and has collision shape)
            if (!state.isAir() && !state.getCollisionShape(this.level(), checkPos).isEmpty()) {
                double groundY = checkPos.getY() + 1.0; // Top of the block
                double distanceFromGround = entityY - groundY;
                
                // Check if entity is at least 2 blocks (2.0) above the ground
                return distanceFromGround >= 2.0;
            }
        }
        
        // If no solid block found within 10 blocks, assume we're high enough
        return true;
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6f, 1f);
        } else {
            f = 0f;
        }
        this.walkAnimation.update(f, 0.2f);
    }


    @Override
    protected void registerGoals() {
        // Changed mod style AI goals - matching priority order
        // Priority 1: Custom jump/climb goal (ExoMutantJumpClimbGoal replaces MeleeAttackGoal)
        this.goalSelector.addGoal(1, new ExoMutantJumpClimbGoal(this, 1.5D, true));
        
        // Priority 2: Improved pathfinding for jumping onto blocks, climbing ladders, and gap jumping
        this.goalSelector.addGoal(2, new ChangedEntityImprovedPathfindingGoal(this));
        
        // Priority 3: Random stroll
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.3, 120, false));
        
        // Priority 3: Leap at target (only when target is above) - ExoMutant already has custom jumping
        // Skipping ChangedStyleLeapAtTargetGoal since ExoMutantJumpClimbGoal handles jumping
        
        // Priority 4: Open doors (if has ground navigation)
        if (net.minecraft.world.entity.ai.util.GoalUtils.hasGroundPathNavigation(this))
            this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
        
        // Priority 5: Float in water
        this.goalSelector.addGoal(5, new FloatGoal(this));
        
        // Priority 6: Look at player
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 7.0F));
        
        // Priority 7: Random look around
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        
        // Priority 8: Look at villager
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Villager.class, 7.0F, 0.2F));
        
        // Target priorities - Changed mod style
        // Priority 1: Hurt by target
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
                .add(Attributes.MAX_HEALTH, 160.0D) // 80 hearts
                .add(Attributes.MOVEMENT_SPEED, 0.35D) // Increased speed
                .add(Attributes.ARMOR_TOUGHNESS, 5.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.JUMP_STRENGTH, 2.0D); // High jump capability
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
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        // No fall damage
        return false;
    }

    @Override
    protected float getWaterSlowDown() {
        // No water slowdown - fast swimming like tiger shark from Changed mod
        return 0.0f;
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            // Fast water movement like tiger shark from Changed mod
            // Using 1.0x speed multiplier for balanced swimming speed
            this.moveRelative(1.0f, pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D)); // Slight drag for realism
        } else {
            super.travel(pTravelVector);
        }
    }

    @Override
    public boolean onClimbable() {
        // Allow climbing on walls
        return this.horizontalCollision || super.onClimbable();
    }

    @Override
    public void makeStuckInBlock(BlockState pState, Vec3 pMotionMultiplier) {
        // Don't get stuck in cobwebs when climbing
        if (pState.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(pState, new Vec3(0.8D, 0.8D, 0.8D));
        } else {
            super.makeStuckInBlock(pState, pMotionMultiplier);
        }
    }

    public void setExoMutantAttack(boolean b) {
    }
}

