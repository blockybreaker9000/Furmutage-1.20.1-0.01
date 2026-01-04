package net.jerika.furmutage.block.custom;

import net.jerika.furmutage.worldgen.tree.TaintedDarkTreeGrower;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Collections;
import java.util.List;

public class TaintedDarkSaplingBlock extends SaplingBlock {
    public TaintedDarkSaplingBlock(TaintedDarkTreeGrower treeGrower, BlockBehaviour.Properties properties) {
        super(treeGrower, properties);
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

