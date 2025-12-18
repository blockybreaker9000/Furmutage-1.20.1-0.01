package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.client.model.TSCDroneModel;
import net.jerika.furmutage.entity.custom.TSCDroneBossEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Boss renderer reusing the regular TSC drone model, but scaled up.
 */
public class TSCDroneBossRenderer extends MobRenderer<TSCDroneBossEntity, TSCDroneModel<TSCDroneBossEntity>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(furmutage.MOD_ID, "textures/entity/tsc_drone.png");

    public TSCDroneBossRenderer(EntityRendererProvider.Context context) {
        super(context, new TSCDroneModel<>(context.bakeLayer(ModModelLayers.TSC_DRONE_LAYER)), 1.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(TSCDroneBossEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(TSCDroneBossEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        // Scale up significantly so it feels like a boss version of the drone
        poseStack.scale(2.5f, 2.5f, 2.5f);
        poseStack.translate(0.0, 1.5, 0.0);
        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(180.0F));

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }
}


