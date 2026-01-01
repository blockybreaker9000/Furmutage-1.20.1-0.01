package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler that gives Changed mod entities 10 blocks of fall damage resistance.
 * Entities can fall up to 10 blocks without taking damage, and will take reduced damage
 * if they fall more than 10 blocks.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChangedEntityFallDamageEvents {
    
    private static final float FALL_DAMAGE_RESISTANCE_BLOCKS = 10.0f; // 10 blocks of free fall
    
    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        
        // Only process on server side
        if (entity == null || entity.level().isClientSide) {
            return;
        }
        
        // Only apply to Changed mod entities
        if (!isChangedEntity(entity)) {
            return;
        }
        
        float distance = event.getDistance();
        
        // If the fall distance is 10 blocks or less, cancel all fall damage
        if (distance <= FALL_DAMAGE_RESISTANCE_BLOCKS) {
            event.setCanceled(true);
            furmutage.LOGGER.debug("Cancelled fall damage for {} (fell {} blocks, within {} block resistance)", 
                entity.getName().getString(), distance, FALL_DAMAGE_RESISTANCE_BLOCKS);
        } else {
            // If the fall distance is more than 10 blocks, reduce the damage by 10 blocks
            float reducedDistance = distance - FALL_DAMAGE_RESISTANCE_BLOCKS;
            event.setDistance(reducedDistance);
            furmutage.LOGGER.debug("Reduced fall damage for {} (fell {} blocks, reduced to {} blocks)", 
                entity.getName().getString(), distance, reducedDistance);
        }
    }
    
    /**
     * Checks if the entity is from the Changed mod.
     */
    private static boolean isChangedEntity(LivingEntity entity) {
        if (entity == null) {
            return false;
        }
        
        // Check if the entity's class is in the Changed mod package
        String className = entity.getClass().getName();
        return className.startsWith("net.ltxprogrammer.changed.entity");
    }
}

