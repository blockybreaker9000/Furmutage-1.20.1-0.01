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
 * Previously prevented crafting Changed and Furmutage items in normal crafting tables and player inventory.
 * Restrictions are now disabled so all items can be crafted in vanilla grids; the Eugenics
 * Crafting Block instead acts as a tier-2 table that can have exclusive recipes.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CraftingRestrictionEvents {
    
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // Restrictions disabled: they do nothing.
    }
}
