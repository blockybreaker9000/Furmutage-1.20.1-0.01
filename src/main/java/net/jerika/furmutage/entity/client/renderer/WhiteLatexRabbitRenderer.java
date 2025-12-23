package net.jerika.furmutage.entity.client.renderer;

import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RabbitRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom renderer for white latex infected rabbit with custom texture.
 */
public class WhiteLatexRabbitRenderer extends RabbitRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(furmutage.MOD_ID, "textures/entity/white_latex_rabbit.png");

    public WhiteLatexRabbitRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(net.minecraft.world.entity.animal.Rabbit pEntity) {
        return TEXTURE;
    }
}

