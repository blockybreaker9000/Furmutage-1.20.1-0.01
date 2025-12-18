package net.jerika.furmutage.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

/**
 * Orange Smoothy behaves like a drinkable food item.
 * It uses the drink animation and drink sound, but does not return a bottle.
 */
public class OrangeSmoothy extends Item {

    public OrangeSmoothy(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        // Use the generic drink sound (similar to water/milk). Change to HONEY_DRINK if desired.
        return SoundEvents.GENERIC_DRINK;
    }
}


