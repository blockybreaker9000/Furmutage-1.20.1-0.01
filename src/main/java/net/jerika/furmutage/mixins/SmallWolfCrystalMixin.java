package net.jerika.furmutage.mixins;

import net.jerika.furmutage.block.custom.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to allow Changed mod wolf_crystal_small block to be placed on tainted_dark_grass.
 */
@Mixin(targets = "net.ltxprogrammer.changed.block.SmallWolfCrystal")
public class SmallWolfCrystalMixin {

    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true, remap = false)
    private void mayPlaceOnTaintedDarkGrass(BlockState otherBlock, BlockGetter level, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (otherBlock.is(ModBlocks.TAINTED_DARK_GRASS.get()) ||
            otherBlock.is(ModBlocks.TAINTED_DARK_DIRT.get()) ||
            otherBlock.is(ModBlocks.TAINTED_DARK_SAND.get()) ||
            otherBlock.is(ModBlocks.TAINTED_DARK_LOG.get()) ||
            otherBlock.is(ModBlocks.TAINTED_DARK_PLANKS.get())) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true, remap = false)
    private void canSurviveOnTaintedDarkGrass(BlockState blockState, LevelReader level, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        BlockState belowState = level.getBlockState(blockPos.below());
        if (belowState.is(ModBlocks.TAINTED_DARK_GRASS.get()) ||
            belowState.is(ModBlocks.TAINTED_DARK_DIRT.get()) ||
            belowState.is(ModBlocks.TAINTED_DARK_SAND.get()) ||
            belowState.is(ModBlocks.TAINTED_DARK_LOG.get()) ||
            belowState.is(ModBlocks.TAINTED_DARK_PLANKS.get())) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
