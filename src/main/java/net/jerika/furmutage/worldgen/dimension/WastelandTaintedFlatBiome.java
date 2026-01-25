package net.jerika.furmutage.worldgen.dimension;

import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.block.custom.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class WastelandTaintedFlatBiome {
    public static final ResourceKey<Biome> WASTELAND_TAINTED_FLAT_KEY = ResourceKey.create(Registries.BIOME, 
            new ResourceLocation(furmutage.MOD_ID, "wasteland_tainted_flat"));
    
    public static RegistryObject<Biome> WASTELAND_TAINTED_FLAT = ModBiomes.BIOMES.register("wasteland_tainted_flat", WastelandTaintedFlatBiome::createTaintedFlatBiome);
    
    private static Biome createTaintedFlatBiome() {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        
        // Some hostile mobs
        spawnSettings.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 15, 1, 3));
        spawnSettings.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 8, 1, 2));
        
        // Use empty generation settings (defined in JSON)
        BiomeGenerationSettings generationSettings = BiomeGenerationSettings.EMPTY;
        
        // Foggy sky colors
        int skyColor = calculateSkyColor(0.5f);
        int fogColor = 0xC0C0C0;
        
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5f)
                .downfall(0.0f)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0xFFFFFF) // White for latex fluid
                        .waterFogColor(0xFFFFFF)
                        .fogColor(fogColor)
                        .skyColor(skyColor)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_DESERT))
                        .build())
                .mobSpawnSettings(spawnSettings.build())
                .generationSettings(generationSettings)
                .build();
    }
    
    private static int calculateSkyColor(float temperature) {
        float f = temperature / 3.0F;
        f = Mth.clamp(f, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
    }
}
