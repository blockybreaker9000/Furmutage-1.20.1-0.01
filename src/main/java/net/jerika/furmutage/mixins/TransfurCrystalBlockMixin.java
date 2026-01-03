package net.jerika.furmutage.mixins;

import net.jerika.furmutage.block.custom.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to allow Changed mod crystals to be placed on dark tainted grass.
 * This targets the TransfurCrystalBlock class from the Changed mod.
 */
@Mixin(targets = "net.ltxprogrammer.changed.block.TransfurCrystalBlock")
public class TransfurCrystalBlockMixin {
    
    /**
     * Allows crystals to be placed on dark tainted grass blocks.
     */
    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true, remap = false)
    private void mayPlaceOnDarkTaintedGrass(BlockState otherBlock, BlockGetter level, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        // Allow placement on dark tainted grass and other dark tainted blocks
        if (otherBlock.is(ModBlocks.TAINTED_DARK_GRASS.get()) ||
            otherBlock.is(ModBlocks.TAINTED_DARK_DIRT.get()) ||
            otherBlock.is(ModBlocks.TAINTED_DARK_SAND.get()) ||
            otherBlock.is(ModBlocks.TAINTED_DARK_LOG.get()) ||
            otherBlock.is(ModBlocks.TAINTED_DARK_PLANKS.get())) {
            cir.setReturnValue(true);
        }
    }
    
    /**
     * Allows crystals to survive on dark tainted grass blocks.
     */
    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true, remap = false)
    private void canSurviveOnDarkTaintedGrass(BlockState blockState, net.minecraft.world.level.LevelReader level, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        // Check if the block below is a dark tainted block
        BlockPos belowPos = blockPos.below();
        BlockState belowState = level.getBlockState(belowPos);
        
        if (belowState.is(ModBlocks.TAINTED_DARK_GRASS.get()) ||
            belowState.is(ModBlocks.TAINTED_DARK_DIRT.get()) ||
            belowState.is(ModBlocks.TAINTED_DARK_SAND.get()) ||
            belowState.is(ModBlocks.TAINTED_DARK_LOG.get()) ||
            belowState.is(ModBlocks.TAINTED_DARK_PLANKS.get())) {
            cir.setReturnValue(true);
        }
    }
}

