package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.client.model.TSCDroneModel;
import net.jerika.furmutage.entity.custom.TSCDroneEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TSCDroneRenderer extends MobRenderer<TSCDroneEntity, TSCDroneModel<TSCDroneEntity>> {
    public TSCDroneRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new TSCDroneModel<>(pContext.bakeLayer(ModModelLayers.TSC_DRONE_LAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(TSCDroneEntity pEntity) {
        return new ResourceLocation(furmutage.MOD_ID, "textures/entity/tsc_drone.png");
    }

    @Override
    public void render(TSCDroneEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.scale(0.8f, 0.8f, 0.8f);
        pMatrixStack.translate(0.0, 2.0, 0.0);
        pMatrixStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(180.0F));
        if(pEntity.isBaby()) {
            pMatrixStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        pMatrixStack.popPose();
    }
}

