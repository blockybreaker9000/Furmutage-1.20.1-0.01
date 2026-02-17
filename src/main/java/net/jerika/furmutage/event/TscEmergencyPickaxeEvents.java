package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.item.ModItems;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Prevents the TSC Emergency pickaxe from breaking diamond ore.
 * It can mine iron but not diamond.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TscEmergencyPickaxeEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        if (event.getPlayer().getMainHandItem().getItem() != ModItems.TSC_EMERGENCY_PICKAXE.get()) {
            return;
        }

        BlockState state = event.getState();
        if (state.is(Blocks.DIAMOND_ORE) || state.is(Blocks.DEEPSLATE_DIAMOND_ORE)) {
            event.setCanceled(true);
        }
    }
}
