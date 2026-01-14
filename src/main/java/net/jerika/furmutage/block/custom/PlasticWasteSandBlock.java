package net.jerika.furmutage.block.custom;

import net.jerika.furmutage.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class PlasticWasteSandBlock extends SandBlock {
    public PlasticWasteSandBlock(BlockBehaviour.Properties properties) {
        // Use a neutral sand color and custom sound
        super(0xC2C2C2, properties.sound(SoundType.SAND));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);

        if (held.is(Items.BRUSH)) {
            if (!level.isClientSide) {
                RandomSource random = level.random;
                int count = 1 + random.nextInt(2); // 1-2 plastic waste

                ItemStack drop = new ItemStack(ModItems.PLASTIC_WASTE.get(), count);
                popResource(level, pos, drop);

                // Turn back into normal sand after brushing
                level.setBlock(pos, net.minecraft.world.level.block.Blocks.SAND.defaultBlockState(), 3);

                // Play brush sound and damage brush a bit
                level.playSound(null, pos, SoundEvents.BRUSH_GENERIC, SoundSource.BLOCKS, 1.0F, 1.0F);
                held.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.use(state, level, pos, player, hand, hit);
    }
}

