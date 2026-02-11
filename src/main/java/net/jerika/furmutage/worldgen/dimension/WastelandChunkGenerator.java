package net.jerika.furmutage.worldgen.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.MegaJungleTrunkPlacer;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class WastelandChunkGenerator extends ChunkGenerator {
    public static final Codec<WastelandChunkGenerator> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter(WastelandChunkGenerator::getBiomeSource),
                    Codec.LONG.fieldOf("seed").orElse(0L).forGetter(gen -> gen.seed)
            ).apply(instance, instance.stable(WastelandChunkGenerator::new))
    );
    
    public static final DeferredRegister<Codec<? extends ChunkGenerator>> CHUNK_GENERATORS = 
            DeferredRegister.create(Registries.CHUNK_GENERATOR, "furmutage");
    
    public static final RegistryObject<Codec<WastelandChunkGenerator>> WASTELAND_CHUNK_GENERATOR = 
            CHUNK_GENERATORS.register("wasteland_chunk_generator", () -> CODEC);
    
    private final long seed;
    private static Block whiteLatexFluidBlock = null;
    
    public WastelandChunkGenerator(BiomeSource biomeSource, long seed) {
        super(biomeSource);
        this.seed = seed;
        // Resolve biome if it's a WastelandBiomeSource
        if (biomeSource instanceof WastelandBiomeSource wastelandBiomeSource) {
            // We'll need to resolve this when we have registry access
            // For now, it will be resolved when getNoiseBiome is called with a proper registry
        }
    }
    
    // Method to resolve biome source after world is loaded
    public void resolveBiomeSource(net.minecraft.core.HolderGetter<net.minecraft.world.level.biome.Biome> biomeRegistry) {
        if (this.biomeSource instanceof WastelandBiomeSource wastelandBiomeSource) {
            wastelandBiomeSource.resolveBiome(biomeRegistry);
        }
    }
    
    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }
    
    public ChunkGenerator withSeed(long seed) {
        // Create a new biome source with the new seed if it's a WastelandBiomeSource
        BiomeSource newBiomeSource = this.biomeSource;
        if (this.biomeSource instanceof WastelandBiomeSource wastelandBiomeSource) {
            newBiomeSource = wastelandBiomeSource.withSeed(seed);
        }
        return new WastelandChunkGenerator(newBiomeSource, seed);
    }
    
    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structures, RandomState randomState, ChunkAccess chunk) {
        int chunkX = chunk.getPos().x * 16;
        int chunkZ = chunk.getPos().z * 16;
        RandomSource random = RandomSource.create(region.getSeed() + chunkX * 7919L + chunkZ * 71993L);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX + x;
                int worldZ = chunkZ + z;
                BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(worldX, region.getMaxBuildHeight(), worldZ);

                int surfaceY = findSurfaceY(region, worldX, worldZ);
                if (surfaceY < region.getMinBuildHeight()) continue;

                pos.setY(surfaceY + 1);
                Holder<Biome> biomeHolder = region.getBiome(pos);
                ResourceLocation biomeId = biomeHolder.unwrapKey().map(k -> k.location()).orElse(null);
                if (biomeId == null) continue;

                String biomePath = biomeId.getPath();
                if (biomePath.equals("wasteland_tainted_flat")) {
                    if (random.nextDouble() < 0.025) {
                        tryPlaceTaintedWhiteTree(region, pos, random);
                    } else if (random.nextDouble() < 0.12 && region.getBlockState(pos).isAir()) {
                        region.setBlock(pos, net.jerika.furmutage.block.custom.ModBlocks.TAINTED_WHITE_GRASS_FOLIAGE.get().defaultBlockState(), 3);
                    }
                } else if (biomePath.equals("wasteland_tainted_dark_grove")) {
                    if (random.nextDouble() < 0.02) {
                        tryPlaceTaintedDarkTree(region, pos, random);
                    } else if (random.nextDouble() < 0.15 && region.getBlockState(pos).isAir()) {
                        region.setBlock(pos, net.jerika.furmutage.block.custom.ModBlocks.TAINTED_DARK_GRASS_FOLIAGE.get().defaultBlockState(), 3);
                    }
                }
            }
        }
    }

    private int findSurfaceY(WorldGenRegion region, int x, int z) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, region.getMaxBuildHeight() - 1, z);
        while (pos.getY() > region.getMinBuildHeight()) {
            if (!region.getBlockState(pos).isAir()) {
                return pos.getY();
            }
            pos.move(0, -1, 0);
        }
        return region.getMinBuildHeight() - 1;
    }

    private void tryPlaceTaintedWhiteTree(WorldGenRegion region, BlockPos pos, RandomSource random) {
        TreeConfiguration config = new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(net.jerika.furmutage.block.custom.ModBlocks.TAINTED_WHITE_LOG.get().defaultBlockState()),
                new ForkingTrunkPlacer(5, 3, 3),
                BlockStateProvider.simple(net.jerika.furmutage.block.custom.ModBlocks.TAINTED_WHITE_LEAF.get().defaultBlockState()),
                new AcaciaFoliagePlacer(ConstantInt.of(3), ConstantInt.of(3)),
                new TwoLayersFeatureSize(2, 2, 2)
        ).dirt(BlockStateProvider.simple(net.jerika.furmutage.block.custom.ModBlocks.TAINTED_WHITE_DIRT.get().defaultBlockState())).build();

        FeaturePlaceContext<TreeConfiguration> context = new FeaturePlaceContext<>(Optional.empty(), region, this, random, pos, config);
        Feature.TREE.place(context);
    }

    private void tryPlaceTaintedDarkTree(WorldGenRegion region, BlockPos pos, RandomSource random) {
        TreeConfiguration config = new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(net.jerika.furmutage.block.custom.ModBlocks.TAINTED_DARK_LOG.get().defaultBlockState()),
                new MegaJungleTrunkPlacer(10, 2, 19),
                BlockStateProvider.simple(net.jerika.furmutage.block.custom.ModBlocks.TAINTED_DARK_LEAF.get().defaultBlockState()),
                new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
                new TwoLayersFeatureSize(0, 0, 0)
        ).dirt(BlockStateProvider.simple(net.jerika.furmutage.block.custom.ModBlocks.TAINTED_DARK_DIRT.get().defaultBlockState())).build();

        FeaturePlaceContext<TreeConfiguration> context = new FeaturePlaceContext<>(Optional.empty(), region, this, random, pos, config);
        Feature.TREE.place(context);
    }
    
    @Override
    public void applyCarvers(WorldGenRegion chunkRegion, long seed, RandomState randomState, BiomeManager biomeAccess, StructureManager structureAccessor, ChunkAccess chunk, GenerationStep.Carving carverStep) {
        // No carvers - flat terrain
    }
    
    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
        // Use default mob spawning
    }
    
    @Override
    public int getGenDepth() {
        return 384; // Standard depth
    }
    
    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
        // Generate smooth terrain with rare ocean masses
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int chunkX = chunk.getPos().x * 16;
        int chunkZ = chunk.getPos().z * 16;
        int seaLevel = getSeaLevel();
        
        // Get white latex fluid block
        if (whiteLatexFluidBlock == null) {
            whiteLatexFluidBlock = getWhiteLatexFluidBlock();
        }
        
        // Pre-calculate heights for multi-pass smoothing
        int[][] heights = new int[20][20]; // Include larger border for better smoothing
        for (int x = -2; x < 18; x++) {
            for (int z = -2; z < 18; z++) {
                int worldX = chunkX + x;
                int worldZ = chunkZ + z;
                heights[x + 2][z + 2] = getBaseHeight(worldX, worldZ, Heightmap.Types.WORLD_SURFACE_WG, chunk, randomState);
            }
        }
        
        // Apply minimal smoothing pass (preserve more natural variation)
        int[][] smoothedHeights = new int[20][20];
        for (int x = 0; x < 20; x++) {
            for (int z = 0; z < 20; z++) {
                if (x > 0 && x < 19 && z > 0 && z < 19) {
                    int center = heights[x][z];
                    int n = heights[x][z - 1];
                    int s = heights[x][z + 1];
                    int e = heights[x + 1][z];
                    int w = heights[x - 1][z];
                    smoothedHeights[x][z] = (center * 4 + n + s + e + w) / 8; // Minimal smoothing for more variation
                } else {
                    smoothedHeights[x][z] = heights[x][z];
                }
            }
        }
        
        // Generate terrain for each column in the chunk with light smoothing
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX + x;
                int worldZ = chunkZ + z;
                
                // Light smoothing with 9-point weighted average (preserve variation)
                int center = smoothedHeights[x + 2][z + 2];
                int north = smoothedHeights[x + 2][z + 1];
                int south = smoothedHeights[x + 2][z + 3];
                int east = smoothedHeights[x + 3][z + 2];
                int west = smoothedHeights[x + 1][z + 2];
                int ne = smoothedHeights[x + 3][z + 1];
                int nw = smoothedHeights[x + 1][z + 1];
                int se = smoothedHeights[x + 3][z + 3];
                int sw = smoothedHeights[x + 1][z + 3];
                
                // Light weighted average to preserve natural terrain variation
                int baseHeight = (center * 3 + (north + south + east + west) * 1 + ne + nw + se + sw) / 11;
                
                String biomeType = getBiomeType(worldX, worldZ);
                
                // Check for ravines and pits before generating terrain
                int ravineDepth = getRavineDepth(worldX, worldZ, baseHeight);
                int pitDepth = getPitDepth(worldX, worldZ, baseHeight);
                int actualDepth = Math.max(ravineDepth, pitDepth);
                
                // Fill terrain based on biome type
                for (int y = chunk.getMaxBuildHeight() - 1; y >= chunk.getMinBuildHeight(); y--) {
                    mutablePos.set(worldX, y, z);
                    
                    // Check if this position is in a ravine or pit
                    boolean inRavine = ravineDepth > 0 && y < baseHeight && y >= baseHeight - ravineDepth;
                    boolean inPit = pitDepth > 0 && y < baseHeight && y >= baseHeight - pitDepth;
                    boolean inHazard = inRavine || inPit;
                    
                    if (y > baseHeight) {
                        // Air above surface
                        chunk.setBlockState(mutablePos, Blocks.AIR.defaultBlockState(), false);
                    } else if (y == baseHeight && !inHazard) {
                        // Surface block based on biome (only if not in ravine/pit)
                        if (biomeType.equals("river")) {
                            // River - white latex fluid at surface
                            if (whiteLatexFluidBlock != null) {
                                chunk.setBlockState(mutablePos, whiteLatexFluidBlock.defaultBlockState(), false);
                            }
                        } else if (biomeType.equals("tainted_flat")) {
                            // Tainted flat - use tainted white grass
                            chunk.setBlockState(mutablePos, net.jerika.furmutage.block.custom.ModBlocks.TAINTED_WHITE_GRASS.get().defaultBlockState(), false);
                        } else if (biomeType.equals("tainted_dark_grove")) {
                            // Tainted dark grove - tainted dark grass surface
                            chunk.setBlockState(mutablePos, net.jerika.furmutage.block.custom.ModBlocks.TAINTED_DARK_GRASS.get().defaultBlockState(), false);
                        } else if (biomeType.equals("hills")) {
                            // Hills - rocky stone/gravel surface
                            chunk.setBlockState(mutablePos, Blocks.STONE.defaultBlockState(), false);
                        } else if (biomeType.equals("scrubland")) {
                            // Scrubland - coarse dirt
                            chunk.setBlockState(mutablePos, Blocks.COARSE_DIRT.defaultBlockState(), false);
                        } else {
                            // Regular wasteland - dirt surface
                            chunk.setBlockState(mutablePos, Blocks.DIRT.defaultBlockState(), false);
                        }
                    } else if (y > baseHeight - 4 && y < baseHeight && !inHazard) {
                        // Middle layers - biome dependent (only if not in ravine/pit)
                        if (biomeType.equals("tainted_flat")) {
                            chunk.setBlockState(mutablePos, net.jerika.furmutage.block.custom.ModBlocks.TAINTED_WHITE_DIRT.get().defaultBlockState(), false);
                        } else if (biomeType.equals("tainted_dark_grove")) {
                            chunk.setBlockState(mutablePos, net.jerika.furmutage.block.custom.ModBlocks.TAINTED_DARK_DIRT.get().defaultBlockState(), false);
                        } else if (biomeType.equals("hills")) {
                            if (y > baseHeight - 2) {
                                chunk.setBlockState(mutablePos, Blocks.STONE.defaultBlockState(), false);
                            } else {
                                chunk.setBlockState(mutablePos, Blocks.DIRT.defaultBlockState(), false);
                            }
                        } else if (biomeType.equals("scrubland")) {
                            chunk.setBlockState(mutablePos, Blocks.DIRT.defaultBlockState(), false);
                        } else {
                            chunk.setBlockState(mutablePos, Blocks.DIRT.defaultBlockState(), false);
                        }
                    } else if (y <= baseHeight - 4 && !inHazard) {
                        // Stone below (only if not in ravine/pit)
                        chunk.setBlockState(mutablePos, Blocks.STONE.defaultBlockState(), false);
                    }
                    
                    // Ravines and pits are air (dangerous drops)
                    if (inHazard) {
                        chunk.setBlockState(mutablePos, Blocks.AIR.defaultBlockState(), false);
                    }
                    
                    // Fill rivers and ocean masses with white latex fluid
                    if (whiteLatexFluidBlock != null) {
                        if (biomeType.equals("river") && y <= seaLevel && y > baseHeight - 10) {
                            chunk.setBlockState(mutablePos, whiteLatexFluidBlock.defaultBlockState(), false);
                        } else if (baseHeight < seaLevel && y <= seaLevel && y > baseHeight) {
                            chunk.setBlockState(mutablePos, whiteLatexFluidBlock.defaultBlockState(), false);
                        }
                    }
                }

                // Trees are placed in buildSurface using Feature.TREE (same as sapling growth)
                // Foliage only placed here for tainted dark grove and tainted flat
                if (biomeType.equals("tainted_dark_grove")) {
                    long groveSeed = (long)worldX * 7919L + (long)worldZ * 9973L + seed;
                    java.util.Random groveRandom = new java.util.Random(groveSeed);
                    if (groveRandom.nextDouble() < 0.15) {
                        BlockPos.MutableBlockPos foliagePos = new BlockPos.MutableBlockPos(worldX, baseHeight + 1, worldZ);
                        if (chunk.getBlockState(foliagePos).isAir()) {
                            chunk.setBlockState(foliagePos, net.jerika.furmutage.block.custom.ModBlocks.TAINTED_DARK_GRASS_FOLIAGE.get().defaultBlockState(), false);
                        }
                    }
                }
                if (biomeType.equals("tainted_flat")) {
                    long flatSeed = (long)worldX * 7919L + (long)worldZ * 9973L + seed;
                    java.util.Random flatRandom = new java.util.Random(flatSeed);
                    if (flatRandom.nextDouble() < 0.12) {
                        BlockPos.MutableBlockPos foliagePos = new BlockPos.MutableBlockPos(worldX, baseHeight + 1, worldZ);
                        if (chunk.getBlockState(foliagePos).isAir()) {
                            chunk.setBlockState(foliagePos, net.jerika.furmutage.block.custom.ModBlocks.TAINTED_WHITE_GRASS_FOLIAGE.get().defaultBlockState(), false);
                        }
                    }
                }
            }
        }
        
        // Place oak trunk trees (no leaves) in small groups for all wasteland biomes
        placeWastelandOakTrunkGroups(chunk, chunkX, chunkZ, smoothedHeights, randomState);
        
        return CompletableFuture.completedFuture(chunk);
    }
    
    @Override
    public int getSeaLevel() {
        return 63;
    }
    
    @Override
    public int getMinY() {
        return -64;
    }
    
    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types heightmap, LevelHeightAccessor level, RandomState randomState) {
        int baseHeight = 64;
        String biomeType = getBiomeType(x, z);
        
        // Check for rivers first
        if (biomeType.equals("river")) {
            // River - create varied depth with some dangerous drops
            int riverDepth = 6 + (int)(Math.sin(x * 0.07) * 4); // 2-10 blocks deep, more variation
            // Add occasional deeper sections (dangerous)
            long riverSeed = (long)x * 11111L + (long)z * 22222L + seed;
            java.util.Random riverRandom = new java.util.Random(riverSeed);
            if (riverRandom.nextDouble() < 0.15) { // 15% chance for deep section
                riverDepth += 8 + riverRandom.nextInt(10); // Additional 8-17 blocks
            }
            return baseHeight - riverDepth;
        }
        
        // Generate terrain based on separate biome regions
        if (biomeType.equals("tainted_dark_grove")) {
            // Wooded grove - gentle rolling hills
            double groveNoise = Math.sin(x * 0.02) * Math.cos(z * 0.02) * 5;
            double detailNoise = Math.sin(x * 0.05) * Math.cos(z * 0.05) * 2;
            return baseHeight + (int)Math.round(groveNoise + detailNoise);
        } else if (biomeType.equals("hills")) {
            // Rocky hills - steeper terrain
            double hillsNoise = Math.sin(x * 0.012) * Math.cos(z * 0.012) * 20;
            double detailNoise = Math.sin(x * 0.035) * Math.cos(z * 0.035) * 5;
            double cliffNoise = Math.sin(x * 0.08) * Math.cos(z * 0.08) * 6;
            return baseHeight + (int)Math.round(hillsNoise + detailNoise + cliffNoise);
        } else if (biomeType.equals("scrubland")) {
            // Dry scrubland - moderate variation
            double scrubNoise = Math.sin(x * 0.018) * Math.cos(z * 0.018) * 6;
            double detailNoise = Math.sin(x * 0.04) * Math.cos(z * 0.04) * 2;
            return baseHeight + (int)Math.round(scrubNoise + detailNoise);
        } else if (biomeType.equals("tainted_flat")) {
            // More varied terrain with hills and depressions
            double smoothVariation = Math.sin(x * 0.02) * Math.cos(z * 0.02) * 3; // Moderate hills
            double detailVariation = Math.sin(x * 0.06) * Math.cos(z * 0.06) * 1.5; // Small details
            double depressionNoise = Math.sin(x * 0.03) * Math.cos(z * 0.03) * 2; // Some depressions
            return baseHeight + (int)Math.round(smoothVariation + detailVariation - depressionNoise);
        } else { // Regular wasteland
            // More varied terrain with hills, valleys, and cliffs
            double smoothVariation = Math.sin(x * 0.018) * Math.cos(z * 0.018) * 4; // Larger hills
            double detailVariation = Math.sin(x * 0.045) * Math.cos(z * 0.045) * 1.5; // Medium detail
            double valleyNoise = Math.sin(x * 0.025) * Math.cos(z * 0.025) * 3; // Valleys
            double cliffVariation = Math.sin(x * 0.1) * Math.cos(z * 0.1) * 4; // Steep drops
            int variation = (int)Math.round(smoothVariation + detailVariation - valleyNoise + cliffVariation);
            
            // More frequent ocean masses (4% chance) - deeper and more dangerous
            long oceanSeed = (long)x * 7919L + (long)z * 9973L + seed;
            java.util.Random oceanRandom = new java.util.Random(oceanSeed);
            if (oceanRandom.nextDouble() < 0.04) {
                int oceanDepth = 10 + oceanRandom.nextInt(10); // 10-19 blocks deep (deeper)
                return baseHeight - oceanDepth;
            }
            
            return baseHeight + variation;
        }
    }
    
    // Helper method to determine biome type at a position (matches biome source logic)
    private String getBiomeType(int x, int z) {
        // Use same logic as biome source for consistency
        double scaledX = x / 150.0;
        double scaledZ = z / 150.0;
        double biomeNoise = Math.sin(scaledX * 0.3 + seed * 0.001) * Math.cos(scaledZ * 0.3 + seed * 0.001);
        
        // Check for rivers first
        if (isRiverPosition(x, z)) return "river";
        
        // Determine biome based on noise (matches biome source)
        if (biomeNoise < -0.4) {
            return "tainted_dark_grove";
        } else if (biomeNoise < -0.2) {
            return "hills";
        } else if (biomeNoise < 0.0) {
            return "scrubland";
        } else if (biomeNoise < 0.2) {
            return "tainted_flat";
        } else {
            return "wasteland";
        }
    }
    
    private boolean isRiverPosition(int x, int z) {
        // Multiple river branches that wind through the landscape
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
    
    /**
     * Check if there's a ravine at this position and return its depth.
     * Ravines are long, narrow chasms that wind through the terrain.
     * @return Depth of ravine in blocks, or 0 if no ravine
     */
    private int getRavineDepth(int x, int z, int baseHeight) {
        long ravineSeed = (long)x * 12345L + (long)z * 67890L + seed;
        java.util.Random ravineRandom = new java.util.Random(ravineSeed);
        
        // 15% chance for a ravine to exist (increased from 8%)
        if (ravineRandom.nextDouble() > 0.15) {
            return 0;
        }
        
        double ravineWidth = 2.0 + ravineRandom.nextDouble() * 6.0; // 2-8 blocks wide (more varied)
        
        // Ravine 1: Winding north-south
        double ravine1X = Math.sin(z * 0.02 + seed * 0.0005) * 150.0;
        double dist1 = Math.abs(x - ravine1X);
        
        // Ravine 2: Winding east-west
        double ravine2Z = Math.cos(x * 0.02 + seed * 0.0005) * 150.0;
        double dist2 = Math.abs(z - ravine2Z);
        
        // Ravine 3: Diagonal winding
        double ravine3X = Math.sin((x + z) * 0.015 + seed * 0.0005) * 180.0;
        double ravine3Z = Math.cos((x - z) * 0.015 + seed * 0.0005) * 180.0;
        double dist3 = Math.sqrt((x - ravine3X) * (x - ravine3X) + (z - ravine3Z) * (z - ravine3Z));
        
        double ravine4X = Math.sin(z * 0.025 + seed * 0.0003) * 120.0;
        double ravine4Z = Math.cos(x * 0.025 + seed * 0.0003) * 120.0;
        double dist4 = Math.sqrt((x - ravine4X) * (x - ravine4X) + (z - ravine4Z) * (z - ravine4Z));
        
        // Check if position is within any ravine
        boolean inRavine = dist1 < ravineWidth || dist2 < ravineWidth || dist3 < ravineWidth || dist4 < ravineWidth;
        
        if (inRavine) {
            // Ravines are 20-40 blocks deep (more dangerous falls)
            int depth = 20 + ravineRandom.nextInt(21);
            return depth;
        }
        
        return 0;
    }
    
    /**
     * Check if there's an open pit at this position and return its depth.
     * Pits are circular or irregular holes in the ground.
     * @return Depth of pit in blocks, or 0 if no pit
     */
    private int getPitDepth(int x, int z, int baseHeight) {
        long pitSeed = (long)x * 54321L + (long)z * 98765L + seed;
        java.util.Random pitRandom = new java.util.Random(pitSeed);
        
        // 12% chance for a pit to exist (increased from 5%)
        if (pitRandom.nextDouble() > 0.12) {
            return 0;
        }
        
        // Create pit centers using noise - more varied distribution
        double pitScale = 0.006; // Scale for pit distribution (more frequent)
        double pitNoise1 = Math.sin(x * pitScale + seed * 0.001) * Math.cos(z * pitScale + seed * 0.001);
        double pitNoise2 = Math.sin(x * pitScale * 1.3 + seed * 0.0015) * Math.cos(z * pitScale * 1.3 + seed * 0.0015);
        double pitNoise3 = Math.sin(x * pitScale * 0.7 + seed * 0.002) * Math.cos(z * pitScale * 0.7 + seed * 0.002);
        
        // Pits form where noise values are very low (more frequent)
        if (pitNoise1 < -0.6 || pitNoise2 < -0.6 || pitNoise3 < -0.6) {
            // Calculate distance to nearest pit center
            int pitCenterX = (int)(x / 40.0) * 40 + (int)(Math.sin(x * 0.012 + seed * 0.001) * 20);
            int pitCenterZ = (int)(z / 40.0) * 40 + (int)(Math.cos(z * 0.012 + seed * 0.001) * 20);
            
            double distToCenter = Math.sqrt((x - pitCenterX) * (x - pitCenterX) + (z - pitCenterZ) * (z - pitCenterZ));
            double pitRadius = 3.0 + pitRandom.nextDouble() * 8.0; // 3-11 block radius (more varied)
            
            if (distToCenter < pitRadius) {
                // Pits are 15-35 blocks deep (more dangerous falls)
                int depth = 15 + pitRandom.nextInt(21);
                // Make pits deeper in the center
                double depthFactor = 1.0 - (distToCenter / pitRadius) * 0.4; // 40% deeper in center
                depth = (int)(depth * depthFactor);
                return Math.max(depth, 12); // Minimum 12 blocks deep (more dangerous)
            }
        }
        
        return 0;
    }
    
    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState randomState) {
        // Create a column with flat-ish terrain
        int baseHeight = getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, level, randomState);
        BlockState[] states = new BlockState[level.getHeight()];
        
        // Fill with air above surface
        for (int y = level.getMaxBuildHeight() - 1; y > baseHeight; y--) {
            states[y - level.getMinBuildHeight()] = Blocks.AIR.defaultBlockState();
        }
        
        // Surface layer - dirt
        states[baseHeight - level.getMinBuildHeight()] = Blocks.DIRT.defaultBlockState();
        
        // Dirt layers
        for (int y = baseHeight - 1; y > baseHeight - 4; y--) {
            if (y >= level.getMinBuildHeight()) {
                states[y - level.getMinBuildHeight()] = Blocks.DIRT.defaultBlockState();
            }
        }
        
        // Stone below
        for (int y = baseHeight - 4; y >= level.getMinBuildHeight(); y--) {
            states[y - level.getMinBuildHeight()] = Blocks.STONE.defaultBlockState();
        }
        
        // Replace water with white latex fluid at sea level and in ocean masses
        if (whiteLatexFluidBlock == null) {
            whiteLatexFluidBlock = getWhiteLatexFluidBlock();
        }
        
        int seaLevel = getSeaLevel();
        if (whiteLatexFluidBlock != null) {
            // Fill ocean masses with white latex fluid
            if (baseHeight < seaLevel) {
                for (int y = seaLevel; y > baseHeight; y--) {
                    if (y >= level.getMinBuildHeight() && y < level.getMaxBuildHeight()) {
                        states[y - level.getMinBuildHeight()] = whiteLatexFluidBlock.defaultBlockState();
                    }
                }
            }
            // Also replace any water blocks at sea level
            for (int y = seaLevel; y >= seaLevel - 5; y--) {
                if (y >= level.getMinBuildHeight() && y < level.getMaxBuildHeight()) {
                    int index = y - level.getMinBuildHeight();
                    if (index >= 0 && index < states.length && states[index] != null && states[index].is(Blocks.WATER)) {
                        states[index] = whiteLatexFluidBlock.defaultBlockState();
                    }
                }
            }
        }
        
        return new NoiseColumn(level.getMinBuildHeight(), states);
    }
    
    @Override
    public void addDebugScreenInfo(List<String> info, RandomState randomState, BlockPos pos) {
        info.add("Wasteland Dimension");
    }
    
    private void placeWastelandOakTrunkGroups(ChunkAccess chunk, int chunkX, int chunkZ, int[][] smoothedHeights, RandomState randomState) {
        for (int gi = -1; gi <= 2; gi++) {
            for (int gj = -1; gj <= 2; gj++) {
                int cx = chunkX + 8 + gi * 24;
                int cz = chunkZ + 8 + gj * 24;
                String biomeType = getBiomeType(cx, cz);
                if (biomeType.equals("river") || biomeType.equals("tainted_flat") || biomeType.equals("tainted_dark_grove")) continue;
                long groupSeed = (long)cx * 31991L + (long)cz * 71993L + seed;
                java.util.Random groupRandom = new java.util.Random(groupSeed);
                if (groupRandom.nextDouble() >= 0.35) continue;
                int treeCount = 2 + groupRandom.nextInt(3);
                for (int t = 0; t < treeCount; t++) {
                    int tx = cx + groupRandom.nextInt(5) - 2;
                    int tz = cz + groupRandom.nextInt(5) - 2;
                    int localX = tx - chunkX;
                    int localZ = tz - chunkZ;
                    if (localX < 0 || localX >= 16 || localZ < 0 || localZ >= 16) continue;
                    int baseHeight = smoothedHeights[localX + 2][localZ + 2];
                    BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos(tx, baseHeight, tz);
                    if (chunk.getBlockState(checkPos).isAir()) continue;
                    placeOakTrunkTree(chunk, tx, baseHeight + 1, tz, groupRandom);
                }
            }
        }
    }

    private void placeOakTrunkTree(ChunkAccess chunk, int baseX, int baseY, int baseZ, java.util.Random random) {
        BlockState logState = Blocks.OAK_LOG.defaultBlockState();
        int height = 4 + random.nextInt(4);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int y = 0; y < height; y++) {
            pos.set(baseX, baseY + y, baseZ);
            if (chunk.getBlockState(pos).canBeReplaced()) {
                chunk.setBlockState(pos, logState, false);
            }
        }
    }

    private static Block getWhiteLatexFluidBlock() {
        try {
            Class<?> changedBlocksClass = Class.forName("net.ltxprogrammer.changed.init.ChangedBlocks");
            java.lang.reflect.Field whiteLatexFluidField = changedBlocksClass.getField("WHITE_LATEX_FLUID");
            return (Block) whiteLatexFluidField.get(null);
        } catch (Exception e) {
            net.jerika.furmutage.furmutage.LOGGER.warn("Could not find Changed mod's white latex fluid block: " + e.getMessage());
            return null;
        }
    }
}
