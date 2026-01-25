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
    private static final ResourceKey<Biome> WASTELAND_MOUNTAIN_KEY = ResourceKey.create(Registries.BIOME, 
            ResourceLocation.fromNamespaceAndPath("furmutage", "wasteland_mountain"));
    private static final ResourceKey<Biome> WASTELAND_TAINTED_FLAT_KEY = ResourceKey.create(Registries.BIOME, 
            ResourceLocation.fromNamespaceAndPath("furmutage", "wasteland_tainted_flat"));
    
    public static final Codec<WastelandBiomeSource> DIRECT_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.LONG.fieldOf("seed").orElse(0L).forGetter(source -> source.seed)
            ).apply(instance, WastelandBiomeSource::new)
    );
    
    public static final Codec<WastelandBiomeSource> CODEC = DIRECT_CODEC;
    
    private final long seed;
    private Holder<Biome> wastelandBiome;
    private Holder<Biome> mountainBiome;
    private Holder<Biome> taintedFlatBiome;
    
    public static final DeferredRegister<Codec<? extends BiomeSource>> BIOME_SOURCES = 
            DeferredRegister.create(Registries.BIOME_SOURCE, "furmutage");
    
    public static final RegistryObject<Codec<WastelandBiomeSource>> WASTELAND_BIOME_SOURCE = 
            BIOME_SOURCES.register("wasteland_biome_source", () -> CODEC);
    
    public WastelandBiomeSource(long seed) {
        super();
        this.seed = seed;
        this.wastelandBiome = null;
        this.mountainBiome = null;
        this.taintedFlatBiome = null;
    }
    
    public WastelandBiomeSource(long seed, Holder<Biome> wastelandBiome, Holder<Biome> mountainBiome, Holder<Biome> taintedFlatBiome) {
        super();
        this.seed = seed;
        this.wastelandBiome = wastelandBiome;
        this.mountainBiome = mountainBiome;
        this.taintedFlatBiome = taintedFlatBiome;
    }
    
    // Initialize all biomes from registry lookup
    public void resolveBiome(HolderGetter<Biome> biomeRegistry) {
        try {
            if (this.wastelandBiome == null) {
                this.wastelandBiome = biomeRegistry.getOrThrow(WASTELAND_KEY);
                furmutage.LOGGER.info("Resolved wasteland biome: {}", WASTELAND_KEY.location());
            }
            if (this.mountainBiome == null) {
                this.mountainBiome = biomeRegistry.getOrThrow(WASTELAND_MOUNTAIN_KEY);
                furmutage.LOGGER.info("Resolved mountain biome: {}", WASTELAND_MOUNTAIN_KEY.location());
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
        // Use noise to create separate, large biome regions
        // Each biome gets its own distinct area
        
        // Ensure biomes are resolved - if not, use fallback to prevent hangs
        if (wastelandBiome == null || mountainBiome == null || taintedFlatBiome == null) {
            // This should not happen if resolveBiome was called, but provide fallback
            furmutage.LOGGER.warn("WastelandBiomeSource biomes not resolved! Using fallback biome. " +
                "Wasteland: " + (wastelandBiome != null) + 
                ", Mountain: " + (mountainBiome != null) + 
                ", Tainted: " + (taintedFlatBiome != null));
            
            // Fallback: return a default biome if available
            if (wastelandBiome != null) {
                return wastelandBiome;
            }
            
            // Last resort: throw a clear error instead of returning null
            // This will cause a clear error message instead of hanging
            throw new IllegalStateException("WastelandBiomeSource biomes not resolved! " +
                "Make sure the wasteland dimension biomes are properly registered. " +
                "This usually means the mod failed to initialize properly.");
        }
        
        // Use large-scale noise to create separate biome regions
        // Scale down coordinates to create much larger biome areas (divide by 400 for ~400 block regions)
        double scaledX = x / 400.0;
        double scaledZ = z / 400.0;
        
        // Create very smooth, large-scale noise using sine/cosine for separate regions
        double biomeNoise = Math.sin(scaledX * 0.3 + seed * 0.001) * Math.cos(scaledZ * 0.3 + seed * 0.001);
        
        // Check for rivers first (winding paths)
        if (isRiverPosition(x, z)) {
            return wastelandBiome; // Rivers use wasteland biome
        }
        
        // Create separate biome regions based on noise value
        // Mountains: negative noise values
        // Tainted Flat: medium positive values
        // Regular Wasteland: high positive values
        if (biomeNoise < -0.3) {
            // Mountain biome region
            return mountainBiome;
        } else if (biomeNoise < 0.2) {
            // Tainted flat biome region
            return taintedFlatBiome;
        } else {
            // Regular wasteland biome region
            return wastelandBiome;
        }
    }
    
    // Check if position is part of a river (winding paths)
    private boolean isRiverPosition(int x, int z) {
        // Create winding river paths using sine waves
        double riverWidth = 4.0; // River width in blocks
        
        // River 1: Winding north-south
        double river1X = Math.sin(z * 0.03 + seed * 0.001) * 100.0;
        double dist1 = Math.abs(x - river1X);
        
        // River 2: Winding east-west  
        double river2Z = Math.cos(x * 0.03 + seed * 0.001) * 100.0;
        double dist2 = Math.abs(z - river2Z);
        
        // River 3: Diagonal winding
        double river3X = Math.sin((x + z) * 0.025 + seed * 0.001) * 120.0;
        double river3Z = Math.cos((x - z) * 0.025 + seed * 0.001) * 120.0;
        double dist3 = Math.sqrt((x - river3X) * (x - river3X) + (z - river3Z) * (z - river3Z));
        
        // Check if position is within any river
        return dist1 < riverWidth || dist2 < riverWidth || dist3 < riverWidth;
    }
    
    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        // Return all biomes if available, otherwise return empty stream
        // This prevents crashes during initialization
        if (wastelandBiome != null && mountainBiome != null && taintedFlatBiome != null) {
            return Stream.of(wastelandBiome, mountainBiome, taintedFlatBiome);
        } else if (wastelandBiome != null) {
            return Stream.of(wastelandBiome);
        }
        // Return empty stream if biomes aren't resolved yet
        // This is safe and won't cause hangs
        return Stream.empty();
    }
    
    public BiomeSource withSeed(long seed) {
        return new WastelandBiomeSource(seed);
    }
}
