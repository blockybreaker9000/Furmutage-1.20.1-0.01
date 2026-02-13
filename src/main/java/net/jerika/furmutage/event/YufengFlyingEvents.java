package net.jerika.furmutage.event;

import net.jerika.furmutage.ai.latex_beast_ai.YufengFlyToTargetGoal;
import net.jerika.furmutage.furmutage;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class YufengFlyingEvents {
    // Track entities we've already modified to avoid duplicate goals
    private static final Set<Mob> processedEntities = java.util.Collections.newSetFromMap(new WeakHashMap<>());
    
    private static final String YUFENG_ENTITY_1 = "changed:dark_latex_yufeng";
    private static final String YUFENG_ENTITY_2 = "changed:dark_latex_double_yufeng";
    
    // Dragon entities from Changed mod
    private static final String[] DRAGON_ENTITIES = {
        "changed:dark_latex_dragon",
        "changed:dark_latex_dragon_head",
        "changed:dark_latex_dragon_part",
        "changed:dark_latex_red_dragon",
        "changed:dark_latex_red_dragon_head",
        "changed:dark_latex_red_dragon_part",
        "changed:white_latex_dragon",
        "changed:white_latex_dragon_head",
        "changed:white_latex_dragon_part"
    };
    
    /** Clear static set when a level unloads to avoid hang during save. */
    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        processedEntities.clear();
    }

    /** Clear on server stop so entities can be released before save. */
    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        processedEntities.clear();
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        // Only process on server side
        if (event.getLevel().isClientSide()) {
            return;
        }
        
        // Check if entity is a Mob
        if (event.getEntity() instanceof Mob mob) {
            String entityType = ForgeRegistries.ENTITY_TYPES.getKey(mob.getType()).toString();
            
            // Check if it's a Yufeng entity or a dragon entity
            boolean isYufeng = entityType.equals(YUFENG_ENTITY_1) || entityType.equals(YUFENG_ENTITY_2);
            boolean isDragon = false;
            
            // Check if it's a dragon entity
            for (String dragonEntity : DRAGON_ENTITIES) {
                if (entityType.equals(dragonEntity)) {
                    isDragon = true;
                    break;
                }
            }
            
            // Also check if entity name contains "dragon" (case-insensitive) as a fallback
            if (!isDragon && entityType.toLowerCase().contains("dragon")) {
                isDragon = true;
            }
            
            if (isYufeng || isDragon) {
                // Skip if we've already processed this entity
                if (processedEntities.contains(mob)) {
                    return;
                }
                
                // Add flying goal with high priority (should execute before regular movement)
                mob.goalSelector.addGoal(1, new YufengFlyToTargetGoal(mob));
                
                processedEntities.add(mob);
                furmutage.LOGGER.debug("[YufengFlyingEvents] Added flying goal to {}", entityType);
            }
        }
    }
}

