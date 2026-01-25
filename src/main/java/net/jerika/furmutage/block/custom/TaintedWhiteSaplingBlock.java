package net.jerika.furmutage.block.custom;

import net.jerika.furmutage.worldgen.tree.TaintedWhiteTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Collections;
import java.util.List;

public class TaintedWhiteSaplingBlock extends SaplingBlock {
    public TaintedWhiteSaplingBlock(TaintedWhiteTreeGrower treeGrower, BlockBehaviour.Properties properties) {
        super(treeGrower, properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        // Can be placed on tainted white blocks and vanilla blocks
        return state.is(ModBlocks.TAINTED_WHITE_GRASS.get()) ||
               state.is(ModBlocks.TAINTED_WHITE_DIRT.get()) ||
               state.is(ModBlocks.TAINTED_WHITE_SAND.get()) ||
               state.is(Blocks.GRASS_BLOCK) ||
               state.is(Blocks.DIRT) ||
               state.is(Blocks.COARSE_DIRT) ||
               state.is(Blocks.PODZOL) ||
               state.is(Blocks.SAND) ||
               state.is(Blocks.RED_SAND);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        // Only drop sapling if it was broken by a player
        // Check if the LootParams contains a player entity (this means a player broke it)
        var entity = builder.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if (entity instanceof Player) {
            return super.getDrops(state, builder);
        }
        // If broken by non-player (piston, explosion, decay, etc.), drop nothing
        return Collections.emptyList();
    }
}

