package net.jerika.furmutage.event;

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

/**
 * Roselight glass pickaxe and glass shovel vein-mine connected blocks of the same type
 * when breaking a block (same logic for both tools, up to 64 blocks).
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RoselightGlassToolVeinMiningEvents {

    private static final int MAX_VEIN_BLOCKS = 64;
    private static final int MAX_DISTANCE = 32;

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
        boolean isGlassPickaxe = heldItem.is(ModItems.ROSELIGHT_GLASS_PICKAXE.get());
        boolean isGlassShovel = heldItem.is(ModItems.ROSELIGHT_GLASS_SHOVEL.get());
        if (!isGlassPickaxe && !isGlassShovel) {
            return;
        }

        BlockState state = event.getState();
        Block block = state.getBlock();
        BlockPos pos = event.getPos();

        if (!ForgeHooks.isCorrectToolForDrops(state, player)) {
            return;
        }

        veinMineBlocks(level, pos, state, block, player, heldItem);
    }

    private static void veinMineBlocks(Level level, BlockPos startPos, BlockState startState, Block targetBlock, Player player, ItemStack tool) {
        if (level.isClientSide()) {
            return;
        }

        Set<BlockPos> processed = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();

        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = startPos.relative(direction);
            if (processed.contains(neighborPos)) {
                continue;
            }
            BlockState neighborState = level.getBlockState(neighborPos);
            Block neighborBlock = neighborState.getBlock();
            if (neighborBlock == targetBlock && ForgeHooks.isCorrectToolForDrops(neighborState, player)) {
                queue.offer(neighborPos);
                processed.add(neighborPos);
            }
        }

        int blocksBroken = 0;

        while (!queue.isEmpty() && blocksBroken < MAX_VEIN_BLOCKS) {
            BlockPos currentPos = queue.poll();

            if (currentPos.distSqr(startPos) > MAX_DISTANCE * MAX_DISTANCE) {
                continue;
            }

            BlockState currentState = level.getBlockState(currentPos);
            Block currentBlock = currentState.getBlock();

            if (currentBlock != targetBlock || currentState.isAir()) {
                continue;
            }
            if (!ForgeHooks.isCorrectToolForDrops(currentState, player)) {
                continue;
            }
            float hardness = currentState.getDestroySpeed(level, currentPos);
            if (hardness < 0.0F) {
                continue;
            }

            if (!player.getAbilities().instabuild) {
                tool.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                if (tool.isEmpty()) {
                    break;
                }
            }

            level.destroyBlock(currentPos, !player.getAbilities().instabuild, player);
            blocksBroken++;

            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = currentPos.relative(direction);
                if (processed.contains(neighborPos)) {
                    continue;
                }
                BlockState neighborState = level.getBlockState(neighborPos);
                Block neighborBlock = neighborState.getBlock();
                if (neighborBlock == targetBlock && ForgeHooks.isCorrectToolForDrops(neighborState, player)) {
                    queue.offer(neighborPos);
                    processed.add(neighborPos);
                }
            }
        }
    }
}
