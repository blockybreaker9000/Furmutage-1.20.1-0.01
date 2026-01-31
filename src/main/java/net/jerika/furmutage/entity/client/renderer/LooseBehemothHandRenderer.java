package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.jerika.furmutage.entity.client.model.LooseBehemothHandModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.LooseBehemothHand;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class LooseBehemothHandRenderer extends MobRenderer<LooseBehemothHand, LooseBehemothHandModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(furmutage.MOD_ID, "textures/entity/loose_behemoth_hand.png");

    public LooseBehemothHandRenderer(EntityRendererProvider.Context context) {
        super(context, new LooseBehemothHandModel(context.bakeLayer(ModModelLayers.LOOSE_BEHEMOTH_HAND_LAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(LooseBehemothHand entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(LooseBehemothHand entity, PoseStack poseStack, float partialTick) {
        float s = 1.0F;
        poseStack.scale(s, s, s);
    }
}
