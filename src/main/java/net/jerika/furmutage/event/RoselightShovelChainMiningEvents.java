package net.jerika.furmutage.event;

import net.jerika.furmutage.block.custom.ModBlocks;
import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RoselightShovelChainMiningEvents {
    
    private static final int MAX_CHAIN_BLOCKS = 64; // Maximum blocks to chain mine
    private static final int MAX_DISTANCE = 32; // Maximum distance from initial block
    
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        var levelAccessor = event.getLevel();
        
        if (levelAccessor.isClientSide()) {
            return;
        }
        
        if (!(levelAccessor instanceof Level)) {
            return;
        }
        
        Level level = (Level) levelAccessor;
        
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getPlayer();
        
        ItemStack heldItem = player.getMainHandItem();
        
        // Check if player is holding the roselight shovel
        if (heldItem.getItem() != ModItems.ROSELIGHT_SHOVEL.get()) {
            return;
        }
        
        BlockState state = event.getState();
        Block block = state.getBlock();
        BlockPos pos = event.getPos();
        
        // Check if this is a white or dark latex block
        if (isWhiteLatexBlock(block) || isDarkLatexBlock(block)) {
            // Perform chain mining after the initial block is broken
            chainMineBlocks(level, pos, block, player, heldItem);
        }
    }
    
    private static boolean isWhiteLatexBlock(Block block) {
        return block == ModBlocks.TAINTED_WHITE_DIRT.get() ||
               block == ModBlocks.TAINTED_WHITE_GRASS.get() ||
               block == ModBlocks.TAINTED_WHITE_SAND.get() ||
               block == ModBlocks.TAINTED_WHITE_LOG.get() ||
               block == ModBlocks.STRIPPED_TAINTED_WHITE_LOG.get() ||
               block == ModBlocks.TAINTED_WHITE_PLANKS.get() ||
               block == ModBlocks.TAINTED_WHITE_SLAB.get() ||
               block == ModBlocks.TAINTED_WHITE_STAIRS.get();
    }
    
    private static boolean isDarkLatexBlock(Block block) {
        return block == ModBlocks.TAINTED_DARK_DIRT.get() ||
               block == ModBlocks.TAINTED_DARK_GRASS.get() ||
               block == ModBlocks.TAINTED_DARK_SAND.get() ||
               block == ModBlocks.TAINTED_DARK_LOG.get() ||
               block == ModBlocks.STRIPPED_TAINTED_DARK_LOG.get() ||
               block == ModBlocks.TAINTED_DARK_PLANKS.get() ||
               block == ModBlocks.TAINTED_DARK_SLAB.get() ||
               block == ModBlocks.TAINTED_DARK_STAIRS.get();
    }
    
    private static void chainMineBlocks(Level level, BlockPos startPos, Block targetBlock, Player player, ItemStack tool) {
        if (level.isClientSide()) {
            return;
        }
        
        Set<BlockPos> processed = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        
        // Add all 6 neighboring blocks to the queue
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = startPos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);
            Block neighborBlock = neighborState.getBlock();
            
            if (neighborBlock == targetBlock) {
                queue.offer(neighborPos);
                processed.add(neighborPos);
            }
        }
        
        int blocksBroken = 0;
        
        while (!queue.isEmpty() && blocksBroken < MAX_CHAIN_BLOCKS) {
            BlockPos currentPos = queue.poll();
            
            // Check distance from start
            if (currentPos.distSqr(startPos) > MAX_DISTANCE * MAX_DISTANCE) {
                continue;
            }
            
            BlockState currentState = level.getBlockState(currentPos);
            Block currentBlock = currentState.getBlock();
            
            // Only mine if it's the same block type
            if (currentBlock == targetBlock && !currentState.isAir()) {
                // Check if player can break this block
                float hardness = currentState.getDestroySpeed(level, currentPos);
                if (hardness >= 0.0F && ForgeHooks.isCorrectToolForDrops(currentState, player)) {
                    // Damage the tool
                    if (!player.getAbilities().instabuild) {
                        tool.hurtAndBreak(1, player, (entity) -> {
                            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                        });
                        
                        // Stop if tool is broken
                        if (tool.isEmpty()) {
                            break;
                        }
                    }
                    
                    // Break the block
                    level.destroyBlock(currentPos, !player.getAbilities().instabuild, player);
                    blocksBroken++;
                    
                    // Add neighboring blocks to queue
                    for (Direction direction : Direction.values()) {
                        BlockPos neighborPos = currentPos.relative(direction);
                        
                        if (!processed.contains(neighborPos)) {
                            BlockState neighborState = level.getBlockState(neighborPos);
                            Block neighborBlock = neighborState.getBlock();
                            
                            // Only add if it's the same block type
                            if (neighborBlock == targetBlock) {
                                queue.offer(neighborPos);
                                processed.add(neighborPos);
                            }
                        }
                    }
                }
            }
        }
    }
}
