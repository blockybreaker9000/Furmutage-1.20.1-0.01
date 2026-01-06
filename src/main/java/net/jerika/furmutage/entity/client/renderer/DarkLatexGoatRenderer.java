package net.jerika.furmutage.entity.client.renderer;

import net.jerika.furmutage.entity.custom.DarkLatexGoatEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GoatRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom renderer for dark latex infected goat with custom texture.
 */
public class DarkLatexGoatRenderer extends GoatRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(furmutage.MOD_ID, "textures/entity/dark_latex_goat.png");

    public DarkLatexGoatRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(net.minecraft.world.entity.animal.goat.Goat pEntity) {
        return TEXTURE;
    }
}

