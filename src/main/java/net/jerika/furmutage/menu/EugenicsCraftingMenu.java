package net.jerika.furmutage.menu;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class EugenicsCraftingMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final Player player;
    private final TransientCraftingContainer craftSlots;
    private final ResultContainer resultSlots = new ResultContainer();
    
    public EugenicsCraftingMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }
    
    public EugenicsCraftingMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenuTypes.EUGENICS_CRAFTING_MENU.get(), containerId);
        this.access = access;
        this.player = playerInventory.player;
        this.craftSlots = new TransientCraftingContainer(this, 3, 3);
        
        // Result slot (index 0) - use vanilla ResultSlot for exact crafting table behavior
        this.addSlot(new ResultSlot(playerInventory.player, this.craftSlots, this.resultSlots, 0, 124, 35));
        
        // Crafting grid slots (indices 1-9)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(this.craftSlots, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }
        
        // Player inventory slots (indices 10-36)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // Player hotbar slots (indices 37-45)
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
    
    protected static void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player, TransientCraftingContainer craftSlots, ResultContainer resultSlots) {
        if (level.isClientSide) {
            return;
        }
        
        ServerPlayer serverPlayer = (ServerPlayer) player;
        ItemStack result = ItemStack.EMPTY;
        
        Optional<CraftingRecipe> optional = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftSlots, level);
        
        if (optional.isPresent()) {
            CraftingRecipe recipe = optional.get();
            if (resultSlots.setRecipeUsed(level, serverPlayer, recipe)) {
                ItemStack recipeResult = recipe.assemble(craftSlots, level.registryAccess());
                result = recipeResult.copy();
            }
        }
        
        resultSlots.setItem(0, result);
        menu.setRemoteSlot(0, result);
        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, result));
    }
    
    @Override
    public void slotsChanged(Container container) {
        this.access.execute((level, pos) -> {
            slotChangedCraftingGrid(this, level, this.player, this.craftSlots, this.resultSlots);
        });
    }
    
    @Override
    public MenuType<?> getType() {
        return ModMenuTypes.EUGENICS_CRAFTING_MENU.get();
    }
    
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            
            if (index == 0) {
                this.access.execute((level, pos) -> {
                    itemstack1.getItem().onCraftedBy(itemstack1, level, player);
                });
                
                if (!this.moveItemStackTo(itemstack1, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
                
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= 10 && index < 46) {
                if (!this.moveItemStackTo(itemstack1, 1, 10, false)) {
                    if (index < 37) {
                        if (!this.moveItemStackTo(itemstack1, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, 10, 46, false)) {
                return ItemStack.EMPTY;
            }
            
            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(player, itemstack1);
            if (index == 0) {
                player.drop(itemstack1, false);
            }
        }
        
        return itemstack;
    }
    
    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, net.jerika.furmutage.block.custom.ModBlocks.EUGENICS_CRAFTING_BLOCK.get());
    }
    
    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, pos) -> {
            this.clearContainer(player, this.craftSlots);
        });
    }
}
