package net.jerika.furmutage.entity.custom;

import net.jerika.furmutage.ai.BacteriaJumpAttackGoal;
import net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal;
import net.jerika.furmutage.ai.latex_beast_ai.ChangedStyleLeapAtTargetGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.jerika.furmutage.furmutage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
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

    /**
     * When the bacteria successfully hits a humanoid target, try to transfur it
     * into the Changed mod's phage latex wolf variant instead of doing only
     * normal damage.
     */
    @Override
    public boolean doHurtTarget(Entity target) {
        boolean result = super.doHurtTarget(target);

        if (!this.level().isClientSide() && target instanceof LivingEntity living) {
            tryPhageTransfur(living);
        }

        return result;
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

    /**
     * Best-effort player transfur into the Changed mod's phage latex wolf variant,
     * using reflection so this compiles even if the API changes.
     */
    private void transfurPlayerToPhageLatex(Player player) {
        try {
            // Get the LatexVariantInstance for the player (same pattern as in TaintedBlockEvents)
            Class<?> instanceClass = Class.forName("net.ltxprogrammer.changed.entity.LatexVariantInstance");
            java.lang.reflect.Method getMethod = instanceClass.getMethod("get", Player.class);
            Object instance = getMethod.invoke(null, player);

            if (instance == null) {
                return;
            }

            // Try to find a phage latex wolf variant field
            Class<?> variantClass = Class.forName("net.ltxprogrammer.changed.init.ChangedVariants");
            Object phageVariant = null;

            // Common expected field name
            try {
                java.lang.reflect.Field phageField = variantClass.getField("PHAGE_LATEX_WOLF");
                phageVariant = phageField.get(null);
            } catch (NoSuchFieldException ignored) {
                // Fallback: search any field with "PHAGE" and "WOLF" in the name
                for (java.lang.reflect.Field field : variantClass.getFields()) {
                    String name = field.getName().toUpperCase();
                    if (name.contains("PHAGE") && name.contains("WOLF")) {
                        phageVariant = field.get(null);
                        break;
                    }
                }
            }

            if (phageVariant == null) {
                return;
            }

            // Try several possible method names to apply the variant
            try {
                java.lang.reflect.Method setVariantMethod = instanceClass.getMethod("setLatexVariant", Object.class);
                setVariantMethod.invoke(instance, phageVariant);
            } catch (IllegalArgumentException e) {
                // Catch errors from Changed mod trying to use attributes that don't exist in 1.20.1
                // (e.g., attack_knockback)
                if (e.getMessage() != null && e.getMessage().contains("attack_knockback")) {
                    furmutage.LOGGER.debug("Ignoring attack_knockback attribute error from Changed mod transfur: {}", e.getMessage());
                    return; // Exit early if this is the attack_knockback error
                }
                throw e; // Re-throw if it's a different error
            } catch (NoSuchMethodException e1) {
                try {
                    java.lang.reflect.Method transfurMethod = instanceClass.getMethod("transfur", Object.class);
                    transfurMethod.invoke(instance, phageVariant);
                } catch (NoSuchMethodException e2) {
                    try {
                        java.lang.reflect.Method transfurPlayerMethod = instanceClass.getMethod("transfur", Player.class, Object.class);
                        transfurPlayerMethod.invoke(null, player, phageVariant);
                    } catch (NoSuchMethodException e3) {
                        try {
                            java.lang.reflect.Method transfurStaticMethod = variantClass.getMethod("transfur", Player.class, Object.class);
                            transfurStaticMethod.invoke(null, player, phageVariant);
                        } catch (NoSuchMethodException e4) {
                            try {
                                Class<?> helperClass = Class.forName("net.ltxprogrammer.changed.process.ChangedTransfurHelper");
                                java.lang.reflect.Method transfurHelperMethod = helperClass.getMethod("transfur", Player.class, Object.class);
                                transfurHelperMethod.invoke(null, player, phageVariant);
                            } catch (Exception e5) {
                                // Last attempt: scan for any suitable transfur-like method
                                for (java.lang.reflect.Method method : variantClass.getMethods()) {
                                    String name = method.getName().toLowerCase();
                                    if (!name.contains("transfur")) continue;
                                    try {
                                        if (method.getParameterCount() == 2 &&
                                                method.getParameterTypes()[0] == Player.class) {
                                            method.invoke(null, player, phageVariant);
                                            break;
                                        }
                                    } catch (Exception ignored2) {
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
            furmutage.LOGGER.warn("Failed to transfur player to phage latex wolf: " + t.getMessage());
        }
    }

    /**
     * Replace a non-player humanoid (villager / raider / zombie / skeleton)
     * with a phage latex wolf entity from Changed, if present.
     */
    private void replaceHumanoidWithPhageLatex(LivingEntity original, net.minecraft.server.level.ServerLevel level) {
        try {
            EntityType<?> phageType = ForgeRegistries.ENTITY_TYPES.getValue(
                    ResourceLocation.tryParse("changed:phage_latex_wolf_male")
            );

            // Fallback: try to find any entity whose description id contains "phage" and "wolf"
            if (phageType == null) {
                for (EntityType<?> type : ForgeRegistries.ENTITY_TYPES.getValues()) {
                    String id = type.getDescriptionId().toLowerCase();
                    if (id.contains("phage") && id.contains("wolf")) {
                        phageType = type;
                        break;
                    }
                }
            }

            if (phageType != null && phageType.create(level) instanceof PathfinderMob phageEntity) {
                phageEntity.moveTo(original.getX(), original.getY(), original.getZ(),
                        original.getYRot(), original.getXRot());

                // Try to preserve health percentage if possible
                if (original.getMaxHealth() > 0) {
                    float healthPercent = original.getHealth() / original.getMaxHealth();
                    phageEntity.setHealth(phageEntity.getMaxHealth() * healthPercent);
                }

                try {
                    phageEntity.finalizeSpawn(level,
                            level.getCurrentDifficultyAt(original.blockPosition()),
                            MobSpawnType.EVENT, null, null);
                } catch (IllegalArgumentException e) {
                    // Catch errors from Changed mod trying to use attributes that don't exist in 1.20.1
                    // (e.g., attack_knockback)
                    if (e.getMessage() != null && e.getMessage().contains("attack_knockback")) {
                        furmutage.LOGGER.debug("Ignoring attack_knockback attribute error from Changed mod: {}", e.getMessage());
                    } else {
                        throw e; // Re-throw if it's a different error
                    }
                }

                level.addFreshEntity(phageEntity);
                original.remove(Entity.RemovalReason.DISCARDED);
            }
        } catch (Throwable t) {
            furmutage.LOGGER.warn("Failed to replace humanoid with phage latex wolf: " + t.getMessage());
        }
    }

    /**
     * Attempt to transfur a humanoid target into a phage latex wolf.
     * Players are transfurred via the Changed API; non-player humanoids are
     * replaced with the phage latex wolf entity from the registry.
     */
    private void tryPhageTransfur(LivingEntity target) {
        // Don't re-transfur Changed entities
        if (isChangedEntity(target)) {
            return;
        }

        // Only affect humanoid-style entities
        boolean isHumanoid = target instanceof Player ||
                             target instanceof Villager ||
                             target instanceof Zombie ||
                             target instanceof net.minecraft.world.entity.monster.Skeleton ||
                             target instanceof net.minecraft.world.entity.raid.Raider;

        if (!isHumanoid) {
            return;
        }

        if (target instanceof Player player) {
            transfurPlayerToPhageLatex(player);
        } else if (target.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            replaceHumanoidWithPhageLatex(target, serverLevel);
        }
    }

    private boolean isChangedEntity(LivingEntity entity) {
        String className = entity.getClass().getName();
        return className.startsWith("net.ltxprogrammer.changed.entity");
    }

    @Override
    protected void registerGoals() {
        // Changed mod style AI goals - matching priority order
        // Priority 1: Custom jump attack goal (BacteriaJumpAttackGoal replaces MeleeAttackGoal)
        this.goalSelector.addGoal(1, new BacteriaJumpAttackGoal(this, 1.2D, true));
        
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
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 7.0F));
        
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
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.JUMP_STRENGTH, 5.0)
                .add(Attributes.FOLLOW_RANGE, 60.0D);
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

