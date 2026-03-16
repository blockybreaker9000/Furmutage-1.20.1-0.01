package net.jerika.furmutage.menu.client;

import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.menu.EugenicsCraftingMenu;
import net.jerika.furmutage.recipe.EugenicsCraftingRecipe;
import net.jerika.furmutage.recipe.ModRecipeTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import com.mojang.blaze3d.systems.RenderSystem;

import java.util.Collections;
import java.util.List;

public class EugenicsCraftingScreen extends AbstractContainerScreen<EugenicsCraftingMenu> {
    private static final ResourceLocation EUGENICS_CRAFTING_GUI =
            new ResourceLocation(furmutage.MOD_ID, "textures/gui/eugenics_crafting.png");

    private static final ResourceLocation EUGENICS_BOOK_CLOSED =
            new ResourceLocation(furmutage.MOD_ID, "textures/gui/eugenics_recipe_book_closed.png");
    private static final ResourceLocation EUGENICS_BOOK_OPEN =
            new ResourceLocation(furmutage.MOD_ID, "textures/gui/eugenics_recipe_book_open.png");

    private boolean bookOpen = false;
    private List<EugenicsCraftingRecipe> eugenicsRecipes = Collections.emptyList();
    private EugenicsCraftingRecipe selectedRecipe = null;
    private static final int RECIPES_PER_PAGE = 32;
    private int currentPage = 0;

    public EugenicsCraftingScreen(EugenicsCraftingMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 29;

        // Place a custom eugenics recipe book button at the top-left of the UI
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        int buttonX = i + 6;
        int buttonY = j + 7;

        int buttonWidth = 18;
        int buttonHeight = 20;

        this.addRenderableWidget(new ImageButton(
                buttonX, buttonY,
                buttonWidth, buttonHeight,
                0, 0, 0,
                EUGENICS_BOOK_CLOSED,
                buttonWidth, buttonHeight,
                btn -> {
                    bookOpen = !bookOpen;
                    if (bookOpen) {
                        refreshEugenicsRecipes();
                    } else {
                        selectedRecipe = null;
                    }
                }
        ) {
            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                ResourceLocation tex = bookOpen ? EUGENICS_BOOK_OPEN : EUGENICS_BOOK_CLOSED;
                guiGraphics.blit(tex, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);
            }
        });
    }

    private void refreshEugenicsRecipes() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            eugenicsRecipes = Collections.emptyList();
            return;
        }
        eugenicsRecipes = mc.level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.EUGENICS_CRAFTING.get());
        currentPage = 0;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if (bookOpen) {
            renderEugenicsBook(guiGraphics, mouseX, mouseY);
        }

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderEugenicsBook(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (eugenicsRecipes.isEmpty()) {
            return;
        }

        int guiLeft = this.leftPos;
        int guiTop = (this.height - this.imageHeight) / 2;

        // Move the recipe panel to the left side of the main GUI
        int panelWidth = 90;
        int panelHeight = this.imageHeight;
        int panelX = guiLeft - panelWidth - 6;
        int panelY = guiTop;

        // Background panel
        int bgColor = 0xC0101010; // semi-transparent dark
        guiGraphics.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, bgColor);

        int cellSize = 18;
        int cols = 4;
        int startX = panelX + 6;
        int startY = panelY + 6;

        ItemStack hovered = ItemStack.EMPTY;

        int total = eugenicsRecipes.size();
        int pageCount = (total + RECIPES_PER_PAGE - 1) / RECIPES_PER_PAGE;
        if (currentPage >= pageCount) {
            currentPage = Math.max(0, pageCount - 1);
        }
        int startIndex = currentPage * RECIPES_PER_PAGE;
        int endIndex = Math.min(total, startIndex + RECIPES_PER_PAGE);

        for (int index = startIndex; index < endIndex; index++) {
            EugenicsCraftingRecipe recipe = eugenicsRecipes.get(index);
            int displayIndex = index - startIndex;
            int col = displayIndex % cols;
            int row = displayIndex / cols;
            int x = startX + col * cellSize;
            int y = startY + row * cellSize;

            ItemStack stack = recipe.getResultItem(Minecraft.getInstance().level.registryAccess());
            guiGraphics.renderItem(stack, x, y);
            guiGraphics.renderItemDecorations(this.font, stack, x, y);

            if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
                hovered = stack;
            }
        }

        // Page controls (if more than one page)
        if (pageCount > 1) {
            String pageText = (currentPage + 1) + "/" + pageCount;
            int textWidth = this.font.width(pageText);
            int controlsY = panelY + panelHeight - 12;
            int centerX = panelX + (panelWidth - textWidth) / 2;
            guiGraphics.drawString(this.font, pageText, centerX, controlsY, 0xFFFFFF, false);

            // Simple "<" and ">" markers for previous/next pages
            guiGraphics.drawString(this.font, "<", panelX + 6, controlsY, 0xFFFFFF, false);
            guiGraphics.drawString(this.font, ">", panelX + panelWidth - 10, controlsY, 0xFFFFFF, false);
        }

        if (!hovered.isEmpty()) {
            guiGraphics.renderTooltip(this.font, hovered, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (bookOpen && !eugenicsRecipes.isEmpty()) {
            int guiLeft = this.leftPos;
            int guiTop = (this.height - this.imageHeight) / 2;

            int panelWidth = 90;
            int panelHeight = this.imageHeight;
            int panelX = guiLeft - panelWidth - 6;
            int panelY = guiTop;

            if (mouseX >= panelX && mouseX < panelX + panelWidth && mouseY >= panelY && mouseY < panelY + panelHeight) {
                int cellSize = 18;
                int cols = 4;
                int startX = panelX + 6;
                int startY = panelY + 6;

                int total = eugenicsRecipes.size();
                int pageCount = (total + RECIPES_PER_PAGE - 1) / RECIPES_PER_PAGE;

                int relX = (int) mouseX - startX;
                int relY = (int) mouseY - startY;
                if (relX >= 0 && relY >= 0) {
                    int col = relX / cellSize;
                    int row = relY / cellSize;
                    if (col >= 0 && col < cols && row >= 0) {
                        int displayIndex = row * cols + col;
                        int recipeIndex = currentPage * RECIPES_PER_PAGE + displayIndex;
                        if (recipeIndex >= 0 && recipeIndex < total) {
                            selectedRecipe = eugenicsRecipes.get(recipeIndex);
                            return true;
                        }
                    }
                }

                // Handle page navigation clicks near the bottom of the panel
                if (pageCount > 1) {
                    int controlsY = panelY + panelHeight - 12;
                    if (mouseY >= controlsY && mouseY < controlsY + 10) {
                        // Previous page area ("<")
                        if (mouseX >= panelX + 4 && mouseX < panelX + 20) {
                            currentPage = (currentPage - 1 + pageCount) % pageCount;
                            return true;
                        }
                        // Next page area (">")
                        if (mouseX >= panelX + panelWidth - 20 && mouseX < panelX + panelWidth) {
                            currentPage = (currentPage + 1) % pageCount;
                            return true;
                        }
                    }
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(EUGENICS_CRAFTING_GUI, i, j, 0, 0, this.imageWidth, this.imageHeight);

        // Render phantom ingredients from the selected recipe into the 3x3 grid
        if (bookOpen && selectedRecipe != null) {
            renderGhostRecipe(guiGraphics, i, j);
        }
    }

    private void renderGhostRecipe(GuiGraphics guiGraphics, int guiLeft, int guiTop) {
        NonNullList<Ingredient> ingredients = selectedRecipe.getIngredients();
        int recipeWidth = selectedRecipe.getRecipeWidth();
        int recipeHeight = selectedRecipe.getRecipeHeight();

        int gridStartX = guiLeft + 30;
        int gridStartY = guiTop + 17;

        for (int row = 0; row < recipeHeight; row++) {
            for (int col = 0; col < recipeWidth; col++) {
                int idx = col + row * recipeWidth;
                if (idx < ingredients.size()) {
                    Ingredient ing = ingredients.get(idx);
                    if (!ing.isEmpty()) {
                        ItemStack[] stacks = ing.getItems();
                        if (stacks.length > 0) {
                            ItemStack stack = stacks[0];
                            int x = gridStartX + col * 18;
                            int y = gridStartY + row * 18;

                            // Draw the item, then overlay a translucent layer to make it clearly "ghost"
                            guiGraphics.renderItem(stack, x, y);
                            RenderSystem.enableBlend();
                            // ARGB: 0x80FFFFFF = 50% opaque white overlay
                            guiGraphics.fill(x, y, x + 16, y + 16, 0x80FFFFFF);
                            RenderSystem.disableBlend();
                        }
                    }
                }
            }
        }
    }
}
