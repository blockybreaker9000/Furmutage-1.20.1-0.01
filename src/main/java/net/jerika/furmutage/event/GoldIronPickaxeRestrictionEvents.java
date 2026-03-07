package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.config.ModCommonConfig;
import net.jerika.furmutage.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Restricts pickaxe usage: gold pickaxe can only break diamond (ore/block) and gold ore;
 * iron tools cannot break gold ore (diamond/netherite can);
 * iron and roselight glass pickaxes cannot break diamond (ore/block).
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GoldIronPickaxeRestrictionEvents {

    private static boolean isGoldPickaxe(ItemStack stack) {
        return stack.is(Items.GOLDEN_PICKAXE);
    }

    private static boolean isIronPickaxe(ItemStack stack) {
        return stack.is(Items.IRON_PICKAXE);
    }

    private static boolean isStonePickaxe(ItemStack stack) {
        return stack.is(Items.STONE_PICKAXE);
    }

    private static boolean isRoselightGlassPickaxe(ItemStack stack) {
        return stack.is(ModItems.ROSELIGHT_GLASS_PICKAXE.get());
    }

    private static boolean isDiamondOre(BlockState state) {
        return state.is(Blocks.DIAMOND_ORE) || state.is(Blocks.DEEPSLATE_DIAMOND_ORE);
    }

    private static boolean isDiamondBlock(BlockState state) {
        return state.is(Blocks.DIAMOND_BLOCK);
    }

    private static boolean isGoldOre(BlockState state) {
        return state.is(Blocks.GOLD_ORE) || state.is(Blocks.DEEPSLATE_GOLD_ORE) || state.is(Blocks.NETHER_GOLD_ORE);
    }

    private static boolean isIronOre(BlockState state) {
        return state.is(Blocks.IRON_ORE) || state.is(Blocks.DEEPSLATE_IRON_ORE);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        if (!ModCommonConfig.ENABLE_ORE_PROGRESSION.get()) {
            return;
        }

        ItemStack tool = event.getPlayer().getMainHandItem();
        BlockState state = event.getState();

        // Stone pickaxe: cannot break iron ore (forces progression to iron pick).
        if (isStonePickaxe(tool) && isIronOre(state)) {
            event.setCanceled(true);
            return;
        }

        // Gold pickaxe: can only break diamond ore/block and gold ore
        if (isGoldPickaxe(tool)) {
            if (!isDiamondOre(state) && !isDiamondBlock(state) && !isGoldOre(state)) {
                event.setCanceled(true);
            }
            return;
        }

        // Gold ore: iron pickaxe cannot break it (diamond/gold/roselight glass can)
        if (isGoldOre(state) && isIronPickaxe(tool)) {
            event.setCanceled(true);
        }

        // Diamond ore: iron pickaxe and roselight glass cannot break it; gold can.
        if (isDiamondOre(state) && (isIronPickaxe(tool) || isRoselightGlassPickaxe(tool))) {
            event.setCanceled(true);
            return;
        }

        // Diamond block: iron or roselight glass pickaxe cannot break it; gold can.
        if (isDiamondBlock(state) && (isIronPickaxe(tool) || isRoselightGlassPickaxe(tool))) {
            event.setCanceled(true);
        }
    }
}
