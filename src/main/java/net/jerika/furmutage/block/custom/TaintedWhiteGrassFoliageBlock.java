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
 * Normal/short tainted white grass foliage block.
 */
public class TaintedWhiteGrassFoliageBlock extends BushBlock {
    public TaintedWhiteGrassFoliageBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        // Can be placed on tainted white blocks
        return state.is(ModBlocks.TAINTED_WHITE_GRASS.get()) ||
               state.is(ModBlocks.TAINTED_WHITE_DIRT.get()) ||
               state.is(ModBlocks.TAINTED_WHITE_SAND.get()) ||
               state.is(ModBlocks.TAINTED_WHITE_LOG.get()) ||
               state.is(ModBlocks.STRIPPED_TAINTED_WHITE_LOG.get()) ||
               state.is(ModBlocks.TAINTED_WHITE_PLANKS.get()) ||
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

