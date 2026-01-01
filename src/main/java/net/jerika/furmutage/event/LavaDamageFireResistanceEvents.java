package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Event handler that grants fire resistance to entities after they take lava damage twice.
 * Fire resistance lasts for 30 seconds (600 ticks).
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LavaDamageFireResistanceEvents {
    
    // Track how many times each entity has taken lava damage
    private static final Map<UUID, Integer> lavaDamageCount = new HashMap<>();
    private static final int LAVA_DAMAGE_THRESHOLD = 2; // Number of lava damage hits needed
    private static final int FIRE_RESISTANCE_DURATION = 600; // 30 seconds in ticks (30 * 20)
    
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        
        // Only apply to living entities on server side
        if (entity == null || entity.level().isClientSide) {
            return;
        }
        
        // Exclude players and vanilla mobs - only apply to furmutage entities
        if (entity instanceof net.minecraft.world.entity.player.Player) {
            return; // Don't give players this ability
        }
        
        // Only apply to furmutage mod entities
        if (!isFurmutageEntity(entity)) {
            return; // Don't give vanilla mobs this ability
        }
        
        // Check if the damage source is lava
        if (isLavaDamage(event.getSource(), entity)) {
            UUID entityId = entity.getUUID();
            
            // Get current lava damage count for this entity
            int currentCount = lavaDamageCount.getOrDefault(entityId, 0);
            currentCount++;
            
            // If entity has reached the threshold, grant fire resistance
            if (currentCount >= LAVA_DAMAGE_THRESHOLD) {
                // Apply fire resistance for 30 seconds
                MobEffectInstance fireResistance = new MobEffectInstance(
                    MobEffects.FIRE_RESISTANCE,
                    FIRE_RESISTANCE_DURATION,
                    0, // Amplifier level 0
                    false, // Not ambient
                    true, // Show particles
                    true // Show icon
                );
                entity.addEffect(fireResistance);
                
                // Reset the counter after granting fire resistance
                lavaDamageCount.remove(entityId);
                
                furmutage.LOGGER.debug("Granted fire resistance to {} after {} lava damage hits", 
                    entity.getName().getString(), LAVA_DAMAGE_THRESHOLD);
            } else {
                // Store the updated count
                lavaDamageCount.put(entityId, currentCount);
            }
        }
    }
    
    /**
     * Checks if the entity is from the furmutage mod.
     */
    private static boolean isFurmutageEntity(LivingEntity entity) {
        if (entity == null) {
            return false;
        }
        
        // Check if the entity's class is in the furmutage package
        String className = entity.getClass().getName();
        return className.startsWith("net.jerika.furmutage.entity");
    }
    
    /**
     * Checks if the damage source is from lava.
     */
    private static boolean isLavaDamage(net.minecraft.world.damagesource.DamageSource source, LivingEntity entity) {
        if (source == null) {
            return false;
        }
        
        // Check for lava damage source using the entity's level
        if (entity.level().damageSources().lava().equals(source)) {
            return true;
        }
        
        // Check if the damage source message ID contains "lava"
        String msgId = source.getMsgId();
        if (msgId != null && msgId.contains("lava")) {
            return true;
        }
        
        // Check if the entity is in lava
        if (entity.isInLava()) {
            return true;
        }
        
        // Check if the damage source entity is in lava
        if (source.getEntity() instanceof LivingEntity livingEntity) {
            return livingEntity.isInLava();
        }
        
        return false;
    }
    
    /**
     * Cleans up the tracking map when an entity leaves the level.
     * This prevents memory leaks from entities that are no longer in the world.
     */
    @SubscribeEvent
    public static void onEntityLeaveLevel(EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            UUID entityId = event.getEntity().getUUID();
            lavaDamageCount.remove(entityId);
        }
    }
}

