package net.jerika.furmutage.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.Collections;
import java.util.List;

/**
 * Normal/short tainted dark grass foliage block.
 */
public class TaintedDarkGrassFoliageBlock extends BushBlock {
    public TaintedDarkGrassFoliageBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        // Can be placed on tainted dark blocks
        return state.is(ModBlocks.TAINTED_DARK_GRASS.get()) ||
               state.is(ModBlocks.TAINTED_DARK_DIRT.get()) ||
               state.is(ModBlocks.TAINTED_DARK_SAND.get()) ||
               state.is(ModBlocks.TAINTED_DARK_LOG.get()) ||
               state.is(ModBlocks.STRIPPED_TAINTED_DARK_LOG.get()) ||
               state.is(ModBlocks.TAINTED_DARK_PLANKS.get()) ||
               state.is(Blocks.GRASS_BLOCK) ||
               state.is(Blocks.DIRT) ||
               state.is(Blocks.COARSE_DIRT) ||
               state.is(Blocks.SAND) ||
               state.is(Blocks.RED_SAND);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        // Drop nothing when broken (like vanilla grass)
        return Collections.emptyList();
    }
}

