package net.jerika.furmutage.event;

import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.client.model.MuglingModel;
import net.jerika.furmutage.furmutage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.MUGLING_LAYER, MuglingModel::createBodyLayer);


    }

}
