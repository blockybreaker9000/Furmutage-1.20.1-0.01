package net.jerika.furmutage.item;

import net.jerika.furmutage.furmutage;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class EmptyTscBrandedWaterBottleItem extends Item {

    public EmptyTscBrandedWaterBottleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (!state.getFluidState().is(FluidTags.WATER) && !state.is(Blocks.WATER)) {
            return InteractionResult.PASS;
        }

        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }

        ItemStack held = context.getItemInHand();
        if (held.isEmpty() || !held.is(this)) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            Item untreated = ForgeRegistries.ITEMS.getValue(new ResourceLocation(furmutage.MOD_ID, "untreated_tsc_branded_water_bottle"));
            if (untreated == null || untreated == net.minecraft.world.item.Items.AIR) {
                return InteractionResult.PASS;
            }
            level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
            held.shrink(1);
            ItemStack filled = new ItemStack(untreated);
            if (held.isEmpty()) {
                player.setItemInHand(context.getHand(), filled);
            } else {
                if (!player.getInventory().add(filled)) {
                    player.drop(filled, false);
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
