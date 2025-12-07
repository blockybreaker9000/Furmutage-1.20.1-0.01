package net.jerika.furmutage.event;

import net.jerika.furmutage.entity.custom.MuglingEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
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
                    // Add goal to target Muggings with priority 2
                    // The goal will only activate if the entity can see Muggings (mustSeeTarget = true)
                    pathfinderMob.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(
                            pathfinderMob, 
                            MuglingEntity.class, 
                            true,  // mustSeeTarget - only attack if they can see the Mugling
                            false  // mustReachTarget - don't require pathfinding to target
                    ));
                    processedEntities.add(livingEntity);
                }
            }
        }
    }
}

