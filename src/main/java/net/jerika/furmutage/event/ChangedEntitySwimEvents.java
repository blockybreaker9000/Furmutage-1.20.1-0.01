package net.jerika.furmutage.event;

import net.jerika.furmutage.ai.latex_beast_ai.ChangedEntityImprovedPathfindingGoal;
import net.jerika.furmutage.furmutage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

/**
 * Registers Changed/Furmutage entities for improved pathfinding and follow range.
 * When in water, gives Dolphin's Grace II for 200 ticks (replaces previous fast water movement).
 * Excludes aquatic entities that already have their own swimming behavior.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChangedEntitySwimEvents {
    /** Duration (ticks) for Dolphin's Grace when entity touches water. 200 ticks = 10 seconds. */
    private static final int DOLPHINS_GRACE_WATER_TICKS = 5000;

    // Track entities that get Dolphin's Grace in water (formerly fast swim)
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
            "changed:latex_eel",
            "furmutage:white_latex_dolphin",
            "furmutage:white_latex_squid",
            "furmutage:dark_latex_dolphin",
            "furmutage:dark_latex_squid",
            "furmutage:latex_exo_mutant",
            "furmutage:latex_mutant_family",
            "furmutage:latex_tenticle_limbs_mutant",
            "furmutage:withered_latex_pudding"
    );
    
    // Entities to exclude from follow range modifications
    private static final Set<String> EXCLUDED_FOLLOW_RANGE_ENTITIES = Set.of(
            "changed:roomba",
            "changed:exoskeleton"
    );
    
    /** Clear static entity sets when a level unloads to avoid infinite "Saving world data" hang. */
    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        fastSwimEntities.clear();
        improvedPathfindingEntities.clear();

    }

    /** Clear on server stop so entities can be released before save. */
    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        fastSwimEntities.clear();
        improvedPathfindingEntities.clear();
    }

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
            
            // Check if it's a Furmutage mod entity and not excluded
            if (entityId.startsWith("furmutage:") && !EXCLUDED_ENTITIES.contains(entityId)) {
                fastSwimEntities.add(livingEntity);
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
     * Applies Dolphin's Grace II in water
     * for Changed/Furmutage entities (same set as fastSwimEntities).
     */
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        // Dolphin's Grace II for 5000 ticks when any Changed/Furmutage entity is in water
        for (LivingEntity entity : new java.util.HashSet<>(fastSwimEntities)) {
            if (entity == null || !entity.isAlive()) {
                fastSwimEntities.remove(entity);
                continue;
            }
            if (entity.isInWater()) {
                entity.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, DOLPHINS_GRACE_WATER_TICKS, 3, false, true, true));
            }
        }
        }
    }

