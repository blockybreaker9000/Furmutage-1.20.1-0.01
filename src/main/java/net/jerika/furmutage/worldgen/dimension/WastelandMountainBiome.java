package net.jerika.furmutage.worldgen.dimension;

import net.jerika.furmutage.furmutage;
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

public class WastelandMountainBiome {
    public static final ResourceKey<Biome> WASTELAND_MOUNTAIN_KEY = ResourceKey.create(Registries.BIOME, 
            new ResourceLocation(furmutage.MOD_ID, "wasteland_mountain"));
    
    public static RegistryObject<Biome> WASTELAND_MOUNTAIN = ModBiomes.BIOMES.register("wasteland_mountain", WastelandMountainBiome::createMountainBiome);
    
    private static Biome createMountainBiome() {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        
        // Fewer mobs in mountains
        spawnSettings.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 5, 1, 2));
        
        // Use empty generation settings (defined in JSON)
        BiomeGenerationSettings generationSettings = BiomeGenerationSettings.EMPTY;
        
        // Foggy sky colors - slightly darker for mountains
        int skyColor = calculateSkyColor(0.4f);
        int fogColor = 0xA0A0A0; // Darker gray fog
        
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.3f) // Colder in mountains
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
