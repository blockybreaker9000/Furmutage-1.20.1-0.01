package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.jerika.furmutage.entity.client.model.LatexBloodWormMutantModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.LatexBloodWormMutant;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;

/**
 * Simple renderer for LatexBloodWormMutant using the copied model.
 * (Does not use Changed's AdvancedHumanoidRenderer to avoid generic mismatches.)
 */
public class LatexBloodWormMutantRenderer extends MobRenderer<LatexBloodWormMutant, LatexBloodWormMutantModel> {
    public LatexBloodWormMutantRenderer(EntityRendererProvider.Context context) {
        super(context,
                new LatexBloodWormMutantModel(context.bakeLayer(ModModelLayers.LATEX_BLOOD_WORM_MUTANT_LAYER)),
                0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(LatexBloodWormMutant entity) {
        return new ResourceLocation(furmutage.MOD_ID, "textures/entity/latex_blood_worm_mutant.png");
    }

    @Override
    protected void scale(LatexBloodWormMutant entity, PoseStack poseStack, float partialTick) {
        float f = 1.0F;
        poseStack.scale(f, f, f);
    }

    @Override
    public void render(LatexBloodWormMutant entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
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
