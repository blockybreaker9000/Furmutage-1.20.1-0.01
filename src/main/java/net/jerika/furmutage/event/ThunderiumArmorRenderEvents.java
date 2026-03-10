package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Reserved for Thunderium armor render tweaks. Body-part hiding is disabled for now.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ThunderiumArmorRenderEvents {

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        // No body-part unrendering for now.
    }
}

