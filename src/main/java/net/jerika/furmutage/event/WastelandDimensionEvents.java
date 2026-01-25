package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.worldgen.dimension.WastelandBiomeSource;
import net.jerika.furmutage.worldgen.dimension.WastelandChunkGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WastelandDimensionEvents {
    
    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }
        
        // Check if this is the wasteland dimension
        ResourceKey<Level> dimensionKey = serverLevel.dimension();
        if (!dimensionKey.location().equals(ResourceLocation.fromNamespaceAndPath("furmutage", "wasteland"))) {
            return;
        }
        
        // Resolve biome source
        ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();
        if (chunkGenerator instanceof WastelandChunkGenerator wastelandChunkGenerator) {
            var biomeRegistry = serverLevel.registryAccess().lookup(Registries.BIOME).orElse(null);
            if (biomeRegistry != null) {
                furmutage.LOGGER.info("Resolving biomes for wasteland dimension...");
                wastelandChunkGenerator.resolveBiomeSource(biomeRegistry);
                furmutage.LOGGER.info("Successfully resolved WastelandBiomeSource for wasteland dimension");
            } else {
                furmutage.LOGGER.error("Biome registry not available for wasteland dimension!");
            }
        } else {
            furmutage.LOGGER.warn("Chunk generator is not WastelandChunkGenerator: {}", chunkGenerator.getClass().getName());
        }
    }
}
