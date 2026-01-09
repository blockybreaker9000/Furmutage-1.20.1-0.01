package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler that prevents Changed mod entities and Furmutage entities from taking suffocation damage.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChangedEntitySuffocationEvents {
    
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        
        // Only process on server side
        if (entity == null || entity.level().isClientSide) {
            return;
        }
        
        // Check if this is suffocation damage
        if (!isSuffocationDamage(source, entity)) {
            return;
        }
        
        // Apply to Changed mod entities or Furmutage entities
        if (isChangedEntity(entity) || isFurmutageEntity(entity)) {
            event.setCanceled(true);
        }
    }
    
    /**
     * Checks if the damage source is suffocation damage.
     */
    private static boolean isSuffocationDamage(DamageSource source, LivingEntity entity) {
        if (source == null) {
            return false;
        }
        
        // Check the damage source message ID for suffocation indicators
        String msgId = source.getMsgId();
        if (msgId != null) {
            // Check for suffocation-related damage source IDs
            if (msgId.equals("inWall") || 
                msgId.contains("suffocation") || 
                msgId.contains("in_wall") ||
                msgId.contains("suffocate")) {
                return true;
            }
        }
        
        // Check if the damage source is from in-wall suffocation
        // In Minecraft 1.20.1, suffocation damage is typically from level.damageSources().inWall()
        try {
            // Try to compare with a reference inWall damage source
            if (entity != null && entity.level() != null) {
                DamageSource inWallSource = entity.level().damageSources().inWall();
                if (source.typeHolder().equals(inWallSource.typeHolder())) {
                    return true;
                }
            }
        } catch (Exception e) {
            // If comparison fails, just rely on msgId check above
        }
        
        // Also check by comparing damage type registry key
        try {
            if (entity != null && entity.level() != null) {
                net.minecraft.core.Registry<net.minecraft.world.damagesource.DamageType> registry = entity.level().registryAccess().registry(net.minecraft.core.registries.Registries.DAMAGE_TYPE).orElse(null);
                if (registry != null) {
                    net.minecraft.resources.ResourceKey<net.minecraft.world.damagesource.DamageType> inWallKey = net.minecraft.resources.ResourceKey.create(
                        net.minecraft.core.registries.Registries.DAMAGE_TYPE,
                        new net.minecraft.resources.ResourceLocation("minecraft", "in_wall")
                    );
                    if (source.typeHolder().is(inWallKey)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // If registry check fails, ignore
        }
        
        return false;
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
        if (className.startsWith("net.ltxprogrammer.changed.entity")) {
            return true;
        }
        
        // Also check by entity registry name
        try {
            String entityType = net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();
            return entityType != null && entityType.startsWith("changed:");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Checks if the entity is a Furmutage entity.
     */
    private static boolean isFurmutageEntity(LivingEntity entity) {
        if (entity == null) {
            return false;
        }
        
        // Check if the entity's class is in the Furmutage entity package
        String className = entity.getClass().getName();
        if (className.startsWith("net.jerika.furmutage.entity.custom")) {
            return true;
        }
        
        // Also check by entity registry name
        try {
            String entityType = net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();
            return entityType != null && entityType.startsWith("furmutage:");
        } catch (Exception e) {
            return false;
        }
    }
}

