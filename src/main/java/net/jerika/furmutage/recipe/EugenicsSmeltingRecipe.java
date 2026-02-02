package net.jerika.furmutage.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class EugenicsSmeltingRecipe extends AbstractCookingRecipe {
    public EugenicsSmeltingRecipe(ResourceLocation id, String group, CookingBookCategory category,
                                  Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(ModRecipeTypes.EUGENICS_SMELTING.get(), id, group, category, ingredient, result, experience, cookingTime);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.EUGENICS_SMELTING.get();
    }
}
