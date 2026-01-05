package net.jerika.furmutage.entity.client.renderer;

import net.jerika.furmutage.entity.custom.WhiteLatexDolphinEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.DolphinRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom renderer for white latex infected dolphin with custom texture.
 */
public class WhiteLatexDolphinRenderer extends DolphinRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(furmutage.MOD_ID, "textures/entity/white_latex_dolphin.png");

    public WhiteLatexDolphinRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(net.minecraft.world.entity.animal.Dolphin pEntity) {
        return TEXTURE;
    }
}

