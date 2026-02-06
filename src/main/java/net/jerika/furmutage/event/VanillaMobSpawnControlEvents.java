package net.jerika.furmutage.event;

import net.jerika.furmutage.config.ModCommonConfig;
import net.jerika.furmutage.furmutage;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Controls vanilla mob spawning based on config options.
 * Prevents configured vanilla mobs from spawning when their config options are disabled.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VanillaMobSpawnControlEvents {
    
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        // Only process on server side
        if (event.getLevel().isClientSide()) {
            return;
        }
        
        EntityType<?> entityType = event.getEntity().getType();
        boolean shouldRemove = false;
        
        // Check each mob type by EntityType so subtypes (Husk, Stray) use their own config
        if (entityType == EntityType.ZOMBIE) {
            shouldRemove = !ModCommonConfig.ENABLE_ZOMBIE_SPAWN.get();
        } else if (entityType == EntityType.HUSK) {
            shouldRemove = !ModCommonConfig.ENABLE_HUSK_SPAWN.get();
        } else if (entityType == EntityType.SKELETON) {
            shouldRemove = !ModCommonConfig.ENABLE_SKELETON_SPAWN.get();
        } else if (entityType == EntityType.STRAY) {
            shouldRemove = !ModCommonConfig.ENABLE_STRAY_SPAWN.get();
        } else if (entityType == EntityType.CREEPER) {
            shouldRemove = !ModCommonConfig.ENABLE_CREEPER_SPAWN.get();
        } else if (entityType == EntityType.SPIDER) {
            shouldRemove = !ModCommonConfig.ENABLE_SPIDER_SPAWN.get();
        } else if (entityType == EntityType.PILLAGER) {
            shouldRemove = !ModCommonConfig.ENABLE_PILLAGER_SPAWN.get();
        } else if (entityType == EntityType.ENDERMAN) {
            shouldRemove = !ModCommonConfig.ENABLE_ENDERMAN_SPAWN.get();
        } else if (entityType == EntityType.ZOMBIFIED_PIGLIN) {
            shouldRemove = !ModCommonConfig.ENABLE_ZOMBIFIED_PIGLIN_SPAWN.get();
        } else if (entityType == EntityType.PIGLIN || entityType == EntityType.PIGLIN_BRUTE) {
            shouldRemove = !ModCommonConfig.ENABLE_PIGLIN_SPAWN.get();
        } else if (entityType == EntityType.VINDICATOR) {
            shouldRemove = !ModCommonConfig.ENABLE_VINDICATOR_SPAWN.get();
        } else if (entityType == EntityType.EVOKER) {
            shouldRemove = !ModCommonConfig.ENABLE_EVOKER_SPAWN.get();
        } else if (entityType == EntityType.RAVAGER) {
            shouldRemove = !ModCommonConfig.ENABLE_RAVAGER_SPAWN.get();
        } else if (entityType == EntityType.GUARDIAN) {
            shouldRemove = !ModCommonConfig.ENABLE_GUARDIAN_SPAWN.get();
        } else if (entityType == EntityType.ELDER_GUARDIAN) {
            shouldRemove = !ModCommonConfig.ENABLE_ELDER_GUARDIAN_SPAWN.get();
        } else if (entityType == EntityType.WITCH) {
            shouldRemove = !ModCommonConfig.ENABLE_WITCH_SPAWN.get();
        }
        
        if (shouldRemove && event.getEntity() instanceof net.minecraft.world.entity.LivingEntity livingEntity) {
            if (livingEntity.tickCount <= 1) {
                event.setCanceled(true);
                livingEntity.discard();
            }
        }
    }
}
