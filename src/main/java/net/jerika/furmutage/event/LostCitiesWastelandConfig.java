package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Configures LostCities to spawn in the wasteland dimension.
 * LostCities reads its configuration from data packs, so we've created
 * a JSON configuration file at: data/lostcities/dimensions/furmutage_wasteland.json
 * 
 * NOTE: LostCities must be manually installed and must be compatible with
 * Forge 47.4.0 and mappings parchment 2023.06.26-1.20.1
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LostCitiesWastelandConfig {
    
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            furmutage.LOGGER.info("LostCities configuration file for wasteland dimension is available.");
            furmutage.LOGGER.info("If LostCities mod is installed and compatible, it will spawn large cities in the wasteland dimension.");
            furmutage.LOGGER.info("City settings: 25% chance, 300-600 block radius, 64-150 block height");
            furmutage.LOGGER.warn("NOTE: LostCities must be manually installed. Make sure the version is compatible with Forge 47.4.0");
        });
    }
}
