package net.jerika.furmutage.item.custom;

import net.jerika.furmutage.block.custom.ModBlocks;
import net.jerika.furmutage.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class WhiteTaintedSeedsItem extends Item {
    public WhiteTaintedSeedsItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (state.is(Blocks.GRASS_BLOCK)) {
            if (!level.isClientSide) {
                level.setBlock(pos, ModBlocks.TAINTED_WHITE_GRASS.get().defaultBlockState(), 3);
                level.playSound(null, pos, ModSounds.TAINTED_HORROR_GRASS_PLACE.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                context.getItemInHand().shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }
}