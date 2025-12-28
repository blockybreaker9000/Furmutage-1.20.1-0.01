package net.jerika.furmutage.block.custom;

import net.jerika.furmutage.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A sugar cane-like plant that grows on tainted white grass/dirt/sand.
 * Can grow up to 7 blocks tall, with different textures for top and bottom.
 * Can bear fruit (tainted red rose apple) when fully grown.
 */
public class TaintedWhiteReedBlock extends GrowingPlantHeadBlock {
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    private static final double GROW_PER_TICK_PROBABILITY = 0.14D; // Growth speed
    public static final int MAX_HEIGHT = 7; // Max height of the plant
    public static final IntegerProperty FRUIT_AGE = IntegerProperty.create("fruit_age", 0, 3); // 0 = no fruit, 3 = fully grown
    private static final int MAX_FRUIT_AGE = 3; // Fully grown fruit age
    private final java.util.function.Supplier<Block> bodyBlock;
    
    public TaintedWhiteReedBlock(BlockBehaviour.Properties properties, java.util.function.Supplier<Block> bodyBlock) {
        super(properties, Direction.UP, SHAPE, false, GROW_PER_TICK_PROBABILITY);
        this.bodyBlock = bodyBlock;
        this.registerDefaultState(this.defaultBlockState().setValue(FRUIT_AGE, 0));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FRUIT_AGE);
    }
    
    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
        return 1;
    }
    
    @Override
    protected boolean canGrowInto(BlockState state) {
        return state.is(Blocks.AIR);
    }
    
    @Override
    protected Block getBodyBlock() {
        return bodyBlock.get();
    }
    
    @Override
    protected boolean canAttachTo(BlockState state) {
        // Can grow on tainted white grass, dirt, sand, and other reed blocks
        return state.is(ModBlocks.TAINTED_WHITE_GRASS.get()) ||
               state.is(ModBlocks.TAINTED_WHITE_DIRT.get()) ||
               state.is(ModBlocks.TAINTED_WHITE_SAND.get()) ||
               state.is(ModBlocks.TAINTED_WHITE_REED.get()) ||
               state.is(ModBlocks.TAINTED_WHITE_REED_PLANT.get());
    }
    
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        
        // Check if it can attach to the block below (other reed blocks)
        if (canAttachTo(belowState) && !belowState.is(ModBlocks.TAINTED_WHITE_GRASS.get()) && 
            !belowState.is(ModBlocks.TAINTED_WHITE_DIRT.get()) && 
            !belowState.is(ModBlocks.TAINTED_WHITE_SAND.get())) {
            return true;
        }
        
        // Can survive on tainted white grass, dirt, or sand
        if (belowState.is(ModBlocks.TAINTED_WHITE_GRASS.get()) ||
            belowState.is(ModBlocks.TAINTED_WHITE_DIRT.get()) ||
            belowState.is(ModBlocks.TAINTED_WHITE_SAND.get())) {
            BlockPos groundPos = belowPos;
            // Check if there's water or tainted blocks adjacent to the ground block
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockState adjacent = level.getBlockState(groundPos.relative(direction));
                if (adjacent.is(Blocks.WATER) || 
                    adjacent.is(ModBlocks.TAINTED_WHITE_GRASS.get()) ||
                    adjacent.is(ModBlocks.TAINTED_WHITE_DIRT.get()) ||
                    adjacent.is(ModBlocks.TAINTED_WHITE_SAND.get())) {
                    return true;
                }
            }
            // Also allow if there's water below
            BlockPos belowGround = groundPos.below();
            BlockState belowGroundState = level.getBlockState(belowGround);
            if (belowGroundState.is(Blocks.WATER)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        
        // Gradually grow fruit on the top (head) reed block
        // The head block is always the top block, so we just grow fruit on this block
        int currentFruitAge = state.getValue(FRUIT_AGE);
        if (currentFruitAge < MAX_FRUIT_AGE) {
            // Growth chance: ~10% per random tick to advance to next stage (faster growth)
            if (random.nextInt(10) == 0) {
                level.setBlock(pos, state.setValue(FRUIT_AGE, currentFruitAge + 1), 3);
            }
        }
    }
    
    
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, 
                                  InteractionHand hand, BlockHitResult hit) {
        // Allow harvesting fruit by right-clicking (only when fully grown)
        int fruitAge = state.getValue(FRUIT_AGE);
        if (fruitAge >= MAX_FRUIT_AGE) {
            if (!level.isClientSide) {
                // Drop the fruit
                Block.popResource(level, pos, new ItemStack(ModItems.TAINTED_RED_ROSE_APPLE.get()));
                // Reset fruit growth
                level.setBlock(pos, state.setValue(FRUIT_AGE, 0), 3);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, 
                              net.minecraft.world.level.block.entity.BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        // Drop fruit if block had fully grown fruit when broken
        dropFruitIfPresent(level, pos, state);
    }
    
    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
        super.spawnAfterBreak(state, level, pos, tool, dropExperience);
        // Drop fruit if block had fully grown fruit when broken (handles non-player breaks like explosions)
        dropFruitIfPresent(level, pos, state);
        // Drop the reed item (handles all break types)
        Block.popResource(level, pos, new ItemStack(this.asItem()));
    }
    
    /**
     * Drops the fruit if the reed has fully grown fruit.
     */
    private void dropFruitIfPresent(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide) {
            int fruitAge = state.getValue(FRUIT_AGE);
            if (fruitAge >= MAX_FRUIT_AGE) {
                Block.popResource(level, pos, new ItemStack(ModItems.TAINTED_RED_ROSE_APPLE.get()));
            }
        }
    }
    
}

