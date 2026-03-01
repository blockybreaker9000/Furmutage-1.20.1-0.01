package net.jerika.furmutage.util;

import net.jerika.furmutage.block.custom.ModBlocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Checks if a block state is one of Furmutage's tainted blocks.
 * Uses ModBlocks directly so we only match this mod's blocks.
 */
public final class TaintedBlocks {

    public static boolean isTaintedWhite(BlockState state) {
        return state.is(ModBlocks.TAINTED_WHITE_LOG.get()) || state.is(ModBlocks.STRIPPED_TAINTED_WHITE_LOG.get())
                || state.is(ModBlocks.TAINTED_WHITE_PLANKS.get()) || state.is(ModBlocks.TAINTED_WHITE_SLAB.get())
                || state.is(ModBlocks.TAINTED_WHITE_STAIRS.get()) || state.is(ModBlocks.TAINTED_WHITE_FENCE.get())
                || state.is(ModBlocks.TAINTED_WHITE_FENCE_GATE.get()) || state.is(ModBlocks.TAINTED_WHITE_DOOR.get())
                || state.is(ModBlocks.TAINTED_WHITE_TRAPDOOR.get()) || state.is(ModBlocks.TAINTED_WHITE_BUTTON.get())
                || state.is(ModBlocks.TAINTED_WHITE_PRESSURE_PLATE.get()) || state.is(ModBlocks.TAINTED_WHITE_LADDER.get())
                || state.is(ModBlocks.TAINTED_WHITE_LEAF.get()) || state.is(ModBlocks.TAINTED_WHITE_SAPLING.get())
                || state.is(ModBlocks.TAINTED_WHITE_GRASS.get()) || state.is(ModBlocks.TAINTED_WHITE_DIRT.get())
                || state.is(ModBlocks.TAINTED_WHITE_SAND.get()) || state.is(ModBlocks.TAINTED_WHITE_VINE.get())
                || state.is(ModBlocks.TAINTED_WHITE_GRASS_FOLIAGE.get()) || state.is(ModBlocks.TAINTED_WHITE_TALL_GRASS.get())
                || state.is(ModBlocks.TAINTED_WHITE_SPOTTED_MUSHROOM.get()) || state.is(ModBlocks.TAINTED_WHITE_DRIP_MUSHROOM.get())
                || state.is(ModBlocks.TAINTED_WHITE_ROSELIGHT_FLOWER.get()) || state.is(ModBlocks.TAINTED_WHITE_CRYSTAL_BLUE_FLOWER.get())
                || state.is(ModBlocks.TAINTED_WHITE_REED.get()) || state.is(ModBlocks.TAINTED_WHITE_REED_PLANT.get());
    }

    public static boolean isTaintedDark(BlockState state) {
        return state.is(ModBlocks.TAINTED_DARK_LOG.get()) || state.is(ModBlocks.STRIPPED_TAINTED_DARK_LOG.get())
                || state.is(ModBlocks.TAINTED_DARK_PLANKS.get()) || state.is(ModBlocks.TAINTED_DARK_SLAB.get())
                || state.is(ModBlocks.TAINTED_DARK_STAIRS.get()) || state.is(ModBlocks.TAINTED_DARK_FENCE.get())
                || state.is(ModBlocks.TAINTED_DARK_FENCE_GATE.get()) || state.is(ModBlocks.TAINTED_DARK_DOOR.get())
                || state.is(ModBlocks.TAINTED_DARK_TRAPDOOR.get()) || state.is(ModBlocks.TAINTED_DARK_BUTTON.get())
                || state.is(ModBlocks.TAINTED_DARK_PRESSURE_PLATE.get()) || state.is(ModBlocks.TAINTED_DARK_LADDER.get())
                || state.is(ModBlocks.TAINTED_DARK_LEAF.get()) || state.is(ModBlocks.TAINTED_DARK_GRASS.get())
                || state.is(ModBlocks.TAINTED_DARK_DIRT.get()) || state.is(ModBlocks.TAINTED_DARK_SAND.get())
                || state.is(ModBlocks.TAINTED_DARK_TALL_GRASS.get()) || state.is(ModBlocks.TAINTED_DARK_GRASS_FOLIAGE.get())
                || state.is(ModBlocks.TAINTED_DARK_ROSELIGHT_FLOWER.get()) || state.is(ModBlocks.TAINTED_DARK_CRYSTAL_BLUE_FLOWER.get())
                || state.is(ModBlocks.TAINTED_DARK_SAPLING.get()) || state.is(ModBlocks.TAINTED_DARK_VINE.get());
    }
}
