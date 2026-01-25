package net.jerika.furmutage.event;

import net.jerika.furmutage.config.ModCommonConfig;
import net.jerika.furmutage.furmutage;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Controls vanilla mob spawning based on config options.
 * Prevents zombies, skeletons, creepers, spiders, and pillagers from spawning
 * when their respective config options are disabled.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VanillaMobSpawnControlEvents {
    
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        // Only process on server side
        if (event.getLevel().isClientSide()) {
            return;
        }
        
        // Check if this is a blocked mob type
        boolean shouldRemove = false;
        
        // Check each mob type and remove if disabled
        if (event.getEntity() instanceof Zombie) {
            if (!ModCommonConfig.ENABLE_ZOMBIE_SPAWN.get()) {
                shouldRemove = true;
            }
        } else if (event.getEntity() instanceof Skeleton) {
            if (!ModCommonConfig.ENABLE_SKELETON_SPAWN.get()) {
                shouldRemove = true;
            }
        } else if (event.getEntity() instanceof Creeper) {
            if (!ModCommonConfig.ENABLE_CREEPER_SPAWN.get()) {
                shouldRemove = true;
            }
        } else if (event.getEntity() instanceof Spider) {
            if (!ModCommonConfig.ENABLE_SPIDER_SPAWN.get()) {
                shouldRemove = true;
            }
        } else {
            // Check for pillagers
            EntityType<?> entityType = event.getEntity().getType();
            if (entityType == EntityType.PILLAGER) {
                if (!ModCommonConfig.ENABLE_PILLAGER_SPAWN.get()) {
                    shouldRemove = true;
                }
            }
        }
        
        // Remove the entity if it's a blocked type
        // Only remove if it was just spawned (tickCount <= 1) to catch natural spawns
        if (shouldRemove && event.getEntity() instanceof net.minecraft.world.entity.LivingEntity livingEntity) {
            // Check if entity was just spawned (natural spawns have tickCount 0 or 1 when joining)
            // This will catch natural spawns, structure spawns, and patrol spawns
            // Spawn eggs and commands might also be caught, but that's acceptable
            if (livingEntity.tickCount <= 1) {
                event.setCanceled(true);
                livingEntity.discard();
            }
        }
    }
}
