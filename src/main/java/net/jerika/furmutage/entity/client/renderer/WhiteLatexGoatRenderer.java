package net.jerika.furmutage.entity.client.renderer;

import net.jerika.furmutage.entity.custom.WhiteLatexGoatEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GoatRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom renderer for white latex infected goat with custom texture.
 */
public class WhiteLatexGoatRenderer extends GoatRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(furmutage.MOD_ID, "textures/entity/white_latex_goat.png");

    public WhiteLatexGoatRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(net.minecraft.world.entity.animal.goat.Goat pEntity) {
        return TEXTURE;
    }
}

