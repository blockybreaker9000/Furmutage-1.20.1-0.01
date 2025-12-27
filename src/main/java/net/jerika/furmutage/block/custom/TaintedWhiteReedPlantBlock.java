package net.jerika.furmutage.block.custom;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

/**
 * The body block for tainted white reed (the middle/upper parts of the plant).
 */
public class TaintedWhiteReedPlantBlock extends GrowingPlantBodyBlock {
    public static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    private final Supplier<GrowingPlantHeadBlock> headBlock;
    
    public TaintedWhiteReedPlantBlock(BlockBehaviour.Properties properties, Supplier<GrowingPlantHeadBlock> headBlock) {
        super(properties, Direction.UP, SHAPE, false);
        this.headBlock = headBlock;
    }
    
    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return headBlock.get();
    }
}

