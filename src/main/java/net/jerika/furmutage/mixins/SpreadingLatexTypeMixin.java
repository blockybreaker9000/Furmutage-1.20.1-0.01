package net.jerika.furmutage.mixins;

import net.jerika.furmutage.util.TaintedBlocks;
import net.ltxprogrammer.changed.entity.latex.SpreadingLatexType;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Allows white latex cover to live on tainted white blocks and dark latex on tainted dark blocks:
 * spread onto them (even full blocks), not decay when on them, set faces from same-type neighbors.
 */
@Mixin(value = SpreadingLatexType.class, remap = false)
public abstract class SpreadingLatexTypeMixin {

    @Redirect(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isCollisionShapeFullBlock(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z"), remap = true)
    private boolean allowSpreadOntoTaintedFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        SpreadingLatexType self = (SpreadingLatexType) (Object) this;
        if (self instanceof SpreadingLatexType.WhiteLatex && TaintedBlocks.isTaintedWhite(state)) return false;
        if (self instanceof SpreadingLatexType.DarkLatex && TaintedBlocks.isTaintedDark(state)) return false;
        return state.isCollisionShapeFullBlock(level, pos);
    }

    @Redirect(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/entity/latex/SpreadingLatexType;canExistOnSurface(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z"), remap = false, require = 0)
    private static boolean allowTaintedAsSupport(BlockGetter level, BlockPos neighborPos, BlockState neighborState, BlockPos blockPos, BlockState blockState, Direction surfaceNormal) {
        if (TaintedBlocks.isTaintedWhite(neighborState) || TaintedBlocks.isTaintedDark(neighborState)) return true;
        return SpreadingLatexType.canExistOnSurface(level, neighborPos, neighborState, blockPos, blockState, surfaceNormal);
    }

    @Inject(method = "shouldDecay", at = @At("HEAD"), cancellable = true, remap = false)
    private void noDecayOnTaintedSameType(net.ltxprogrammer.changed.world.LatexCoverState state, LevelReader level, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockAt = level.getBlockState(blockPos);
        SpreadingLatexType self = (SpreadingLatexType) (Object) this;
        if (self instanceof SpreadingLatexType.WhiteLatex && TaintedBlocks.isTaintedWhite(blockAt)) {
            cir.setReturnValue(false);
            cir.cancel();
        } else if (self instanceof SpreadingLatexType.DarkLatex && TaintedBlocks.isTaintedDark(blockAt)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Redirect(method = "spreadState", at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/entity/latex/SpreadingLatexType;canExistOnSurface(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z"), remap = false, require = 0)
    private boolean allowTaintedFaceForSpread(BlockGetter level, BlockPos neighborPos, BlockState neighborState, BlockPos blockPos, BlockState blockAtPos, Direction surfaceNormal) {
        if (level instanceof LevelReader lr) {
            boolean whiteOnWhite = (Object) this instanceof SpreadingLatexType.WhiteLatex && TaintedBlocks.isTaintedWhite(blockAtPos) && (TaintedBlocks.isTaintedWhite(neighborState) || neighborHasLatexType(lr, neighborPos, true));
            boolean darkOnDark = (Object) this instanceof SpreadingLatexType.DarkLatex && TaintedBlocks.isTaintedDark(blockAtPos) && (TaintedBlocks.isTaintedDark(neighborState) || neighborHasLatexType(lr, neighborPos, false));
            if (whiteOnWhite || darkOnDark) return true;
        }
        return SpreadingLatexType.canExistOnSurface(level, neighborPos, neighborState, blockPos, blockAtPos, surfaceNormal);
    }

    private static boolean neighborHasLatexType(LevelReader level, BlockPos neighborPos, boolean white) {
        if (level.getBlockState(neighborPos).getBlock() instanceof net.ltxprogrammer.changed.block.LatexCoveringSource source) {
            var cover = source.getLatexCoverState(level.getBlockState(neighborPos), neighborPos);
            if (white) return cover.getType() == ChangedLatexTypes.WHITE_LATEX.get();
            return cover.getType() == ChangedLatexTypes.DARK_LATEX.get();
        }
        LatexCoverState cover = LatexCoverState.getAt(level, neighborPos);
        if (white) return cover.is(ChangedLatexTypes.WHITE_LATEX.get());
        return cover.is(ChangedLatexTypes.DARK_LATEX.get());
    }
}
