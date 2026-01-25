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

public class WastelandBiome {
    public static final ResourceKey<Biome> WASTELAND_KEY = ResourceKey.create(Registries.BIOME, 
            new ResourceLocation(furmutage.MOD_ID, "wasteland"));
    
    public static RegistryObject<Biome> WASTELAND = ModBiomes.BIOMES.register("wasteland", WastelandBiome::createWastelandBiome);
    
    private static Biome createWastelandBiome() {
        // Create biome with dead grass, foggy atmosphere, and white latex fluid
        // Note: Most biome settings are defined in JSON, this is just a fallback
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        
        // Add some hostile mobs but fewer than normal
        spawnSettings.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 20, 1, 4));
        spawnSettings.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 10, 1, 3));
        
        // Create empty biome generation settings (features are defined in JSON)
        // Since we're using JSON, we'll use a minimal generation settings
        // The actual biome will be loaded from JSON, this is just a fallback
        BiomeGenerationSettings generationSettings = BiomeGenerationSettings.EMPTY;
        
        // Foggy sky colors - grayish fog
        int skyColor = calculateSkyColor(0.5f); // Grayish sky
        int fogColor = 0xC0C0C0; // Light gray fog
        
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false) // No rain
                .temperature(0.5f) // Moderate temperature
                .downfall(0.0f) // No precipitation
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0xFFFFFF) // White for latex fluid
                        .waterFogColor(0xFFFFFF) // White fog
                        .fogColor(fogColor) // Gray fog
                        .skyColor(skyColor) // Grayish sky
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
