package net.jerika.furmutage.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class TaintedWhiteVineBlock extends VineBlock {
    
    public TaintedWhiteVineBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        
        // Infect nearby vanilla vines
        if (random.nextInt(50) == 0) { // 2% chance per random tick
            tryInfectNearbyVines(level, pos, random);
        }
    }
    
    /**
     * Attempts to infect nearby vanilla vines with tainted white vines.
     */
    private void tryInfectNearbyVines(ServerLevel level, BlockPos pos, RandomSource random) {
        // Check all 6 directions
        Direction[] directions = Direction.values();
        for (Direction direction : directions) {
            BlockPos checkPos = pos.relative(direction);
            BlockState checkState = level.getBlockState(checkPos);
            
            // If it's a vanilla vine, replace it with tainted white vine
            if (checkState.is(Blocks.VINE)) {
                // Copy the attachment properties from the vanilla vine
                BlockState newState = ModBlocks.TAINTED_WHITE_VINE.get().defaultBlockState();
                
                // Copy all directional attachments
                for (BooleanProperty property : PROPERTY_BY_DIRECTION.values()) {
                    if (checkState.hasProperty(property)) {
                        newState = newState.setValue(property, checkState.getValue(property));
                    }
                }
                
                level.setBlock(checkPos, newState, 3);
                break; // Only infect one vine per tick
            }
        }
    }
}

