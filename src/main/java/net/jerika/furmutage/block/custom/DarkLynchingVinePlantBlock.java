package net.jerika.furmutage.block.custom;

import net.jerika.furmutage.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class DarkLynchingVinePlantBlock extends GrowingPlantBodyBlock {
    public static final BooleanProperty BERRIES = BlockStateProperties.BERRIES;
    private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    
    public DarkLynchingVinePlantBlock(BlockBehaviour.Properties properties) {
        super(properties, Direction.DOWN, SHAPE, false);
        this.registerDefaultState(this.stateDefinition.any().setValue(BERRIES, false));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BERRIES);
    }
    
    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) ModBlocks.DARK_LYNCHING_VINE.get();
    }
    
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Grow berries naturally on all blocks EXCEPT the bottom one
        if (!state.getValue(BERRIES)) {
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);
            boolean isBottomBlock = !belowState.is(ModBlocks.DARK_LYNCHING_VINE.get()) && 
                                   !belowState.is(ModBlocks.DARK_LYNCHING_VINE_PLANT.get());
            
            // Only grow berries if this is NOT the bottom block
            if (!isBottomBlock && random.nextInt(30) == 0) { // ~30% chance per random tick
                level.setBlock(pos, state.setValue(BERRIES, true), 2);
            }
        }
    }
    
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, 
                                 InteractionHand hand, BlockHitResult hit) {
        // Harvest berries (like glowberries)
        if (state.getValue(BERRIES)) {
            if (!level.isClientSide) {
                // Play harvest sound
                level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            }
            popResource(level, pos, new ItemStack(ModItems.PHAGE_BLUEBERRY.get(), 1 + level.random.nextInt(2)));
            level.setBlock(pos, state.setValue(BERRIES, false), 2);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        
        // Check if mined with shears - if so, drop the block itself (head block)
        if (builder.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.TOOL) != null) {
            ItemStack tool = builder.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.TOOL);
            if (tool != null && tool.is(Items.SHEARS)) {
                drops.add(new ItemStack(ModBlocks.DARK_LYNCHING_VINE.get().asItem()));
            }
        }
        
        // Drop berries if present
        if (state.getValue(BERRIES)) {
            drops.add(new ItemStack(ModItems.PHAGE_BLUEBERRY.get(), 1 + builder.getLevel().getRandom().nextInt(2)));
        }
        
        return drops;
    }
}

