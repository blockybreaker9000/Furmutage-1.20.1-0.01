package net.jerika.furmutage.block.custom;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Tainted dark fence that connects to any FenceBlock (vanilla and tainted dark/white).
 */
public class TaintedDarkFenceBlock extends FenceBlock {

    public TaintedDarkFenceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public boolean connectsTo(BlockState state, boolean isSideSolid, Direction direction) {
        return super.connectsTo(state, isSideSolid, direction)
                || state.getBlock() instanceof FenceBlock;
    }
}
