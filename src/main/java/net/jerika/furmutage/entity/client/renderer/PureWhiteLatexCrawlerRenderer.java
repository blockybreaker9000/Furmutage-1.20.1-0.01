package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.client.model.PureWhiteLatexCrawlerModel;
import net.jerika.furmutage.entity.custom.PureWhiteLatexCrawlerEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PureWhiteLatexCrawlerRenderer extends MobRenderer<PureWhiteLatexCrawlerEntity, PureWhiteLatexCrawlerModel> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(furmutage.MOD_ID, "textures/entity/pure_white_latex_crawler.png");

    public PureWhiteLatexCrawlerRenderer(EntityRendererProvider.Context context) {
        super(context, new PureWhiteLatexCrawlerModel(context.bakeLayer(ModModelLayers.PURE_WHITE_LATEX_CRAWLER_LAYER)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(PureWhiteLatexCrawlerEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(PureWhiteLatexCrawlerEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // Slight scale to make it a bit larger than default player-sized mobs
        poseStack.pushPose();
        poseStack.scale(1.1F, 1.1F, 1.1F);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }
}

