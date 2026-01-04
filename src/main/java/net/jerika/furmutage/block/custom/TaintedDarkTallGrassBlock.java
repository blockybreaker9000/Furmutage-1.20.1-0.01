package net.jerika.furmutage.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.Collections;
import java.util.List;

/**
 * Tall tainted dark grass foliage block with 3 texture variants.
 */
public class TaintedDarkTallGrassBlock extends DoublePlantBlock {
    public static final IntegerProperty VARIANT = IntegerProperty.create("variant", 0, 2);

    public TaintedDarkTallGrassBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(VARIANT, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF, VARIANT);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        
        // Set random variant when placed
        if (!oldState.is(state.getBlock())) {
            if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
                // Set variant for lower half
                int variant = level.getRandom().nextInt(3);
                level.setBlock(pos, state.setValue(VARIANT, variant), 2);
                
                // Also set variant for upper half if it exists
                BlockPos abovePos = pos.above();
                BlockState aboveState = level.getBlockState(abovePos);
                if (aboveState.is(this) && aboveState.getValue(HALF) == DoubleBlockHalf.UPPER) {
                    level.setBlock(abovePos, aboveState.setValue(VARIANT, variant), 2);
                }
            } else {
                // Set variant for upper half
                int variant = level.getRandom().nextInt(3);
                level.setBlock(pos, state.setValue(VARIANT, variant), 2);
                
                // Also set variant for lower half if it exists
                BlockPos belowPos = pos.below();
                BlockState belowState = level.getBlockState(belowPos);
                if (belowState.is(this) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                    level.setBlock(belowPos, belowState.setValue(VARIANT, variant), 2);
                }
            }
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        // Can be placed on tainted dark blocks
        return state.is(ModBlocks.TAINTED_DARK_GRASS.get()) ||
               state.is(ModBlocks.TAINTED_DARK_DIRT.get()) ||
               state.is(ModBlocks.TAINTED_DARK_SAND.get()) ||
               state.is(ModBlocks.TAINTED_DARK_LOG.get()) ||
               state.is(ModBlocks.STRIPPED_TAINTED_DARK_LOG.get());
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        // Drop nothing when broken (like vanilla tall grass)
        return Collections.emptyList();
    }
}

