package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.jerika.furmutage.entity.client.model.DeepSlateLatexSquidDogFemaleModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.DeepSlateLatexSquidDog;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Simple renderer for DeepSlateLatexSquidDog using the copied model.
 * (Does not use Changed's AdvancedHumanoidRenderer to avoid generic mismatches.)
 */
public class DeepSlateLatexSquidDogRenderer extends MobRenderer<DeepSlateLatexSquidDog, DeepSlateLatexSquidDogFemaleModel> {
    public DeepSlateLatexSquidDogRenderer(EntityRendererProvider.Context context) {
        super(context,
                new DeepSlateLatexSquidDogFemaleModel(context.bakeLayer(ModModelLayers.DEEPSLATE_LATEX_SQUID_DOG_FEMALE_LAYER)),
                0.65f);
    }

    @Override
    public ResourceLocation getTextureLocation(DeepSlateLatexSquidDog entity) {
        return new ResourceLocation(furmutage.MOD_ID, "textures/entity/deepslate_latex_squid_dog.png");
    }

    @Override
    protected void scale(DeepSlateLatexSquidDog entity, PoseStack poseStack, float partialTick) {
        float f = 1.0525F;
        poseStack.scale(f, f, f);
    }

    @Override
    protected void setupRotations(DeepSlateLatexSquidDog entity, PoseStack poseStack, float bob, float bodyYRot, float partialTicks) {
        super.setupRotations(entity, poseStack, bob, bodyYRot, partialTicks);
        
        // Handle swimming rotation - rotate horizontally when swimming
        float swimAmount = entity.getSwimAmount(partialTicks);
        if (swimAmount > 0.0F) {
            float f3 = entity.isInWater() ? -90.0F - entity.getXRot() : -90.0F;
            float f4 = Mth.lerp(swimAmount, 0.0F, f3);
            poseStack.mulPose(Axis.XP.rotationDegrees(f4));
            if (entity.isVisuallySwimming()) {
                poseStack.translate(0.0D, -1.0D, 0.3D);
            }
        }
    }
}
