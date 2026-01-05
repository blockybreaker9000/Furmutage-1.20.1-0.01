package net.jerika.furmutage.block.custom;

import net.jerika.furmutage.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;

import java.util.List;

public class RoselightCrystalShardBlock extends Block {
    private static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D);
    
    public RoselightCrystalShardBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    
    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }
    
    @Override
    public net.minecraft.world.level.block.RenderShape getRenderShape(BlockState state) {
        return net.minecraft.world.level.block.RenderShape.MODEL;
    }
    
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        
        // Check if mined with pickaxe - if so, drop 2-5 glass shards
        if (builder.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.TOOL) != null) {
            ItemStack tool = builder.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.TOOL);
            if (tool != null) {
                // Check if the tool is a pickaxe by checking common pickaxe items
                boolean isPickaxe = tool.is(Items.WOODEN_PICKAXE) ||
                                   tool.is(Items.STONE_PICKAXE) ||
                                   tool.is(Items.IRON_PICKAXE) ||
                                   tool.is(Items.GOLDEN_PICKAXE) ||
                                   tool.is(Items.DIAMOND_PICKAXE) ||
                                   tool.is(Items.NETHERITE_PICKAXE) ||
                                   tool.getItem() instanceof net.minecraft.world.item.PickaxeItem;
                
                if (isPickaxe) {
                    // Drop 2-5 glass shards (random amount between 2 and 5 inclusive)
                    int amount = 2 + builder.getLevel().getRandom().nextInt(4); // 2 + (0-3) = 2-5
                    drops.add(new ItemStack(ModItems.ROSELIGHT_GLASS_SHARD.get(), amount));
                }
            }
        }
        
        return drops;
    }
}

