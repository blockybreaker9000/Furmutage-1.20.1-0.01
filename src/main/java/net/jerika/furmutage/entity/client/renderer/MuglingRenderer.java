package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.jerika.furmutage.entity.client.model.MuglingModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.MuglingEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MuglingRenderer extends MobRenderer<MuglingEntity, MuglingModel<MuglingEntity>> {
    public MuglingRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new MuglingModel<>(pContext.bakeLayer(ModModelLayers.MUGLING_LAYER)), 0.1f);
    }

    @Override
    public ResourceLocation getTextureLocation(MuglingEntity pEntity) {
        return new ResourceLocation(furmutage.MOD_ID, "textures/entity/mugling.png");
    }

    @Override
    public void render(MuglingEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pMatrixStack.scale(0.5f, 0.5f, 0.5f);
        }


        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
