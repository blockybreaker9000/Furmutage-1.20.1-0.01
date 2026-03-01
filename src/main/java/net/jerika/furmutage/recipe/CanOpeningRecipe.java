package net.jerika.furmutage.recipe;

import net.jerika.furmutage.item.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapelessRecipe;

/**
 * Shapeless recipe: can opener + canned food → open canned food.
 * Returns the can opener with 2 durability consumed via getRemainingItems.
 */
public class CanOpeningRecipe extends ShapelessRecipe {
    private static final int DURABILITY_COST = 2;

    public CanOpeningRecipe(ResourceLocation id, String group, CraftingBookCategory category,
                            ItemStack result, NonNullList<Ingredient> ingredients) {
        super(id, group, category, result, ingredients);
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.is(ModItems.CAN_OPENER.get())) {
                ItemStack opener = stack.copy();
                int damage = opener.getDamageValue() + DURABILITY_COST;
                if (damage >= opener.getMaxDamage()) {
                    remaining.set(i, ItemStack.EMPTY);
                } else {
                    opener.setDamageValue(damage);
                    remaining.set(i, opener);
                }
                break;
            }
        }
        return remaining;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CAN_OPENING.get();
    }
}
