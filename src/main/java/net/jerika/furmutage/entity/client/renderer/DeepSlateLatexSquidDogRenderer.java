package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.jerika.furmutage.entity.client.model.DeepSlateLatexSquidDogFemaleModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.DeepSlateLatexSquidDog;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;

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

    @Override
    public void render(DeepSlateLatexSquidDog entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // Check light level at entity position
        BlockPos pos = entity.blockPosition();
        int blockLight = entity.level().getBrightness(LightLayer.BLOCK, pos);
        int skyLight = entity.level().getBrightness(LightLayer.SKY, pos);
        int totalLight = Math.max(blockLight, skyLight);
        
        // Calculate alpha based on light level (0-15 scale)
        // No light (0) = invisible (alpha 0.0), full light (15) = fully visible (alpha 1.0)
        float alpha = Mth.clamp(totalLight / 15.0F, 0.0F, 1.0F);
        
        // If completely dark, don't render at all
        if (alpha <= 0.0F) {
            return;
        }
        
        // Apply alpha to rendering (blend state is managed by render types)
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        
        // Reset color
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
