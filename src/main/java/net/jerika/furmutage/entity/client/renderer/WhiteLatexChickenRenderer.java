package net.jerika.furmutage.entity.client.renderer;

import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom renderer for white latex infected chicken with custom texture.
 */
public class WhiteLatexChickenRenderer extends ChickenRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(furmutage.MOD_ID, "textures/entity/white_latex_chicken.png");

    public WhiteLatexChickenRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(net.minecraft.world.entity.animal.Chicken pEntity) {
        return TEXTURE;
    }
}

