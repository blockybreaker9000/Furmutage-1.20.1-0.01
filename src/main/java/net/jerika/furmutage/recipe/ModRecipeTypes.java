package net.jerika.furmutage.recipe;

import net.jerika.furmutage.furmutage;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, furmutage.MOD_ID);

    public static final RegistryObject<RecipeType<EugenicsSmeltingRecipe>> EUGENICS_SMELTING =
            RECIPE_TYPES.register("eugenics_smelting", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "eugenics_smelting";
                }
            });
}
