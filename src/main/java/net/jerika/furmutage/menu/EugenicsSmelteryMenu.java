package net.jerika.furmutage.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;

public class EugenicsSmelteryMenu extends AbstractFurnaceMenu {

    public EugenicsSmelteryMenu(int containerId, Inventory playerInventory, Container container, ContainerData data) {
        super(ModMenuTypes.EUGENICS_SMELTERY_MENU.get(), net.jerika.furmutage.recipe.ModRecipeTypes.EUGENICS_SMELTING.get(),
                RecipeBookType.FURNACE, containerId, playerInventory, container, data);
    }
}
