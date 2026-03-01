package net.jerika.furmutage.mixins;

import net.jerika.furmutage.util.TaintedBlocks;
import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Allows tryCover to place latex cover on tainted blocks (white latex on tainted white, dark on tainted dark)
 * by treating them as not full-block for the purpose of the cover check.
 */
@Mixin(value = AbstractLatexBlock.class, remap = false)
public abstract class AbstractLatexBlockMixin {

    @Unique
    private static final ThreadLocal<LatexType> furmutage$tryCoverType = new ThreadLocal<>();

    @Inject(method = "tryCover", at = @At("HEAD"), remap = false)
    private static void captureTryCoverType(Level level, BlockPos relative, LatexType type, CallbackInfoReturnable<Boolean> cir) {
        furmutage$tryCoverType.set(type);
    }

    @Inject(method = "tryCover", at = @At("RETURN"), remap = false)
    private static void clearTryCoverType(Level level, BlockPos relative, LatexType type, CallbackInfoReturnable<Boolean> cir) {
        furmutage$tryCoverType.remove();
    }

    @Redirect(method = "tryCover", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isCollisionShapeFullBlock(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z"), remap = true, require = 0)
    private static boolean allowCoverOnTaintedBlocks(BlockState state, BlockGetter level, BlockPos pos) {
        LatexType type = furmutage$tryCoverType.get();
        if (type != null) {
            if (type == ChangedLatexTypes.WHITE_LATEX.get() && TaintedBlocks.isTaintedWhite(state)) return false;
            if (type == ChangedLatexTypes.DARK_LATEX.get() && TaintedBlocks.isTaintedDark(state)) return false;
        }
        return state.isCollisionShapeFullBlock(level, pos);
    }
}
