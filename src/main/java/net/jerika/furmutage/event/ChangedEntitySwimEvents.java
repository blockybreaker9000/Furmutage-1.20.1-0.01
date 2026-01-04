package net.jerika.furmutage.event;

import net.jerika.furmutage.ai.ChangedEntityImprovedPathfindingGoal;
import net.jerika.furmutage.furmutage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.UUID;
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
    
    // Track Changed entities for sprint speed reduction
    private static final Set<LivingEntity> changedEntities = java.util.Collections.newSetFromMap(new WeakHashMap<>());
    
    // Track entities that currently have the sprint speed reduction modifier applied
    private static final Set<LivingEntity> entitiesWithSprintReduction = java.util.Collections.newSetFromMap(new WeakHashMap<>());
    
    // UUID for the sprint speed reduction modifier
    private static final UUID SPRINT_SPEED_REDUCTION_UUID = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
    
    // Sprint speed reduction multiplier (0.7 = 70% of normal sprint speed, so 30% reduction)
    private static final double SPRINT_SPEED_REDUCTION = -0.3D; // Negative value reduces speed
    
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
    
    // Entities to exclude from follow range modifications (roomba)
    private static final Set<String> EXCLUDED_FOLLOW_RANGE_ENTITIES = Set.of(
            "changed:roomba"
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
                changedEntities.add(livingEntity);
                
                // Add improved pathfinding goal for Changed entities
                if (livingEntity instanceof PathfinderMob pathfinderMob && !improvedPathfindingEntities.contains(pathfinderMob)) {
                    // Add improved pathfinding goal with high priority (before most other goals)
                    pathfinderMob.goalSelector.addGoal(2, new ChangedEntityImprovedPathfindingGoal(pathfinderMob));
                    improvedPathfindingEntities.add(pathfinderMob);
                }
            }
            
            // Set follow range to 80 blocks for Changed entities (except roomba)
            if (entityId.startsWith("changed:") && !EXCLUDED_FOLLOW_RANGE_ENTITIES.contains(entityId)) {
                AttributeInstance followRange = livingEntity.getAttribute(Attributes.FOLLOW_RANGE);
                if (followRange != null) {
                    // Set the base value to 80 blocks
                    followRange.setBaseValue(80.0D);
                }
            }
            // Note: X-ray vision is handled by EntitySensingXRayMixin
        }
    }
    
    /**
     * Check if an entity should have fast swimming enabled.
     * This is called by the mixin to determine if travel() should be modified.
     */
    public static boolean shouldHaveFastSwimming(LivingEntity entity) {
        return fastSwimEntities.contains(entity);
    }
    
    /**
     * Reduces sprint speed for Changed entities by applying a modifier when they're sprinting.
     */
    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        
        // Only process on server side
        if (entity.level().isClientSide()) {
            return;
        }
        
        // Only process Changed entities
        if (!changedEntities.contains(entity)) {
            return;
        }
        
        // Get movement speed attribute
        AttributeInstance movementSpeed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed == null) {
            return;
        }
        
        // Check if entity is sprinting
        boolean isSprinting = entity.isSprinting();
        
        // Check if modifier is already applied
        boolean hasModifier = entitiesWithSprintReduction.contains(entity);
        
        if (isSprinting && !hasModifier) {
            // Apply sprint speed reduction modifier (multiplicative, reduces by 30%)
            AttributeModifier sprintReduction = new AttributeModifier(
                SPRINT_SPEED_REDUCTION_UUID,
                "Changed Entity Sprint Speed Reduction",
                SPRINT_SPEED_REDUCTION,
                AttributeModifier.Operation.MULTIPLY_TOTAL
            );
            movementSpeed.addTransientModifier(sprintReduction);
            entitiesWithSprintReduction.add(entity);
        } else if (!isSprinting && hasModifier) {
            // Remove sprint speed reduction modifier when not sprinting
            movementSpeed.removeModifier(SPRINT_SPEED_REDUCTION_UUID);
            entitiesWithSprintReduction.remove(entity);
        }
    }
}

