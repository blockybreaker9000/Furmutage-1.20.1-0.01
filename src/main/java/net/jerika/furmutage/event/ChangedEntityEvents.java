package net.jerika.furmutage.event;

import net.jerika.furmutage.entity.custom.MuglingEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChangedEntityEvents {
    // Track entities we've already modified to avoid duplicate goals
    private static final Set<LivingEntity> processedEntities = java.util.Collections.newSetFromMap(new WeakHashMap<>());
    
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
        
        // Check if entity is from Changed mod or furmutage mod
        String entityClassName = entity.getClass().getName();
        boolean isChangedOrFurmutage = entityClassName.startsWith("net.ltxprogrammer.changed.entity") || 
                                       entityClassName.startsWith("net.jerika.furmutage.entity");
        
        if (isChangedOrFurmutage && entity.isEffectiveAi()) {
            net.minecraft.world.phys.Vec3 currentMovement = entity.getDeltaMovement();
            
            // Handle water speed boost
            if (entity.isInWater()) {
                if (currentMovement.horizontalDistanceSqr() > 0.1) {
                    // Boost horizontal movement in water
                    double speedBoost = 1.0; //  speed increase in water
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
                if (currentMovement.horizontalDistanceSqr() > 0.50) {
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
        }
    }
}

