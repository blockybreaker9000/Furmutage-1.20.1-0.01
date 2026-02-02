package net.jerika.furmutage.block.custom;

import net.jerika.furmutage.block.entity.EugenicsSmelteryBlockEntity;
import net.jerika.furmutage.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class EugenicsSmelteryBlock extends AbstractFurnaceBlock {

    public EugenicsSmelteryBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EugenicsSmelteryBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, ModBlockEntities.EUGENICS_SMELTERY_BLOCK_ENTITY.get());
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof EugenicsSmelteryBlockEntity smelteryBlockEntity) {
            smelteryBlockEntity.openMenu(player);
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }
}
