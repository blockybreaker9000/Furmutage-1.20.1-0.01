package net.jerika.furmutage.event;

import net.jerika.furmutage.block.custom.ModBlocks;
import net.jerika.furmutage.furmutage;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles stripping tainted logs with an axe (right-click).
 * Vanilla/AxeItem strippables map may not include mod blocks in 1.20.1, so we handle it explicitly.
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TaintedLogStripEvents {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof AxeItem)) {
            return;
        }

        Level level = event.getLevel();
        if (level.isClientSide()) {
            return;
        }

        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        BlockState strippedState = null;
        if (state.is(ModBlocks.TAINTED_WHITE_LOG.get())) {
            strippedState = ModBlocks.STRIPPED_TAINTED_WHITE_LOG.get().defaultBlockState();
        } else if (state.is(ModBlocks.TAINTED_DARK_LOG.get())) {
            strippedState = ModBlocks.STRIPPED_TAINTED_DARK_LOG.get().defaultBlockState();
        }

        if (strippedState != null && state.hasProperty(BlockStateProperties.AXIS)) {
            strippedState = strippedState.setValue(BlockStateProperties.AXIS, state.getValue(BlockStateProperties.AXIS));
            level.setBlock(pos, strippedState, 11);
            level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            stack.hurtAndBreak(1, event.getEntity(), (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            event.setCanceled(true);
        }
    }
}
