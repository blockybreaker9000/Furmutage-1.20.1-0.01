package net.jerika.furmutage.event;

import net.jerika.furmutage.ai.ChangedEntityImprovedPathfindingGoal;
import net.jerika.furmutage.furmutage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.WeakHashMap;

/**
 * Adds swim animation and fast swim speed to Changed mod entities,
 * excluding aquatic entities that already have their own swimming behavior.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChangedEntitySwimEvents {
    // Track entities that should have fast swimming
    private static final Set<LivingEntity> fastSwimEntities = java.util.Collections.newSetFromMap(new WeakHashMap<>());
    
    // Track entities that have been given improved pathfinding
    private static final Set<PathfinderMob> improvedPathfindingEntities = java.util.Collections.newSetFromMap(new WeakHashMap<>());
    
    // Entities to exclude from swim speed/animation modifications
    private static final Set<String> EXCLUDED_ENTITIES = Set.of(
            "changed:latex_shark",
            "changed:latex_shark_feral",
            "changed:latex_shark_female",
            "changed:latex_shark_male",
            "changed:latex_siren",
            "changed:latex_manta_ray_female",
            "changed:latex_manta_ray_male",
            "changed:latex_mermaid_shark",
            "changed:latex_squid_dog_female",
            "changed:latex_squid_dog_male",
            "changed:latex_tiger_shark",
            "changed:latex_orca",
            "changed:latex_eel"
    );
    
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        // Only process on server side
        if (event.getLevel().isClientSide()) {
            return;
        }
        
        // Check if entity is a LivingEntity from Changed mod
        if (event.getEntity() instanceof LivingEntity livingEntity) {
            // Get entity type ID
            ResourceLocation entityTypeId = ForgeRegistries.ENTITY_TYPES.getKey(livingEntity.getType());
            if (entityTypeId == null) {
                return;
            }
            
            String entityId = entityTypeId.toString();
            
            // Check if it's a Changed mod entity and not excluded
            if (entityId.startsWith("changed:") && !EXCLUDED_ENTITIES.contains(entityId)) {
                fastSwimEntities.add(livingEntity);
                
                // Add improved pathfinding goal for Changed entities
                if (livingEntity instanceof PathfinderMob pathfinderMob && !improvedPathfindingEntities.contains(pathfinderMob)) {
                    // Add improved pathfinding goal with high priority (before most other goals)
                    pathfinderMob.goalSelector.addGoal(2, new ChangedEntityImprovedPathfindingGoal(pathfinderMob));
                    improvedPathfindingEntities.add(pathfinderMob);
                }
            }
        }
    }
    
    /**
     * Check if an entity should have fast swimming enabled.
     * This is called by the mixin to determine if travel() should be modified.
     */
    public static boolean shouldHaveFastSwimming(LivingEntity entity) {
        return fastSwimEntities.contains(entity);
    }
}

