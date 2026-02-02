package net.jerika.furmutage.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.jerika.furmutage.furmutage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, furmutage.MOD_ID);

    public static final RegistryObject<RecipeSerializer<EugenicsSmeltingRecipe>> EUGENICS_SMELTING =
            SERIALIZERS.register("eugenics_smelting", () -> new RecipeSerializer<EugenicsSmeltingRecipe>() {

                @Override
                public EugenicsSmeltingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
                    String group = GsonHelper.getAsString(json, "group", "");
                    CookingBookCategory category = CookingBookCategory.CODEC.byName(
                            GsonHelper.getAsString(json, "category", null), CookingBookCategory.MISC);
                    
                    JsonElement ingredientElement = GsonHelper.isArrayNode(json, "ingredient") 
                            ? GsonHelper.getAsJsonArray(json, "ingredient") 
                            : GsonHelper.getAsJsonObject(json, "ingredient");
                    Ingredient ingredient = Ingredient.fromJson(ingredientElement);
                    
                    String resultStr = GsonHelper.getAsString(json, "result");
                    ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(resultStr)));
                    
                    float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
                    int cookingTime = GsonHelper.getAsInt(json, "cookingtime", 200);
                    
                    return new EugenicsSmeltingRecipe(recipeId, group, category, ingredient, result, experience, cookingTime);
                }

                @Override
                public EugenicsSmeltingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
                    String group = buffer.readUtf();
                    CookingBookCategory category = buffer.readEnum(CookingBookCategory.class);
                    Ingredient ingredient = Ingredient.fromNetwork(buffer);
                    ItemStack result = buffer.readItem();
                    float experience = buffer.readFloat();
                    int cookingTime = buffer.readVarInt();
                    return new EugenicsSmeltingRecipe(recipeId, group, category, ingredient, result, experience, cookingTime);
                }

                @Override
                public void toNetwork(FriendlyByteBuf buffer, EugenicsSmeltingRecipe recipe) {
                    buffer.writeUtf(recipe.getGroup());
                    buffer.writeEnum(recipe.category());
                    recipe.getIngredients().get(0).toNetwork(buffer);
                    buffer.writeItem(recipe.getResultItem(null));
                    buffer.writeFloat(recipe.getExperience());
                    buffer.writeVarInt(recipe.getCookingTime());
                }
            });
}

