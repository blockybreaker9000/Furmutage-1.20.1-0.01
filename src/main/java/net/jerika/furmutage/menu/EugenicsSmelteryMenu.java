package net.jerika.furmutage.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipeType;

public class EugenicsSmelteryMenu extends AbstractFurnaceMenu {

    private final ContainerData data;

    public EugenicsSmelteryMenu(int containerId, Inventory playerInventory, Container container, ContainerData data) {
        // Use vanilla blasting recipes so the Eugenics Smeltery can cook all blast-furnace-compatible items/blocks
        super(ModMenuTypes.EUGENICS_SMELTERY_MENU.get(), RecipeType.BLASTING,
                RecipeBookType.BLAST_FURNACE, containerId, playerInventory, container, data);
        this.data = data;
    }

    /** Expose for GUI: burn (flame) progress 0–14. */
    public int getBurnProgressScaled() {
        int burnDuration = this.data.get(1);
        return burnDuration == 0 ? 0 : this.data.get(0) * 14 / burnDuration;
    }

    /** Expose for GUI: smelting (arrow) progress 0–24. */
    public int getSmeltingProgressScaled() {
        int smeltingTotal = this.data.get(3);
        return smeltingTotal == 0 ? 0 : this.data.get(2) * 24 / smeltingTotal;
    }
}
