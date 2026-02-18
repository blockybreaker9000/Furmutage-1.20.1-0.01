package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.jerika.furmutage.entity.client.model.DarkLatexChargerMutantModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.DarkLatexChargerMutantEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DarkLatexChargerMutantRenderer extends MobRenderer<DarkLatexChargerMutantEntity, DarkLatexChargerMutantModel> {
    public DarkLatexChargerMutantRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new DarkLatexChargerMutantModel(pContext.bakeLayer(ModModelLayers.DARK_LATEX_CHARGER_MUTANT_LAYER)), 1.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(DarkLatexChargerMutantEntity pEntity) {
        return new ResourceLocation(furmutage.MOD_ID, "textures/entity/dark_latex_charger_mutant.png");
    }

    @Override
    public void render(DarkLatexChargerMutantEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.scale(1.2f, 1.2f, 1.2f);
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        pMatrixStack.popPose();
    }
}
