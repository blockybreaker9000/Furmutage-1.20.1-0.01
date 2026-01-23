package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.jerika.furmutage.entity.client.model.DeepCaveHypnoCatModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.DeepCaveHypnoCat;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;

public class DeepCaveHypnoCatRenderer extends MobRenderer<DeepCaveHypnoCat, DeepCaveHypnoCatModel> {
    public DeepCaveHypnoCatRenderer(EntityRendererProvider.Context context) {
        super(context,
                new DeepCaveHypnoCatModel(context.bakeLayer(ModModelLayers.DEEP_CAVE_HYPNO_CAT_LAYER)),
                0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(DeepCaveHypnoCat entity) {
        return new ResourceLocation(furmutage.MOD_ID, "textures/entity/deep_cave_hypno_cat.png");
    }

    @Override
    protected void scale(DeepCaveHypnoCat entity, PoseStack poseStack, float partialTick) {
        float f = 1.0F;
        poseStack.scale(f, f, f);
    }

    @Override
    public void render(DeepCaveHypnoCat entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
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
