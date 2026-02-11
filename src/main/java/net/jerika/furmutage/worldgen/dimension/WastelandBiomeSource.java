package net.jerika.furmutage.worldgen.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.jerika.furmutage.furmutage;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Stream;

public class WastelandBiomeSource extends BiomeSource {
    private static final ResourceKey<Biome> WASTELAND_KEY = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath("furmutage", "wasteland"));
    private static final ResourceKey<Biome> WASTELAND_TAINTED_DARK_GROVE_KEY = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath("furmutage", "wasteland_tainted_dark_grove"));
    private static final ResourceKey<Biome> WASTELAND_HILLS_KEY = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath("furmutage", "wasteland_hills"));
    private static final ResourceKey<Biome> WASTELAND_SCRUBLAND_KEY = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath("furmutage", "wasteland_scrubland"));
    private static final ResourceKey<Biome> WASTELAND_TAINTED_FLAT_KEY = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath("furmutage", "wasteland_tainted_flat"));

    public static final Codec<WastelandBiomeSource> DIRECT_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.LONG.fieldOf("seed").orElse(0L).forGetter(source -> source.seed)
            ).apply(instance, instance.stable(WastelandBiomeSource::new))
    );

    public static final Codec<WastelandBiomeSource> CODEC = DIRECT_CODEC;

    private final long seed;
    private Holder<Biome> wastelandBiome;
    private Holder<Biome> taintedDarkGroveBiome;
    private Holder<Biome> hillsBiome;
    private Holder<Biome> scrublandBiome;
    private Holder<Biome> taintedFlatBiome;

    public static final DeferredRegister<Codec<? extends BiomeSource>> BIOME_SOURCES =
            DeferredRegister.create(Registries.BIOME_SOURCE, "furmutage");

    public static final RegistryObject<Codec<WastelandBiomeSource>> WASTELAND_BIOME_SOURCE =
            BIOME_SOURCES.register("wasteland_biome_source", () -> CODEC);

    public WastelandBiomeSource(long seed) {
        super();
        this.seed = seed;
        this.wastelandBiome = null;
        this.taintedDarkGroveBiome = null;
        this.hillsBiome = null;
        this.scrublandBiome = null;
        this.taintedFlatBiome = null;
    }

    public WastelandBiomeSource(long seed, Holder<Biome> wastelandBiome, Holder<Biome> taintedDarkGroveBiome,
                                Holder<Biome> hillsBiome, Holder<Biome> scrublandBiome, Holder<Biome> taintedFlatBiome) {
        super();
        this.seed = seed;
        this.wastelandBiome = wastelandBiome;
        this.taintedDarkGroveBiome = taintedDarkGroveBiome;
        this.hillsBiome = hillsBiome;
        this.scrublandBiome = scrublandBiome;
        this.taintedFlatBiome = taintedFlatBiome;
    }

    public void resolveBiome(HolderGetter<Biome> biomeRegistry) {
        try {
            if (this.wastelandBiome == null) {
                this.wastelandBiome = biomeRegistry.getOrThrow(WASTELAND_KEY);
                furmutage.LOGGER.info("Resolved wasteland biome: {}", WASTELAND_KEY.location());
            }
            if (this.taintedDarkGroveBiome == null) {
                this.taintedDarkGroveBiome = biomeRegistry.getOrThrow(WASTELAND_TAINTED_DARK_GROVE_KEY);
                furmutage.LOGGER.info("Resolved tainted dark grove biome: {}", WASTELAND_TAINTED_DARK_GROVE_KEY.location());
            }
            if (this.hillsBiome == null) {
                this.hillsBiome = biomeRegistry.getOrThrow(WASTELAND_HILLS_KEY);
                furmutage.LOGGER.info("Resolved hills biome: {}", WASTELAND_HILLS_KEY.location());
            }
            if (this.scrublandBiome == null) {
                this.scrublandBiome = biomeRegistry.getOrThrow(WASTELAND_SCRUBLAND_KEY);
                furmutage.LOGGER.info("Resolved scrubland biome: {}", WASTELAND_SCRUBLAND_KEY.location());
            }
            if (this.taintedFlatBiome == null) {
                this.taintedFlatBiome = biomeRegistry.getOrThrow(WASTELAND_TAINTED_FLAT_KEY);
                furmutage.LOGGER.info("Resolved tainted flat biome: {}", WASTELAND_TAINTED_FLAT_KEY.location());
            }
            furmutage.LOGGER.info("All biomes resolved successfully for WastelandBiomeSource");
        } catch (Exception e) {
            furmutage.LOGGER.error("Failed to resolve biomes for WastelandBiomeSource", e);
            throw e;
        }
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        if (wastelandBiome == null || taintedDarkGroveBiome == null || hillsBiome == null || scrublandBiome == null || taintedFlatBiome == null) {
            furmutage.LOGGER.warn("WastelandBiomeSource biomes not resolved! Using fallback.");
            if (wastelandBiome != null) return wastelandBiome;
            throw new IllegalStateException("WastelandBiomeSource biomes not resolved!");
        }

        double scaledX = x / 150.0;
        double scaledZ = z / 150.0;
        double biomeNoise = Math.sin(scaledX * 0.3 + seed * 0.001) * Math.cos(scaledZ * 0.3 + seed * 0.001);

        if (isRiverPosition(x, z)) {
            return wastelandBiome;
        }

        if (biomeNoise < -0.4) {
            return taintedDarkGroveBiome;
        } else if (biomeNoise < -0.2) {
            return hillsBiome;
        } else if (biomeNoise < 0.0) {
            return scrublandBiome;
        } else if (biomeNoise < 0.2) {
            return taintedFlatBiome;
        } else {
            return wastelandBiome;
        }
    }

    private boolean isRiverPosition(int x, int z) {
        double riverWidth = 4.0;
        double river1X = Math.sin(z * 0.03 + seed * 0.001) * 100.0;
        double dist1 = Math.abs(x - river1X);
        double river2Z = Math.cos(x * 0.03 + seed * 0.001) * 100.0;
        double dist2 = Math.abs(z - river2Z);
        double river3X = Math.sin((x + z) * 0.025 + seed * 0.001) * 120.0;
        double river3Z = Math.cos((x - z) * 0.025 + seed * 0.001) * 120.0;
        double dist3 = Math.sqrt((x - river3X) * (x - river3X) + (z - river3Z) * (z - river3Z));
        return dist1 < riverWidth || dist2 < riverWidth || dist3 < riverWidth;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        if (wastelandBiome != null && taintedDarkGroveBiome != null && hillsBiome != null && scrublandBiome != null && taintedFlatBiome != null) {
            return Stream.of(wastelandBiome, taintedDarkGroveBiome, hillsBiome, scrublandBiome, taintedFlatBiome);
        } else if (wastelandBiome != null) {
            return Stream.of(wastelandBiome);
        }
        return Stream.empty();
    }

    public BiomeSource withSeed(long seed) {
        return new WastelandBiomeSource(seed);
    }
}
