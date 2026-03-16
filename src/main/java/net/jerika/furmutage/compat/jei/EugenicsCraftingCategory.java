package net.jerika.furmutage.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.jerika.furmutage.block.custom.ModBlocks;
import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.recipe.EugenicsCraftingRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * JEI recipe category for Eugenics Crafting (furmutage:eugenics_crafting).
 * Layout mirrors the vanilla crafting table: 3x3 grid input, one output.
 */
public class EugenicsCraftingCategory implements IRecipeCategory<EugenicsCraftingRecipe> {

    private static final ResourceLocation CRAFTING_TEXTURE =
            new ResourceLocation("minecraft", "textures/gui/container/crafting_table.png");

    private final IDrawable background;
    private final IDrawable icon;

    public EugenicsCraftingCategory(IGuiHelper helper) {
        // Use the top 176x83 area (crafting grid + result, no player inventory)
        this.background = helper.createDrawable(CRAFTING_TEXTURE, 0, 0, 176, 83);
        this.icon = helper.createDrawableIngredient(
                VanillaTypes.ITEM_STACK,
                new ItemStack(ModBlocks.EUGENICS_CRAFTING_BLOCK.get())
        );
    }

    @Override
    public RecipeType<EugenicsCraftingRecipe> getRecipeType() {
        return FurmutageJeiPlugin.EUGENICS_CRAFTING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.furmutage.eugenics_crafting");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, EugenicsCraftingRecipe recipe, IFocusGroup focuses) {
        // Inputs: 3x3 grid centered like vanilla crafting table
        // Vanilla crafting_table GUI input origin is roughly at (30, 17)
        int originX = 30;
        int originY = 17;

        int width = recipe.getRecipeWidth();
        int height = recipe.getRecipeHeight();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int idx = col + row * width;
                if (idx < recipe.getIngredients().size()) {
                    Ingredient ing = recipe.getIngredients().get(idx);
                    if (!ing.isEmpty()) {
                        int x = originX + col * 18;
                        int y = originY + row * 18;
                        builder.addInputSlot(x, y)
                                .addIngredients(ing);
                    }
                }
            }
        }

        // Output: same position as vanilla crafting result slot (124, 35)
        builder.addOutputSlot(124, 35)
                .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(EugenicsCraftingRecipe recipe, IRecipeSlotsView recipeSlotsView,
                     GuiGraphics guiGraphics, double mouseX, double mouseY) {
        // No extra overlays; the static crafting table background is enough.
    }
}

