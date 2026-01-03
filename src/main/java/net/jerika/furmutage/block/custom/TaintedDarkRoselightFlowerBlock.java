package net.jerika.furmutage.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.Collections;
import java.util.List;

/**
 * Tainted dark roselight flower that grows on tainted dark grass.
 */
public class TaintedDarkRoselightFlowerBlock extends FlowerBlock {
    public TaintedDarkRoselightFlowerBlock(BlockBehaviour.Properties properties) {
        super(MobEffects.REGENERATION, 7, properties); // Regeneration effect for 7 ticks
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        // Can be placed on tainted dark grass and dirt
        return state.is(ModBlocks.TAINTED_DARK_GRASS.get()) ||
               state.is(ModBlocks.TAINTED_DARK_DIRT.get()) ||
               state.is(ModBlocks.TAINTED_DARK_SAND.get()) ||
               state.is(Blocks.GRASS_BLOCK) ||
               state.is(Blocks.DIRT) ||
               state.is(Blocks.COARSE_DIRT);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        // Drop the flower item when broken
        return Collections.singletonList(new ItemStack(this));
    }
}

