package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.jerika.furmutage.entity.client.model.DeepLatexSquidModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.DeepLatexSquidEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renderer for Deep Latex Squid entity.
 * Matches Changed mod's LatexSquidDogMaleRenderer structure.
 */
public class DeepLatexSquidRenderer extends MobRenderer<DeepLatexSquidEntity, DeepLatexSquidModel<DeepLatexSquidEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(furmutage.MOD_ID, "textures/entity/deep_latex_squid.png");
    
    public DeepLatexSquidRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new DeepLatexSquidModel<>(pContext.bakeLayer(ModModelLayers.DEEP_LATEX_SQUID_LAYER)), 0.65f);
    }

    @Override
    public ResourceLocation getTextureLocation(DeepLatexSquidEntity pEntity) {
        return TEXTURE;
    }
    
    @Override
    protected void scale(DeepLatexSquidEntity entity, PoseStack pose, float partialTick) {
        // Scale matches Changed mod's LatexSquidDogMaleRenderer exactly
        pose.scale(1.0525F, 1.0525F, 1.0525F);
    }
    
    @Override
    public void render(DeepLatexSquidEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        // Prepare model before rendering (similar to prepareMobModel in Changed mod)
        if (pEntity.isSwimming() && pEntity.isInWater()) {
            // Entity is swimming
        }
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}

