package net.jerika.furmutage.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Tainted Red Rose Apple - hurts transfurred players with Shock and Poison 2 for 20 seconds.
 * Has no effect on untransfurred players.
 */
public class TaintedRedRoseAppleItem extends Item {
    
    public TaintedRedRoseAppleItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player && !level.isClientSide) {
            if (isPlayerTransfurred(player)) {
                // Transfurred players get negative effects
                int duration = 20 * 20; // 20 seconds
                player.addEffect(new MobEffectInstance(MobEffects.POISON, duration, 1)); // Poison 2 (amplifier 1 = level 2)
                
                // Apply shock effect from Changed mod
                try {
                    Class<?> changedEffectsClass = Class.forName("net.ltxprogrammer.changed.init.ChangedEffects");
                    java.lang.reflect.Field shockField = changedEffectsClass.getField("SHOCK");
                    Object shockEffect = shockField.get(null);
                    if (shockEffect instanceof net.minecraft.world.effect.MobEffect) {
                        player.addEffect(new MobEffectInstance((net.minecraft.world.effect.MobEffect) shockEffect, duration, 0));
                    }
                } catch (Exception e) {
                    // Changed mod effects not available, skip shock effect
                }
            }
        }
        
        return super.finishUsingItem(stack, level, entity);
    }
    
    /**
     * Check if a player is transfurred using the Changed mod API.
     */
    private boolean isPlayerTransfurred(Player player) {
        try {
            Class<?> instanceClass = Class.forName("net.ltxprogrammer.changed.entity.LatexVariantInstance");
            java.lang.reflect.Method getMethod = instanceClass.getMethod("get", Player.class);
            Object instance = getMethod.invoke(null, player);
            if (instance == null) {
                return false;
            }
            
            java.lang.reflect.Method getVariantMethod = instanceClass.getMethod("getLatexVariant");
            Object variant = getVariantMethod.invoke(instance);
            return variant != null;
        } catch (Throwable t) {
            return false;
        }
    }
}

