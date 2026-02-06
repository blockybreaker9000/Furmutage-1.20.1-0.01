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
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * When the player breaks foliage, flowers, or planted mushrooms with the roselight hoe,
 * vein-mines connected blocks of the same type (up to a limit).
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RoselightHoeVeinMiningEvents {

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
        if (heldItem.getItem() != ModItems.ROSELIGHT_HOE.get()) {
            return;
        }

        BlockState state = event.getState();
        Block block = state.getBlock();
        BlockPos pos = event.getPos();

        if (!isFoliageFlowerOrPlantedMushroom(block)) {
            return;
        }

        veinMineBlocks(level, pos, state, block, player, heldItem);
    }

    /**
     * True for foliage (grass, fern, vines, leaves, tall grass), flowers, and planted mushrooms.
     */
    private static boolean isFoliageFlowerOrPlantedMushroom(Block block) {
        return block instanceof net.minecraft.world.level.block.BushBlock
                || block instanceof VineBlock
                || block instanceof LeavesBlock
                || block instanceof DoublePlantBlock;
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
            if (neighborBlock == targetBlock && isFoliageFlowerOrPlantedMushroom(neighborBlock)) {
                queue.offer(neighborPos);
                processed.add(neighborPos);
            }
        }

        // For double plants, also queue the other half so we vein-mine both parts together
        if (targetBlock instanceof DoublePlantBlock && startState.hasProperty(DoublePlantBlock.HALF)) {
            BlockPos otherHalf = startState.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER
                    ? startPos.above()
                    : startPos.below();
            if (!processed.contains(otherHalf) && level.getBlockState(otherHalf).getBlock() == targetBlock) {
                queue.offer(otherHalf);
                processed.add(otherHalf);
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

            float hardness = currentState.getDestroySpeed(level, currentPos);
            if (hardness < 0.0F) {
                continue;
            }
            if (!ForgeHooks.isCorrectToolForDrops(currentState, player)) {
                continue;
            }

            if (!player.getAbilities().instabuild) {
                tool.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                if (tool.isEmpty()) {
                    break;
                }
            }

            // For double plants, break the other half first so it doesn't drop as item
            if (currentBlock instanceof DoublePlantBlock && currentState.hasProperty(DoublePlantBlock.HALF)) {
                BlockPos otherHalf = currentState.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER
                        ? currentPos.above()
                        : currentPos.below();
                BlockState otherState = level.getBlockState(otherHalf);
                if (otherState.getBlock() == targetBlock) {
                    level.destroyBlock(otherHalf, !player.getAbilities().instabuild, player);
                    processed.add(otherHalf);
                    blocksBroken++;
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
                if (neighborBlock == targetBlock && isFoliageFlowerOrPlantedMushroom(neighborBlock)) {
                    queue.offer(neighborPos);
                    processed.add(neighborPos);
                }
            }
        }
    }
}
