package net.jerika.furmutage.event;

import net.jerika.furmutage.entity.client.model.*;
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
        event.registerLayerDefinition(ModModelLayers.WITHERED_LATEX_PUDDING_LAYER, WitheredLatexPuddingModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.MUTANT_FAMILY_LAYER, MutantFamilyModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.LATEX_TENTICLE_LIMBS_MUTANT_LAYER, LatexTenticleLimbsMutantModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.TSC_DRONE_LAYER, TSCDroneModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BACTERIA_LAYER, BacteriaModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.LATEX_BOMBER_MUTANT_LAYER, LatexBomberMutantModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.LATEX_EXO_MUTANT_LAYER, LatexExoMutantModel::createBodyLayer);
    }

}
