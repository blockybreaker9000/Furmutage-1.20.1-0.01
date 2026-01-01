package net.jerika.furmutage.block.custom;

import net.jerika.furmutage.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.Collections;
import java.util.List;

/**
 * TSC Plastic Waste Ore block that drops TSC Plastic Waste Clump items.
 * Only spawns in stone (not deepslate).
 */
public class TSCPlasticWasteOreBlock extends Block {
    public TSCPlasticWasteOreBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return Collections.singletonList(new ItemStack(ModItems.TSC_PLASTIC_WASTE_CLUMP.get()));
    }
}

