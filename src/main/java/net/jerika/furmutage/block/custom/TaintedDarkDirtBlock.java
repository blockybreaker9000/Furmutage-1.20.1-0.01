package net.jerika.furmutage.block.custom;

import net.jerika.furmutage.furmutage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class TaintedDarkDirtBlock extends Block {
    public TaintedDarkDirtBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Convert to tainted dark grass if there's light above
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        
        if (aboveState.isAir() && level.getMaxLocalRawBrightness(abovePos) >= 9) {
            // Convert to tainted dark grass if conditions are right
            if (random.nextInt(5) == 0) { // 20% chance
                level.setBlock(pos, ModBlocks.TAINTED_DARK_GRASS.get().defaultBlockState(), 3);
            }
        }
        
        // Convert nearby water to dark latex fluid
        if (random.nextInt(2) == 0) { // 50% chance per random tick (faster spreading)
            convertNearbyWaterToLatexFluid(level, pos, random, false);
        }

    }

    /**
     * Spreads taint to nearby dirt and grass blocks.
     */
    private void spreadToNearbyBlocks(ServerLevel level, BlockPos pos, RandomSource random) {
        // Check blocks in a 3x3x3 area around this block
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip self
                    
                    BlockPos checkPos = pos.offset(x, y, z);
                    BlockState checkState = level.getBlockState(checkPos);
                    
                    // Convert dirt to tainted dark dirt
                    if (checkState.is(Blocks.DIRT) || checkState.is(Blocks.COARSE_DIRT)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_DIRT.get().defaultBlockState(), 3);
                    }
                    // Convert grass blocks to tainted dark grass
                    else if (checkState.is(Blocks.GRASS_BLOCK)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_GRASS.get().defaultBlockState(), 3);
                    }
                    // Convert sand to tainted dark sand
                    else if (checkState.is(Blocks.SAND) || checkState.is(Blocks.RED_SAND)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_SAND.get().defaultBlockState(), 3);
                    }
                    // Convert vanilla logs to tainted dark log
                    else if (checkState.is(Blocks.OAK_LOG) || checkState.is(Blocks.BIRCH_LOG) || 
                        checkState.is(Blocks.SPRUCE_LOG) || checkState.is(Blocks.JUNGLE_LOG) ||
                        checkState.is(Blocks.ACACIA_LOG) || checkState.is(Blocks.DARK_OAK_LOG) ||
                        checkState.is(Blocks.MANGROVE_LOG) || checkState.is(Blocks.CHERRY_LOG) ||
                        checkState.is(Blocks.CRIMSON_STEM) || checkState.is(Blocks.WARPED_STEM) ||
                        checkState.is(Blocks.STRIPPED_OAK_LOG) || checkState.is(Blocks.STRIPPED_BIRCH_LOG) ||
                        checkState.is(Blocks.STRIPPED_SPRUCE_LOG) || checkState.is(Blocks.STRIPPED_JUNGLE_LOG) ||
                        checkState.is(Blocks.STRIPPED_ACACIA_LOG) || checkState.is(Blocks.STRIPPED_DARK_OAK_LOG) ||
                        checkState.is(Blocks.STRIPPED_MANGROVE_LOG) || checkState.is(Blocks.STRIPPED_CHERRY_LOG) ||
                        checkState.is(Blocks.STRIPPED_CRIMSON_STEM) || checkState.is(Blocks.STRIPPED_WARPED_STEM)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_LOG.get().defaultBlockState(), 3);
                    }
                    // Convert vanilla planks to tainted dark planks
                    else if (checkState.is(Blocks.OAK_PLANKS) || checkState.is(Blocks.BIRCH_PLANKS) ||
                        checkState.is(Blocks.SPRUCE_PLANKS) || checkState.is(Blocks.JUNGLE_PLANKS) ||
                        checkState.is(Blocks.ACACIA_PLANKS) || checkState.is(Blocks.DARK_OAK_PLANKS) ||
                        checkState.is(Blocks.MANGROVE_PLANKS) || checkState.is(Blocks.CHERRY_PLANKS) ||
                        checkState.is(Blocks.CRIMSON_PLANKS) || checkState.is(Blocks.WARPED_PLANKS)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_PLANKS.get().defaultBlockState(), 3);
                    }
                    // Convert vanilla leaves to tainted dark leaves
                    else if (checkState.is(Blocks.OAK_LEAVES) || checkState.is(Blocks.BIRCH_LEAVES) ||
                        checkState.is(Blocks.SPRUCE_LEAVES) || checkState.is(Blocks.JUNGLE_LEAVES) ||
                        checkState.is(Blocks.ACACIA_LEAVES) || checkState.is(Blocks.DARK_OAK_LEAVES) ||
                        checkState.is(Blocks.MANGROVE_LEAVES) || checkState.is(Blocks.CHERRY_LEAVES) ||
                        checkState.is(Blocks.AZALEA_LEAVES) || checkState.is(Blocks.FLOWERING_AZALEA_LEAVES) ||
                        checkState.is(Blocks.NETHER_WART_BLOCK) || checkState.is(Blocks.WARPED_WART_BLOCK)) {
                        level.setBlock(checkPos, ModBlocks.TAINTED_DARK_LEAF.get().defaultBlockState(), 3);
                    }
                }
            }
        }
    }
    
    /**
     * Converts nearby water blocks to dark latex fluid.
     */
    private void convertNearbyWaterToLatexFluid(ServerLevel level, BlockPos pos, RandomSource random, boolean isWhite) {
        try {
            // Get the ChangedBlocks class
            Class<?> changedBlocksClass = Class.forName("net.ltxprogrammer.changed.init.ChangedBlocks");
            
            // Get the fluid block based on type
            String fluidBlockName = isWhite ? "WHITE_LATEX_FLUID" : "DARK_LATEX_FLUID";
            java.lang.reflect.Field fluidField = changedBlocksClass.getField(fluidBlockName);
            Object fluidRegistryObject = fluidField.get(null);
            
            if (fluidRegistryObject == null) {
                return;
            }
            
            // Call .get() on the RegistryObject to get the actual Block
            java.lang.reflect.Method getMethod = fluidRegistryObject.getClass().getMethod("get");
            net.minecraft.world.level.block.Block latexFluidBlock = (net.minecraft.world.level.block.Block) getMethod.invoke(fluidRegistryObject);
            
            if (latexFluidBlock == null) {
                return;
            }
            
            // Search within 12 blocks for water (increased from 5)
            int radius = 12;
            java.util.List<BlockPos> waterPositions = new java.util.ArrayList<>();
            
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos checkPos = pos.offset(x, y, z);
                        double distance = Math.sqrt(x * x + y * y + z * z);
                        
                        // Only convert water within radius blocks
                        if (distance <= radius) {
                            BlockState checkState = level.getBlockState(checkPos);
                            if (checkState.is(Blocks.WATER)) {
                                waterPositions.add(checkPos);
                            }
                        }
                    }
                }
            }
            
            // Convert up to 10 water blocks per tick (increased from 3 for faster spreading)
            if (!waterPositions.isEmpty()) {
                java.util.Collections.shuffle(waterPositions, new java.util.Random(random.nextLong()));
                int toConvert = Math.min(10, waterPositions.size());
                
                for (int i = 0; i < toConvert; i++) {
                    BlockPos waterPos = waterPositions.get(i);
                    level.setBlock(waterPos, latexFluidBlock.defaultBlockState(), 3);
                }
            }
        } catch (ClassNotFoundException e) {
            // Changed mod not loaded, skip
        } catch (Exception e) {
            furmutage.LOGGER.warn("Failed to convert water to latex fluid: {}", e.getMessage());
        }
    }
}

