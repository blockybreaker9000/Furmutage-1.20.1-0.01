package net.jerika.furmutage.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CanOpenerItem extends Item {
    public static final int MAX_DURABILITY = 200;

    public CanOpenerItem(Properties properties) {
        super(properties.durability(MAX_DURABILITY));
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return MAX_DURABILITY;
    }
}
