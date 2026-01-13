package net.jerika.furmutage.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RoselightShears extends Item {
    
    public RoselightShears() {
        super(new Properties().durability(800));
    }
    
    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }
    
    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack result = itemStack.copy();
        result.setCount(1);
        return result;
    }
}
