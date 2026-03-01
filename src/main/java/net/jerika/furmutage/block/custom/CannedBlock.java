package net.jerika.furmutage.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

/**
 * Stackable canned food block (1–4 cans), waterloggable. Used for canned cat food, dog food, beans, ham.
 */
public class CannedBlock extends Block implements SimpleWaterloggedBlock {

    public static final IntegerProperty CANS = IntegerProperty.create("cans", 1, 4);
    public static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape ONE_AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 7.0D, 10.0D);
    private static final VoxelShape TWO_AABB = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 7.0D, 13.0D);
    private static final VoxelShape THREE_AABB = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 7.0D, 13.0D);
    private static final VoxelShape FOUR_AABB = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D);

    public CannedBlock() {
        super(BlockBehaviour.Properties.of()
                .offsetType(BlockBehaviour.OffsetType.XZ)
                .sound(SoundType.COPPER)
                .strength(0.5F)
                .dynamicShape());
        registerDefaultState(stateDefinition.any().setValue(CANS, 1).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CANS, WATERLOGGED);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return List.of(new ItemStack(this, state.getValue(CANS)));
    }

    private VoxelShape shapeForState(BlockState state) {
        return switch (state.getValue(CANS)) {
            case 1 -> ONE_AABB;
            case 2 -> TWO_AABB;
            case 3 -> THREE_AABB;
            case 4 -> FOUR_AABB;
            default -> ONE_AABB;
        };
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        Vec3 vec3 = state.getOffset(level, pos);
        return shapeForState(state).move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(level, pos);
        return shapeForState(state).move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return Block.canSupportCenter(level, pos.below(), Direction.UP);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return !context.isSecondaryUseActive() && context.getItemInHand().getItem() == this.asItem() && state.getValue(CANS) < 4 || super.canBeReplaced(state, context);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());
        if (blockstate.is(this)) {
            return blockstate.cycle(CANS);
        }
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        return defaultBlockState().setValue(WATERLOGGED, flag);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, otherState, level, pos, otherPos);
    }
}
