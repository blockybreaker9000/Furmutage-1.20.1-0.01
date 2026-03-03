package net.jerika.furmutage.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

/**
 * Shaped recipe that only works in the Eugenics Crafting Block.
 * Use "type": "furmutage:eugenics_crafting" in JSON with standard pattern/key/result.
 */
public class EugenicsCraftingRecipe extends ShapedRecipe {

    public EugenicsCraftingRecipe(ResourceLocation id, String group, CraftingBookCategory category,
                                  int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
        super(id, group, category, width, height, ingredients, result);
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.EUGENICS_CRAFTING.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.EUGENICS_CRAFTING.get();
    }
}
