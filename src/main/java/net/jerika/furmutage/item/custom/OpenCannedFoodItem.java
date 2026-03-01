package net.jerika.furmutage.item.custom;

import net.jerika.furmutage.item.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

/**
 * Consumable open canned food. Uses drink animation and honey drink sound (like orange juice),
 * so no food-eating particles. Gives one empty can when finished (if not creative).
 */
public class OpenCannedFoodItem extends Item {

    public OpenCannedFoodItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            ItemStack emptyCan = new ItemStack(ModItems.EMPTY_CAN.get());
            if (!player.getInventory().add(emptyCan)) {
                player.drop(emptyCan, false);
            }
        }
        return result;
    }
}
