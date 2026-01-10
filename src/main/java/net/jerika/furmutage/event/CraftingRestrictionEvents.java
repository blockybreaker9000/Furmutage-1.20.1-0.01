package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.util.CraftingRestrictions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

/**
 * Event handler that prevents crafting Changed and Furmutage items in normal crafting tables and player inventory.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CraftingRestrictionEvents {
    
    /**
     * Prevents restricted items from appearing in result slots of normal crafting tables.
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
            return;
        }
        
        Player player = event.player;
        AbstractContainerMenu container = player.containerMenu;
        
        // Only check normal crafting tables and inventory crafting
        if (container instanceof CraftingMenu || container instanceof InventoryMenu) {
            // Find the result slot and check if it has a restricted item
            for (int i = 0; i < container.slots.size(); i++) {
                var slot = container.slots.get(i);
                if (slot instanceof ResultSlot) {
                    ItemStack result = slot.getItem();
                    if (!result.isEmpty() && CraftingRestrictions.isRestrictedItem(result)) {
                        // Clear the restricted item from the result slot
                        slot.set(ItemStack.EMPTY);
                    }
                }
            }
        }
    }
}
