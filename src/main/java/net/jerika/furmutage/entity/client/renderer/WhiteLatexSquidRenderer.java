package net.jerika.furmutage.entity.client.renderer;

import net.jerika.furmutage.entity.custom.WhiteLatexSquidEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SquidRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom renderer for white latex infected squid with custom texture.
 */
public class WhiteLatexSquidRenderer extends SquidRenderer<WhiteLatexSquidEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(furmutage.MOD_ID, "textures/entity/white_latex_squid.png");

    public WhiteLatexSquidRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SquidModel<>(pContext.bakeLayer(ModelLayers.SQUID)));
    }

    @Override
    public ResourceLocation getTextureLocation(WhiteLatexSquidEntity pEntity) {
        return TEXTURE;
    }
}

