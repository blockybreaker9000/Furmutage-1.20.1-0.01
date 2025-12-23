package net.jerika.furmutage.entity.client.renderer;

import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom renderer for white latex infected pig with custom texture.
 */
public class WhiteLatexPigRenderer extends PigRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(furmutage.MOD_ID, "textures/entity/white_latex_pig.png");

    public WhiteLatexPigRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(net.minecraft.world.entity.animal.Pig pEntity) {
        return TEXTURE;
    }
}

