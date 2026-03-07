package net.jerika.furmutage.event;

import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.config.ModCommonConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Renders a message above the hotbar when the player looks at ores that use the mod's
 * progression (iron → TSC Emergency, gold → Roselight, diamond → Gold pickaxe).
 */
@Mod.EventBusSubscriber(modid = furmutage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class OreToolHintOverlay {

    private static final int HOTBAR_OFFSET_FROM_BOTTOM = 48;

    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            return;
        }
        HitResult hit = mc.hitResult;
        if (hit == null || hit.getType() != HitResult.Type.BLOCK) {
            return;
        }
        if (!ModCommonConfig.ENABLE_ORE_PROGRESSION.get()) {
            return;
        }
        BlockHitResult blockHit = (BlockHitResult) hit;
        BlockState state = mc.level.getBlockState(blockHit.getBlockPos());
        Component message = getOreProgressionHint(state);
        if (message == null) {
            return;
        }
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Font font = mc.font;
        int width = event.getWindow().getGuiScaledWidth();
        int height = event.getWindow().getGuiScaledHeight();
        int textWidth = font.width(message);
        int x = (width - textWidth) / 2;
        int y = height - HOTBAR_OFFSET_FROM_BOTTOM;
        guiGraphics.drawString(font, message, x, y, 0xFFFFFF, true);
    }

    /**
     * Returns the progression hint for ores that require a specific pickaxe tier, or null.
     */
    private static Component getOreProgressionHint(BlockState state) {
        // Iron ore: only TSC Emergency pickaxe can break (stone can't)
        if (state.is(Blocks.IRON_ORE) || state.is(Blocks.DEEPSLATE_IRON_ORE)) {
            return Component.translatable("furmutage.tool_hint.iron_ore");
        }
        // Gold ore: need Roselight pickaxe (iron can't)
        if (state.is(Blocks.GOLD_ORE) || state.is(Blocks.DEEPSLATE_GOLD_ORE) || state.is(Blocks.NETHER_GOLD_ORE)) {
            return Component.translatable("furmutage.tool_hint.gold_ore");
        }
        // Diamond ore / block: need gold pickaxe (iron and roselight glass can't)
        if (state.is(Blocks.DIAMOND_ORE) || state.is(Blocks.DEEPSLATE_DIAMOND_ORE) || state.is(Blocks.DIAMOND_BLOCK)) {
            return Component.translatable("furmutage.tool_hint.diamond_ore");
        }
        return null;
    }
}
