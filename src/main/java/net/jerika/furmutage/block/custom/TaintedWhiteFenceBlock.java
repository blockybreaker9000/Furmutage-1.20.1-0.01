package net.jerika.furmutage.block.custom;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Tainted white fence that connects to any FenceBlock (vanilla and tainted dark/white).
 */
public class TaintedWhiteFenceBlock extends FenceBlock {

    public TaintedWhiteFenceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public boolean connectsTo(BlockState state, boolean isSideSolid, Direction direction) {
        return super.connectsTo(state, isSideSolid, direction)
                || state.getBlock() instanceof FenceBlock;
    }
}
