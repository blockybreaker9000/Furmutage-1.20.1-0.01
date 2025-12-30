package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.jerika.furmutage.entity.client.model.LatexBomberMutantModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.LatexMutantBomberEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class LatexMutantBomberRenderer extends MobRenderer<LatexMutantBomberEntity, LatexBomberMutantModel<LatexMutantBomberEntity>> {
    public LatexMutantBomberRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new LatexBomberMutantModel<>(pContext.bakeLayer(ModModelLayers.LATEX_BOMBER_MUTANT_LAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(LatexMutantBomberEntity pEntity) {
        return new ResourceLocation(furmutage.MOD_ID, "textures/entity/latex_bomber_mutant.png");
    }

    @Override
    public void render(LatexMutantBomberEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pMatrixStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}

