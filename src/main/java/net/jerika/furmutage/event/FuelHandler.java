package net.jerika.furmutage.event;

import net.jerika.furmutage.block.custom.ModBlocks;
import net.jerika.furmutage.furmutage;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles fuel registration for custom items.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FuelHandler {
    
    @SubscribeEvent
    public static void onFurnaceFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        // Register tainted white reed as fuel (0.25 of an item = 50 ticks)
        // 1 item = 200 ticks, so 0.25 = 50 ticks
        if (event.getItemStack().getItem() == ModBlocks.TAINTED_WHITE_REED.get().asItem()) {
            event.setBurnTime(50);
        }
    }
}

