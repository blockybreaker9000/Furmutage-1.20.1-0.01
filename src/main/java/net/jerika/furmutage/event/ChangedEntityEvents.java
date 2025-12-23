package net.jerika.furmutage.event;

import net.jerika.furmutage.ai.watching_you.ChangedDistantStareGoal;
import net.jerika.furmutage.ai.watching_you.ExoFollowBehindGoal;
import net.jerika.furmutage.ai.latex_beast_ai.LongRangePlayerTargetGoal;
import net.jerika.furmutage.ai.latex_beast_ai.StalkAndHideGoal;
import net.jerika.furmutage.ai.VerticalLungeAttackGoal;
import net.jerika.furmutage.entity.custom.MuglingEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChangedEntityEvents {
    // Track entities we've already modified to avoid duplicate goals
    private static final Set<LivingEntity> processedEntities = java.util.Collections.newSetFromMap(new WeakHashMap<>());
    // Track transfurred players who recently attacked a Changed entity (for retaliation)
    private static final Map<Player, Long> transfurredAggressors = new WeakHashMap<>();
    private static final long RETALIATION_DURATION_TICKS = 20L * 20L; // 20 seconds
    
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        // Only process on server side
        if (event.getLevel().isClientSide()) {
            return;
        }
        
        // Check if entity is from Changed mod
        if (event.getEntity() instanceof LivingEntity livingEntity) {
            // Skip if we've already processed this entity
            if (processedEntities.contains(livingEntity)) {
                return;
            }
            
            String entityClassName = livingEntity.getClass().getName();
            
            // Check if entity is from Changed mod (net.ltxprogrammer.changed package)
            if (entityClassName.startsWith("net.ltxprogrammer.changed.entity")) {
                // Check if it's a PathfinderMob (has goal selector)
                if (livingEntity instanceof PathfinderMob pathfinderMob) {
                    // Add goal to target Muglings with priority 2
                    // The goal will only activate if the entity can see Muglings (mustSeeTarget = true)
                    pathfinderMob.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(
                            pathfinderMob, 
                            MuglingEntity.class, 
                            true,  // mustSeeTarget - only attack if they can see the Mugling
                            true  // mustReachTarget - require pathfinding to Mugling
                    ));
                    
                    // Add long-range player detection (128 blocks) if line of sight
                    // This allows Changed entities to see players from far away
                    pathfinderMob.targetSelector.addGoal(1, new LongRangePlayerTargetGoal(pathfinderMob));

                    // Add stalking and hiding behavior (30% chance per entity)
                    // This goal will stalk players, hide behind blocks, then attack
                    // The goal itself checks for the chance, so we always add it
                    pathfinderMob.goalSelector.addGoal(3, new StalkAndHideGoal(pathfinderMob, 1.0D, 2.0F));

                    // Add distant stare behavior (very small chance)
                    // Changed entities will occasionally stare at players from far away
                    pathfinderMob.goalSelector.addGoal(7, new ChangedDistantStareGoal(pathfinderMob));

                    // Add exoskeleton follow-from-behind behavior for exo-type Changed entities.
                    // Small random chance to activate, cancels as soon as the player looks at it.
                    pathfinderMob.goalSelector.addGoal(4, new ExoFollowBehindGoal(pathfinderMob, 0.9D));
                    
                    // Add uncommon vertical lunge attack when player is 5 blocks high
                    pathfinderMob.goalSelector.addGoal(2, new VerticalLungeAttackGoal(pathfinderMob));
                    
                    processedEntities.add(livingEntity);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        // Only process on server side
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();

        // Check if damage is from fire or lava using damage type tags (more reliable)
        if (source.is(net.minecraft.tags.DamageTypeTags.IS_FIRE)) {
            String entityClassName = entity.getClass().getName();

            // Check if entity is from Changed mod or furmutage mod
            if (entityClassName.startsWith("net.ltxprogrammer.changed.entity") ||
                    entityClassName.startsWith("net.jerika.furmutage.entity")) {
                // Cancel fire and lava damage
                event.setCanceled(true);
                return;
            }
        }

        // If a transfurred player attacks a Changed entity, mark them as an aggressor
        if (entity != null && source.getEntity() instanceof Player player) {
            String victimClassName = entity.getClass().getName();
            if (victimClassName.startsWith("net.ltxprogrammer.changed.entity") && isPlayerTransfurred(player)) {
                long now = entity.level().getGameTime();
                transfurredAggressors.put(player, now + RETALIATION_DURATION_TICKS);
            }
        }

        // If a Changed entity attacks a transfurred player, normally we cancel the damage,
        // but if that player recently attacked a Changed entity (marked as aggressor),
        // then allow the damage as retaliation.
        if (entity instanceof Player player && source.getEntity() != null) {
            String attackerClassName = source.getEntity().getClass().getName();
            if (attackerClassName.startsWith("net.ltxprogrammer.changed.entity") && isPlayerTransfurred(player)) {
                long now = entity.level().getGameTime();
                Long allowedUntil = transfurredAggressors.get(player);
                boolean isAggressor = allowedUntil != null && allowedUntil >= now;

                if (!isAggressor) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        // Only process on server side
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        LivingEntity entity = event.getEntity();
        
        // Check if entity is from Changed mod
        String entityClassName = entity.getClass().getName();
        boolean isChanged = entityClassName.startsWith("net.ltxprogrammer.changed.entity");
        
        if (isChanged && entity.isEffectiveAi()) {
            net.minecraft.world.phys.Vec3 currentMovement = entity.getDeltaMovement();
            
            // Handle water speed boost
            if (entity.isInWater()) {
                if (currentMovement.horizontalDistanceSqr() > 5.5) {
                    // Boost horizontal movement in water
                    double speedBoost = 100.0; //  speed increase in water
                    entity.setDeltaMovement(
                        currentMovement.x * speedBoost,
                        currentMovement.y,
                        currentMovement.z * speedBoost
                    );
                }
            }
            
            // Handle lava speed boost and floating
            if (entity.isInLava()) {
                // Increase movement speed in lava by boosting delta movement
                if (currentMovement.horizontalDistanceSqr() > 1.0) {
                    // Boost horizontal movement in lava
                    double speedBoost = 1.0; // speed increase in lava
                    entity.setDeltaMovement(
                        currentMovement.x * speedBoost,
                        currentMovement.y,
                        currentMovement.z * speedBoost
                    );
                }
                
                // Make entity float in lava (add upward force to counteract sinking)
                double yMovement = currentMovement.y;
                // If sinking (negative Y movement), add upward force
                if (yMovement < 0.1) {
                    // Add upward force to float
                    double floatForce = 0.1; // Upward force to keep floating
                    entity.setDeltaMovement(
                        currentMovement.x,
                        Math.max(yMovement + floatForce, 0.1), // Keep at least slight upward movement
                        currentMovement.z
                    );
                }
            }

            // If this is a Changed mob and its current target is a transfurred player,
            // normally we clear the target so it becomes non-aggressive towards that player.
            // But if the player recently attacked a Changed entity, allow it to keep the target.
            if (entity instanceof PathfinderMob pathfinderMob && pathfinderMob.getTarget() instanceof Player player) {
                if (isPlayerTransfurred(player)) {
                    long now = entity.level().getGameTime();
                    Long allowedUntil = transfurredAggressors.get(player);
                    boolean isAggressor = allowedUntil != null && allowedUntil >= now;

                    if (!isAggressor) {
                        pathfinderMob.setTarget(null);
                    }
                }
            }
        }
    }

    /**
     * Best-effort check to see if a player is transfurred using the Changed mod API.
     * Uses reflection so this code still compiles even if the Changed classes change.
     *
     * If the reflection calls fail for any reason, this safely returns false.
     */
    private static boolean isPlayerTransfurred(Player player) {
        try {
            Class<?> instanceClass = Class.forName("net.ltxprogrammer.changed.entity.LatexVariantInstance");
            java.lang.reflect.Method getMethod = instanceClass.getMethod("get", Player.class);
            Object instance = getMethod.invoke(null, player);
            if (instance == null) {
                return false;
            }

            // Try to get the latex variant from the instance
            java.lang.reflect.Method getVariantMethod = instanceClass.getMethod("getLatexVariant");
            Object variant = getVariantMethod.invoke(instance);
            return variant != null;
        } catch (Throwable t) {
            // If anything goes wrong (class not found, method missing, etc.),
            // just treat the player as not transfurred to avoid crashes.
            return false;
        }
    }
}

