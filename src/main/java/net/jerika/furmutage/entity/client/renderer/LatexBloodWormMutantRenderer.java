package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.jerika.furmutage.entity.client.model.LatexBloodWormMutantModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.LatexBloodWormMutant;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

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
}
