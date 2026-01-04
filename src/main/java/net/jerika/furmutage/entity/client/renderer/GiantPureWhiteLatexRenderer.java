package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.jerika.furmutage.entity.client.model.GiantPureWhiteLatexModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.GiantPureWhiteLatexEntity;
import net.jerika.furmutage.furmutage;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class GiantPureWhiteLatexRenderer extends AdvancedHumanoidRenderer<GiantPureWhiteLatexEntity, GiantPureWhiteLatexModel<GiantPureWhiteLatexEntity>,
        ArmorLatexMaleWolfModel<GiantPureWhiteLatexEntity>> {
    public GiantPureWhiteLatexRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new GiantPureWhiteLatexModel<>(pContext.bakeLayer(ModModelLayers.GIANT_PURE_WHITE_LATEX_LAYER)),
                ArmorLatexMaleWolfModel.MODEL_SET, 0.8f);
    }

    @Override
    public ResourceLocation getTextureLocation(GiantPureWhiteLatexEntity pEntity) {
        return new ResourceLocation(furmutage.MOD_ID, "textures/entity/giant_pure_white_latex.png");
    }

    @Override
    public void render(GiantPureWhiteLatexEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        // Scale up for 6 blocks tall (the model is human-sized ~2 blocks, so scale 3x to get 6 blocks)
        pMatrixStack.pushPose();
        pMatrixStack.scale(3.0f, 3.0f, 3.0f);
        // Human models are positioned with feet at ground level (Y=24 in model space = Y=0 in world)
        // Scale happens from origin, so no translation needed - model should align correctly
        
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        pMatrixStack.popPose();
    }
}

