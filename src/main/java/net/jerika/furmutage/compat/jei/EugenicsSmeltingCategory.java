package net.jerika.furmutage.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.recipe.EugenicsSmeltingRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * JEI recipe category for Eugenics Smelting (furmutage:eugenics_smelting).
 * Layout mirrors the vanilla blast furnace: input, fuel, output.
 */
public class EugenicsSmeltingCategory implements IRecipeCategory<EugenicsSmeltingRecipe> {

    private static final ResourceLocation BLAST_FURNACE_TEXTURE =
            new ResourceLocation("minecraft", "textures/gui/container/blast_furnace.png");

    // Use a cropped area that shows just the furnace portion (no player inventory)
    private final IDrawable background;
    private final IDrawable icon;

    public EugenicsSmeltingCategory(IGuiHelper helper) {
        // x,y,width,height: top-left 0,0 area 176x85 from blast furnace texture
        this.background = helper.createDrawable(BLAST_FURNACE_TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(
                VanillaTypes.ITEM_STACK,
                new ItemStack(net.jerika.furmutage.block.custom.ModBlocks.EUGENICS_SMELTERY_OVEN.get())
        );
    }

    @Override
    public RecipeType<EugenicsSmeltingRecipe> getRecipeType() {
        return FurmutageJeiPlugin.EUGENICS_SMELTING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.furmutage.eugenics_smeltery");
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
    public void setRecipe(IRecipeLayoutBuilder builder, EugenicsSmeltingRecipe recipe, IFocusGroup focuses) {
        // Input slot (same as smeltery GUI input)
        builder.addInputSlot(56, 17)
                .addIngredients(recipe.getIngredients().get(0));

        // Fuel slot (same as smeltery GUI fuel position; JEI doesn't care what's in it)
        builder.addInputSlot(56, 53);

        // Output slot
        builder.addOutputSlot(116, 35)
                .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(EugenicsSmeltingRecipe recipe, IRecipeSlotsView recipeSlotsView,
                     GuiGraphics guiGraphics, double mouseX, double mouseY) {
        // No extra overlays; the static blast furnace background is enough.
    }
}

