package net.jerika.furmutage.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Utility class to check if items can be crafted in normal crafting tables.
 * Changed and Furmutage items can only be crafted in the Eugenics Crafting Block.
 */
public class CraftingRestrictions {
    
    /**
     * Checks if an item is allowed to be crafted in normal crafting tables.
     * Returns false for Changed and Furmutage items.
     */
    public static boolean isAllowedItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return true; // Empty stacks are always allowed
        }
        
        return isAllowedItem(stack.getItem());
    }
    
    /**
     * Checks if an item is allowed to be crafted in normal crafting tables.
     * Returns false for Changed and Furmutage items.
     * Exception: The eugenics crafting block can be crafted in normal crafting tables.
     */
    public static boolean isAllowedItem(Item item) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(item);
        if (itemId == null) {
            return true; // If we can't identify the item, allow it (safer default)
        }
        
        String namespace = itemId.getNamespace();
        String path = itemId.getPath();
        
        // Exception: Eugenics crafting block can be crafted in normal crafting tables
        if (namespace.equals("furmutage") && path.equals("eugenics_crafting_block")) {
            return true;
        }
        
        // Changed mod items (namespace: "changed")
        if (namespace.equals("changed")) {
            return false;
        }
        
        // Furmutage mod items (namespace: "furmutage")
        if (namespace.equals("furmutage")) {
            return false;
        }
        
        // All other items are allowed
        return true;
    }
    
    /**
     * Checks if an item is a Changed or Furmutage item.
     */
    public static boolean isRestrictedItem(ItemStack stack) {
        return !isAllowedItem(stack);
    }
    
    /**
     * Checks if an item is a Changed or Furmutage item.
     */
    public static boolean isRestrictedItem(Item item) {
        return !isAllowedItem(item);
    }
}
