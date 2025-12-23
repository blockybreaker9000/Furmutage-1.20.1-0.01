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
 * Tainted white spotted mushroom that grows on tainted white grass.
 */
public class TaintedWhiteSpottedMushroomBlock extends BushBlock {
    public TaintedWhiteSpottedMushroomBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        // Can be placed on tainted white grass and dirt
        return state.is(ModBlocks.TAINTED_WHITE_GRASS.get()) ||
               state.is(ModBlocks.TAINTED_WHITE_DIRT.get()) ||
               state.is(ModBlocks.TAINTED_WHITE_SAND.get()) ||
               state.is(Blocks.GRASS_BLOCK) ||
               state.is(Blocks.DIRT) ||
               state.is(Blocks.COARSE_DIRT);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        // Drop the mushroom item when broken
        return Collections.singletonList(new ItemStack(this));
    }
}

