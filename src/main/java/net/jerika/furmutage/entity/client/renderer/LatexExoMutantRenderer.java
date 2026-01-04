package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.jerika.furmutage.entity.client.model.LatexExoMutantModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.LatexExoMutantEntity;
import net.jerika.furmutage.furmutage;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LatexExoMutantRenderer extends AdvancedHumanoidRenderer<LatexExoMutantEntity, LatexExoMutantModel<LatexExoMutantEntity>, 
        ArmorLatexMaleWolfModel<LatexExoMutantEntity>> {
    public LatexExoMutantRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new LatexExoMutantModel<>(pContext.bakeLayer(ModModelLayers.LATEX_EXO_MUTANT_LAYER)),
                ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(LatexExoMutantEntity pEntity) {
        return new ResourceLocation(furmutage.MOD_ID, "textures/entity/latex_exo_mutant.png");
    }

    @Override
    public void render(LatexExoMutantEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        // Scale to fit entity size (0.6f width, 1.8f height)
        pMatrixStack.pushPose();
        pMatrixStack.scale(1.0f, 1.0f, 1.0f);
        pMatrixStack.translate(0, 0.0, 0);

        if(pEntity.isBaby()) {
            pMatrixStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        pMatrixStack.popPose();
    }
}

