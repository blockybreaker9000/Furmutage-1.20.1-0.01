package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.jerika.furmutage.entity.client.model.BacteriaModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.LatexBacteriaEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class LatexBacteriaRenderer extends MobRenderer<LatexBacteriaEntity, BacteriaModel<LatexBacteriaEntity>> {
    public LatexBacteriaRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BacteriaModel<>(pContext.bakeLayer(ModModelLayers.BACTERIA_LAYER)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(LatexBacteriaEntity pEntity) {
        return new ResourceLocation(furmutage.MOD_ID, "textures/entity/latex_bacteria.png");
    }

    @Override
    public void render(LatexBacteriaEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}

