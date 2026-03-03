package net.jerika.furmutage.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.jerika.furmutage.block.custom.ModBlocks;
import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.recipe.EugenicsSmeltingRecipe;
import net.jerika.furmutage.recipe.ModRecipeTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * JEI integration for Furmutage:
 * - Shows Eugenics Smelting recipes in their own category.
 * - Marks the Eugenics Crafting Block as a catalyst for vanilla crafting recipes.
 * - Marks the Eugenics Smeltery Oven as a catalyst for Eugenics Smelting recipes.
 */
@JeiPlugin
public class FurmutageJeiPlugin implements IModPlugin {

    public static final RecipeType<EugenicsSmeltingRecipe> EUGENICS_SMELTING_TYPE =
            RecipeType.create(furmutage.MOD_ID, "eugenics_smelting", EugenicsSmeltingRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(furmutage.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new EugenicsSmeltingCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        var recipeManager = minecraft.level.getRecipeManager();
        var eugenicsSmeltingRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.EUGENICS_SMELTING.get());
        registration.addRecipes(EUGENICS_SMELTING_TYPE, eugenicsSmeltingRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        // Eugenics Crafting Block acts as an extra crafting table (vanilla crafting recipes)
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.EUGENICS_CRAFTING_BLOCK.get()),
                mezz.jei.api.constants.RecipeTypes.CRAFTING
        );

        // Eugenics Smeltery Oven is the catalyst for Eugenics Smelting recipes
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.EUGENICS_SMELTERY_OVEN.get()),
                EUGENICS_SMELTING_TYPE
        );
    }
}

