package net.jerika.furmutage.block.entity;

import net.jerika.furmutage.menu.EugenicsCraftingMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EugenicsCraftingBlockEntity extends BaseContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(10, ItemStack.EMPTY); // 9 crafting slots + 1 result
    
    public EugenicsCraftingBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EUGENICS_CRAFTING_BLOCK_ENTITY.get(), pos, state);
    }
    
    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.furmutage.eugenics_crafting");
    }
    
    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new EugenicsCraftingMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition));
    }
    
    @Override
    public int getContainerSize() {
        return 10;
    }
    
    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getItem(int index) {
        return index >= 0 && index < items.size() ? items.get(index) : ItemStack.EMPTY;
    }
    
    @Override
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(items, index, count);
    }
    
    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(items, index);
    }
    
    @Override
    public void setItem(int index, ItemStack stack) {
        if (index >= 0 && index < items.size()) {
            items.set(index, stack);
        }
    }
    
    @Override
    public boolean stillValid(Player player) {
        if (level == null || level.getBlockEntity(worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64.0;
    }
    
    @Override
    public void clearContent() {
        items.clear();
    }
    
    public static void tick(Level level, BlockPos pos, BlockState state, EugenicsCraftingBlockEntity blockEntity) {
        // Tick logic if needed
    }
}
