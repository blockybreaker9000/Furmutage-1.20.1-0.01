package net.jerika.furmutage.menu.client;

import net.jerika.furmutage.menu.EugenicsSmelteryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class EugenicsSmelteryScreen extends AbstractContainerScreen<EugenicsSmelteryMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/blast_furnace.png");

    public EugenicsSmelteryScreen(EugenicsSmelteryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);

        // Flame (burn progress) – blast furnace texture at 176,0 size 14x14
        int burn = this.menu.getBurnProgressScaled();
        if (burn > 0) {
            guiGraphics.blit(TEXTURE, i + 56, j + 36 + 14 - burn, 176, 14 - burn, 14, burn);
        }

        // Arrow (smelting progress) – blast furnace texture at 176,14 size 24x16
        int cook = this.menu.getSmeltingProgressScaled();
        if (cook > 0) {
            guiGraphics.blit(TEXTURE, i + 79, j + 35, 176, 14, cook, 16);
        }
    }
}
