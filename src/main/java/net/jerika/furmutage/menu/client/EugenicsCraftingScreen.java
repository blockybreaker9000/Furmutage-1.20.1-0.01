package net.jerika.furmutage.menu.client;

import net.jerika.furmutage.menu.EugenicsCraftingMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class EugenicsCraftingScreen extends AbstractContainerScreen<EugenicsCraftingMenu> {
    private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/crafting_table.png");

    public EugenicsCraftingScreen(EugenicsCraftingMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 29;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(CRAFTING_TABLE_GUI_TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
}
