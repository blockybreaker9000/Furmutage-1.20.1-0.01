package net.jerika.furmutage.mixins;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Makes gold pickaxe count as correct tool for diamond ore/block (so it gets drops and speed).
 * Makes only iron pickaxe count as correct tool for gold ore.
 */
@Mixin(ForgeHooks.class)
public class ForgeHooksGoldIronPickaxeMixin {

    @Inject(method = "isCorrectToolForDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)Z", at = @At("HEAD"), cancellable = true, remap = false)
    private static void furmutage$goldIronPickaxeToolCheck(BlockState state, Player player, CallbackInfoReturnable<Boolean> cir) {
        ItemStack tool = player.getMainHandItem();

        // Gold pickaxe is correct for diamond ore/block
        if (tool.is(Items.GOLDEN_PICKAXE)) {
            if (state.is(Blocks.DIAMOND_ORE) || state.is(Blocks.DEEPSLATE_DIAMOND_ORE) || state.is(Blocks.DIAMOND_BLOCK)) {
                cir.setReturnValue(true);
            }
            return;
        }

        // Gold ore: only iron pickaxe is correct
        if (state.is(Blocks.GOLD_ORE) || state.is(Blocks.DEEPSLATE_GOLD_ORE) || state.is(Blocks.NETHER_GOLD_ORE)) {
            if (!tool.is(Items.IRON_PICKAXE)) {
                cir.setReturnValue(false);
            }
        }
    }
}
