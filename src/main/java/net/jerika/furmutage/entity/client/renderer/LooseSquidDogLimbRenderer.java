package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.jerika.furmutage.entity.client.model.LooseSquidDogLimbModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.LooseSquidDogLimbEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class LooseSquidDogLimbRenderer extends MobRenderer<LooseSquidDogLimbEntity, LooseSquidDogLimbModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(furmutage.MOD_ID, "textures/entity/loose_squid_dog_limb.png");

    public LooseSquidDogLimbRenderer(EntityRendererProvider.Context context) {
        super(context, new LooseSquidDogLimbModel(context.bakeLayer(ModModelLayers.LOOSE_SQUID_DOG_LIMB_LAYER)), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(LooseSquidDogLimbEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(LooseSquidDogLimbEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(1.0F, 1.0F, 1.0F);
        // Model was built with +X as front; rotate 90° so it faces -Z (Minecraft forward)
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        if (entity.isHanging()) {
            poseStack.translate(0.0, -1.5, 0.0);
        }
    }
}
