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

public class WastelandScrublandBiome {
    public static final ResourceKey<Biome> WASTELAND_SCRUBLAND_KEY = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(furmutage.MOD_ID, "wasteland_scrubland"));

    public static RegistryObject<Biome> WASTELAND_SCRUBLAND = ModBiomes.BIOMES.register("wasteland_scrubland", WastelandScrublandBiome::createBiome);

    private static Biome createBiome() {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        spawnSettings.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 18, 1, 3));
        spawnSettings.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.HUSK, 5, 1, 2));

        BiomeGenerationSettings generationSettings = BiomeGenerationSettings.EMPTY;

        int skyColor = calculateSkyColor(0.55f);
        int fogColor = 0xB0A090;

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.55f)
                .downfall(0.0f)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0xFFFFFF)
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
