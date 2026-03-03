package net.jerika.furmutage.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.jerika.furmutage.furmutage;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, furmutage.MOD_ID);

    public static final RegistryObject<RecipeSerializer<CanOpeningRecipe>> CAN_OPENING =
            SERIALIZERS.register("can_opening", () -> new RecipeSerializer<CanOpeningRecipe>() {
                @Override
                public CanOpeningRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
                    String group = GsonHelper.getAsString(json, "group", "");
                    CraftingBookCategory category = CraftingBookCategory.CODEC.byName(
                            GsonHelper.getAsString(json, "category", CraftingBookCategory.MISC.getSerializedName()), CraftingBookCategory.MISC);
                    NonNullList<Ingredient> ingredients = NonNullList.create();
                    for (JsonElement element : GsonHelper.getAsJsonArray(json, "ingredients")) {
                        ingredients.add(Ingredient.fromJson(element));
                    }
                    ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
                    return new CanOpeningRecipe(recipeId, group, category, result, ingredients);
                }

                @Override
                public CanOpeningRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
                    String group = buffer.readUtf();
                    CraftingBookCategory category = buffer.readEnum(CraftingBookCategory.class);
                    int size = buffer.readVarInt();
                    NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
                    for (int i = 0; i < size; i++) {
                        ingredients.set(i, Ingredient.fromNetwork(buffer));
                    }
                    ItemStack result = buffer.readItem();
                    return new CanOpeningRecipe(recipeId, group, category, result, ingredients);
                }

                @Override
                public void toNetwork(FriendlyByteBuf buffer, CanOpeningRecipe recipe) {
                    buffer.writeUtf(recipe.getGroup());
                    buffer.writeEnum(recipe.category());
                    buffer.writeVarInt(recipe.getIngredients().size());
                    for (Ingredient ing : recipe.getIngredients()) {
                        ing.toNetwork(buffer);
                    }
                    buffer.writeItem(recipe.getResultItem(RegistryAccess.EMPTY));
                }
            });

    public static final RegistryObject<RecipeSerializer<EugenicsCraftingRecipe>> EUGENICS_CRAFTING =
            SERIALIZERS.register("eugenics_crafting", () -> new RecipeSerializer<EugenicsCraftingRecipe>() {
                @Override
                public EugenicsCraftingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
                    String group = GsonHelper.getAsString(json, "group", "");
                    CraftingBookCategory category = CraftingBookCategory.CODEC.byName(
                            GsonHelper.getAsString(json, "category", CraftingBookCategory.MISC.getSerializedName()), CraftingBookCategory.MISC);
                    JsonArray patternArray = GsonHelper.getAsJsonArray(json, "pattern");
                    String[] pattern = new String[patternArray.size()];
                    int width = 0;
                    for (int i = 0; i < patternArray.size(); i++) {
                        pattern[i] = GsonHelper.convertToString(patternArray.get(i), "pattern[" + i + "]");
                        width = Math.max(width, pattern[i].length());
                    }
                    int height = pattern.length;
                    JsonObject keyObj = GsonHelper.getAsJsonObject(json, "key");
                    Map<String, Ingredient> key = new HashMap<>();
                    for (Map.Entry<String, JsonElement> entry : keyObj.entrySet()) {
                        if (entry.getKey().length() == 1) {
                            key.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
                        }
                    }
                    NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);
                    for (int row = 0; row < height; row++) {
                        String rowStr = pattern[row];
                        for (int col = 0; col < width; col++) {
                            char c = col < rowStr.length() ? rowStr.charAt(col) : ' ';
                            Ingredient ing = (c == ' ') ? Ingredient.EMPTY : key.getOrDefault(String.valueOf(c), Ingredient.EMPTY);
                            ingredients.set(col + row * width, ing);
                        }
                    }
                    ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
                    return new EugenicsCraftingRecipe(recipeId, group, category, width, height, ingredients, result);
                }

                @Override
                public EugenicsCraftingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
                    int width = buffer.readVarInt();
                    int height = buffer.readVarInt();
                    String group = buffer.readUtf();
                    CraftingBookCategory category = buffer.readEnum(CraftingBookCategory.class);
                    NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);
                    for (int i = 0; i < ingredients.size(); i++) {
                        ingredients.set(i, Ingredient.fromNetwork(buffer));
                    }
                    ItemStack result = buffer.readItem();
                    return new EugenicsCraftingRecipe(recipeId, group, category, width, height, ingredients, result);
                }

                @Override
                public void toNetwork(FriendlyByteBuf buffer, EugenicsCraftingRecipe recipe) {
                    buffer.writeVarInt(recipe.getRecipeWidth());
                    buffer.writeVarInt(recipe.getRecipeHeight());
                    buffer.writeUtf(recipe.getGroup());
                    buffer.writeEnum(recipe.category());
                    for (Ingredient ing : recipe.getIngredients()) {
                        ing.toNetwork(buffer);
                    }
                    buffer.writeItem(recipe.getResultItem(RegistryAccess.EMPTY));
                }
            });

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
                    buffer.writeEnum(CookingBookCategory.class.cast(recipe.category()));
                    recipe.getIngredients().get(0).toNetwork(buffer);
                    buffer.writeItem(recipe.getResultItem(null));
                    buffer.writeFloat(recipe.getExperience());
                    buffer.writeVarInt(recipe.getCookingTime());
                }
            });
}

