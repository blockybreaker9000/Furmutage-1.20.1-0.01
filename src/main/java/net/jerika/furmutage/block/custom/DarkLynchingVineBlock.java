package net.jerika.furmutage.block.custom;

import net.jerika.furmutage.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class DarkLynchingVineBlock extends GrowingPlantHeadBlock {
    public static final BooleanProperty BERRIES = BlockStateProperties.BERRIES;
    private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    
    public DarkLynchingVineBlock(BlockBehaviour.Properties properties) {
        super(properties, Direction.DOWN, SHAPE, false, 0.1D);
        this.registerDefaultState(this.stateDefinition.any().setValue(BERRIES, false));
    }
    
    @Override
    protected void createBlockStateDefinition(net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BERRIES);
    }
    
    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
        return 1;
    }
    
    @Override
    protected boolean canGrowInto(BlockState state) {
        return state.isAir();
    }
    
    @Override
    protected Block getBodyBlock() {
        return ModBlocks.DARK_LYNCHING_VINE_PLANT.get();
    }
    
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        
        // Check if mined with shears - if so, drop the block itself
        if (builder.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.TOOL) != null) {
            ItemStack tool = builder.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.TOOL);
            if (tool != null && tool.is(Items.SHEARS)) {
                drops.add(new ItemStack(this.asItem()));
            }
        }
        
        // Drop berries if present
        if (state.getValue(BERRIES)) {
            drops.add(new ItemStack(ModItems.PHAGE_BLUEBERRY.get(), 1 + builder.getLevel().getRandom().nextInt(2)));
        }
        return drops;
    }
    
    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(ModBlocks.DARK_LYNCHING_VINE.get().asItem());
    }
}

