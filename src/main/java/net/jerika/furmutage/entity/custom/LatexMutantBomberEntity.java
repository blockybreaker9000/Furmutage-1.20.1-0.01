package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal;
import net.jerika.furmutage.ai.latex_beast_ai.ChangedStyleLeapAtTargetGoal;
import net.jerika.furmutage.sound.ModSounds;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LatexMutantBomberEntity extends Monster {
    private static final double EXPLOSION_DISTANCE = 3.0D; // Distance to trigger explosion (3 blocks)
    
    public LatexMutantBomberEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            setupAnimationStates();
        } else {
            // Check if we're close enough to target to explode
            LivingEntity target = this.getTarget();
            if (target != null && target.isAlive()) {
                double distanceSqr = this.distanceToSqr(target);
                if (distanceSqr <= EXPLOSION_DISTANCE * EXPLOSION_DISTANCE) {
                    this.explode();
                }
            }
        }
    }

    private void setupAnimationStates() {
        // Idle animation: play continuously when stationary
        if (this.getDeltaMovement().horizontalDistanceSqr() < 1.0E-6) {
            if (this.idleAnimationTimeout <= 0) {
                this.idleAnimationTimeout = this.random.nextInt(40) + 80;
                this.idleAnimationState.start(this.tickCount);
            } else {
                --this.idleAnimationTimeout;
            }
        } else {
            this.idleAnimationState.stop();
        }
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

    private void explode() {
        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            float explosionRadius = 8.0F; // Very large explosion that destroys many blocks
            // Create explosion that destroys blocks
            serverLevel.explode(this, this.getX(), this.getY(), this.getZ(), explosionRadius, false, ExplosionInteraction.BLOCK);
            
            // Damage nearby entities
            this.level().getEntitiesOfClass(LivingEntity.class, 
                    this.getBoundingBox().inflate(explosionRadius, explosionRadius, explosionRadius),
                    entity -> entity != this && entity.isAlive())
                    .forEach(entity -> {
                        double distance = this.distanceTo(entity);
                        if (distance <= explosionRadius) {
                            float damage = 10.0F * (1.0F - (float)(distance / explosionRadius));
                            entity.hurt(serverLevel.damageSources().explosion(this, this), damage);
                        }
                    });
            
            // Remove this entity
            this.discard();
        }
    }

    @Override
    protected void registerGoals() {
        // Changed mod style AI goals - matching priority order
        // Priority 1: Move towards target (replaces MeleeAttackGoal for bomber)
        this.goalSelector.addGoal(1, new MoveTowardsTargetGoal(this, 1.0D, 32.0F));
        
        // Priority 2: Improved pathfinding for jumping onto blocks, climbing ladders, and gap jumping
        this.goalSelector.addGoal(2, new ChangedEntityImprovedPathfindingGoal(this));
        
        // Priority 3: Random stroll
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.3, 120, false));
        
        // Priority 4: Leap at target (only when target is above)
        this.goalSelector.addGoal(4, new ChangedStyleLeapAtTargetGoal(this, 0.4f));
        
        // Priority 5: Open doors (if has ground navigation)
        if (net.minecraft.world.entity.ai.util.GoalUtils.hasGroundPathNavigation(this))
            this.goalSelector.addGoal(5, new OpenDoorGoal(this, true));
        
        // Priority 6: Float in water
        this.goalSelector.addGoal(6, new FloatGoal(this));
        
        // Priority 7: Look at player
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
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.FOLLOW_RANGE, 60.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.5D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.LATEX_MUTANT_BOMBER_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return ModSounds.LATEX_MUTANT_BOMBER_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.LATEX_MUTANT_BOMBER_DEATH.get();
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        // Resistant to explosions - reduce explosion damage by 90%
        if (pSource.getMsgId().contains("explosion") || 
            pSource.getDirectEntity() != null && pSource.getDirectEntity().getClass().getSimpleName().contains("Explosion")) {
            // Only take 10% of explosion damage
            return super.hurt(pSource, pAmount * 0.1f);
        }
        return super.hurt(pSource, pAmount);
    }
}

