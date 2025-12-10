package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.client.model.WitheredLatexPuddingModel;
import net.jerika.furmutage.entity.custom.WitheredLatexPuddingEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WitheredLatexPuddingRenderer extends MobRenderer<WitheredLatexPuddingEntity, WitheredLatexPuddingModel<WitheredLatexPuddingEntity>> {
    public WitheredLatexPuddingRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new WitheredLatexPuddingModel<>(pContext.bakeLayer(ModModelLayers.WITHERED_LATEX_PUDDING_LAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(WitheredLatexPuddingEntity pEntity) {
        return new ResourceLocation(furmutage.MOD_ID, "textures/entity/withered_latex_pudding.png");
    }

    @Override
    public void render(WitheredLatexPuddingEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        // Scale down to fit with latex mutant model proportions
        // Model geometry is large, scale it down to match entity size (2.4f width, 1.2f height)
        pMatrixStack.pushPose();
        pMatrixStack.scale(1.2f, 1.2f, 1.2f);
        pMatrixStack.translate(0, 0.0, 0); // Adjust vertical position to align with ground

        if(pEntity.isBaby()) {
            pMatrixStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        pMatrixStack.popPose();
    }
}
