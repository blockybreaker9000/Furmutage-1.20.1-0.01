package net.jerika.furmutage.entity.client.renderer;

import net.jerika.furmutage.entity.custom.DarkLatexSquidEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SquidRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom renderer for dark latex infected squid with custom texture.
 */
public class DarkLatexSquidRenderer extends SquidRenderer<DarkLatexSquidEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(furmutage.MOD_ID, "textures/entity/dark_latex_squid.png");

    public DarkLatexSquidRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SquidModel<>(pContext.bakeLayer(ModelLayers.SQUID)));
    }

    @Override
    public ResourceLocation getTextureLocation(DarkLatexSquidEntity pEntity) {
        return TEXTURE;
    }
}

