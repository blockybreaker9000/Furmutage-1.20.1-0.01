package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Restricts pickaxe usage: gold pickaxe can only break diamond (ore/block);
 * only iron pickaxe can break gold ore.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GoldIronPickaxeRestrictionEvents {

    private static boolean isGoldPickaxe(ItemStack stack) {
        return stack.is(Items.GOLDEN_PICKAXE);
    }

    private static boolean isIronPickaxe(ItemStack stack) {
        return stack.is(Items.IRON_PICKAXE);
    }

    private static boolean isDiamondBreakable(BlockState state) {
        return state.is(Blocks.DIAMOND_ORE) || state.is(Blocks.DEEPSLATE_DIAMOND_ORE) || state.is(Blocks.DIAMOND_BLOCK);
    }

    private static boolean isGoldOre(BlockState state) {
        return state.is(Blocks.GOLD_ORE) || state.is(Blocks.DEEPSLATE_GOLD_ORE) || state.is(Blocks.NETHER_GOLD_ORE);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        ItemStack tool = event.getPlayer().getMainHandItem();
        BlockState state = event.getState();

        // Gold pickaxe: can only break diamond ore/block
        if (isGoldPickaxe(tool)) {
            if (!isDiamondBreakable(state)) {
                event.setCanceled(true);
            }
            return;
        }

        // Gold ore: only iron pickaxe can break it
        if (isGoldOre(state)) {
            if (!isIronPickaxe(tool)) {
                event.setCanceled(true);
            }
        }
    }
}
