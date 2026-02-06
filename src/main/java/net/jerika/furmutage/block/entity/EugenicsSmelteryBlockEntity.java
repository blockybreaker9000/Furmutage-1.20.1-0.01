package net.jerika.furmutage.block.entity;

import net.jerika.furmutage.menu.EugenicsSmelteryMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;

public class EugenicsSmelteryBlockEntity extends AbstractFurnaceBlockEntity {

    public EugenicsSmelteryBlockEntity(BlockPos pos, BlockState state) {
        // Use vanilla BLASTING recipe type so this smeltery can cook all blast-furnace recipes
        super(ModBlockEntities.EUGENICS_SMELTERY_BLOCK_ENTITY.get(), pos, state, RecipeType.BLASTING);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.furmutage.eugenics_smeltery");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new EugenicsSmelteryMenu(containerId, playerInventory, this, this.getDataAccess());
    }
    
    public net.minecraft.world.inventory.ContainerData getDataAccess() {
        return this.dataAccess;
    }
    
    public void openMenu(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, this, this.worldPosition);
        }
    }
}
