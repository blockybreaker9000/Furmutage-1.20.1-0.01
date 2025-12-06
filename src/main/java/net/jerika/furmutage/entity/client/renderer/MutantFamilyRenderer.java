package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.jerika.furmutage.entity.client.MutantFamilyModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.LatexMutantFamilyEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MutantFamilyRenderer extends MobRenderer<LatexMutantFamilyEntity, MutantFamilyModel<LatexMutantFamilyEntity>> {
    public MutantFamilyRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new MutantFamilyModel<>(pContext.bakeLayer(ModModelLayers.MUTANT_FAMILY_LAYER)), 0.02f);
    }

    @Override
    public ResourceLocation getTextureLocation(LatexMutantFamilyEntity pEntity) {
        return new ResourceLocation(furmutage.MOD_ID, "textures/entity/mutantfamily.png");
    }

    @Override
    public void render(LatexMutantFamilyEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pMatrixStack.scale(1.5f, 1.5f, 1.5f);
        }


        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
