package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EmptyTscBottleWaterFillEvents {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack held = event.getItemStack();
        if (held.isEmpty() || !held.is(ModItems.EMPTY_TSC_BRANDED_WATER_BOTTLE.get())) {
            return;
        }

        Level level = event.getLevel();
        BlockHitResult hitResult = event.getHitVec();
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);

        // Check clicked block for water
        boolean isWater = state.getFluidState().is(FluidTags.WATER)
                || state.is(Blocks.WATER)
                || state.is(Blocks.WATER_CAULDRON);

        // Also check the block we're clicking "into" (e.g. when clicking on edge of water)
        if (!isWater && hitResult.getDirection() != null) {
            BlockPos adjacentPos = pos.relative(hitResult.getDirection());
            BlockState adjacentState = level.getBlockState(adjacentPos);
            isWater = adjacentState.getFluidState().is(FluidTags.WATER)
                    || adjacentState.is(Blocks.WATER);
        }

        if (!isWater) {
            return;
        }

        if (level.isClientSide) {
            event.setCanceled(true);
            return; // Server will do the actual conversion
        }

        var untreated = ForgeRegistries.ITEMS.getValue(new ResourceLocation(furmutage.MOD_ID, "untreated_tsc_branded_water_bottle"));
        if (untreated == null || untreated == net.minecraft.world.item.Items.AIR) {
            return;
        }

        level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
        held.shrink(1);
        ItemStack filled = new ItemStack(untreated);
        if (held.isEmpty()) {
            player.setItemInHand(event.getHand(), filled);
        } else {
            if (!player.getInventory().add(filled)) {
                player.drop(filled, false);
            }
        }
        event.setCanceled(true);
    }
}
