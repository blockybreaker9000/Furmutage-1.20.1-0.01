package net.jerika.furmutage.item;

import net.jerika.furmutage.furmutage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class TscBrandedWaterBottleItem extends Item {

    public TscBrandedWaterBottleItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        Player player = entity instanceof Player p ? p : null;
        if (player != null && !player.getAbilities().instabuild) {
            stack.shrink(1);
            Item empty = ForgeRegistries.ITEMS.getValue(new ResourceLocation(furmutage.MOD_ID, "empty_tsc_branded_water_bottle"));
            if (empty != null && empty != net.minecraft.world.item.Items.AIR) {
                if (!player.getInventory().add(new ItemStack(empty))) {
                    player.drop(new ItemStack(empty), false);
                }
            }
        }
        return stack;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }
}
