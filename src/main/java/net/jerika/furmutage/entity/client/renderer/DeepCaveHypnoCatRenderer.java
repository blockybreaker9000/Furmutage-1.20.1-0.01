package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.jerika.furmutage.entity.client.model.DeepCaveHypnoCatModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.DeepCaveHypnoCat;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

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
}
