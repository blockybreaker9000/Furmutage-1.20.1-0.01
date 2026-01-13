package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.jerika.furmutage.entity.client.model.DeepSlateLatexSquidDogFemaleModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.DeepSlateLatexSquidDog;
import net.ltxprogrammer.changed.Changed;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

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
        return Changed.modResource("textures/latex_squid_dog_female.png");
    }

    @Override
    protected void scale(DeepSlateLatexSquidDog entity, PoseStack poseStack, float partialTick) {
        float f = 1.0525F;
        poseStack.scale(f, f, f);
    }
}
