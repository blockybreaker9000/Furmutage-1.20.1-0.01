package net.jerika.furmutage.block.entity;

import net.jerika.furmutage.item.ModItems;
import net.jerika.furmutage.menu.EugenicsSmelteryMenu;
import net.jerika.furmutage.recipe.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;

public class EugenicsSmelteryBlockEntity extends AbstractFurnaceBlockEntity {

    public EugenicsSmelteryBlockEntity(BlockPos pos, BlockState state) {
        // Use custom Eugenics Smelting recipe type so only furmutage:eugenics_smelting recipes work here
        super(ModBlockEntities.EUGENICS_SMELTERY_BLOCK_ENTITY.get(), pos, state, ModRecipeTypes.EUGENICS_SMELTING.get());
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

    @Override
    protected int getBurnDuration(ItemStack stack) {
        // Only white/dark latex clumps act as fuel in the Eugenics oven
        if (stack.is(ModItems.WHITE_LATEX_CLUMP.get()) || stack.is(ModItems.DARK_LATEX_CLUMP.get())) {
            // Similar to coal burn time (1600 ticks), tweak if desired
            return 1600;
        }
        return 0;
    }
}
