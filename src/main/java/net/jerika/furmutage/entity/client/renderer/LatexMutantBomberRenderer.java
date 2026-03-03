package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.jerika.furmutage.entity.client.model.LatexBomberMutantModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.LatexMutantBomberEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class LatexMutantBomberRenderer extends MobRenderer<LatexMutantBomberEntity, LatexBomberMutantModel<LatexMutantBomberEntity>> {
    public LatexMutantBomberRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new LatexBomberMutantModel<>(pContext.bakeLayer(ModModelLayers.LATEX_BOMBER_MUTANT_LAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(LatexMutantBomberEntity pEntity) {
        return new ResourceLocation(furmutage.MOD_ID, "textures/entity/latex_bomber_mutant.png");
    }

    @Override
    protected void scale(LatexMutantBomberEntity entity, PoseStack poseStack, float partialTickTime) {
        // Base baby scale
        if (entity.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }

        // Strong creeper-style swelling, based directly on fuse progress
        float swell = entity.getSwelling(partialTickTime);
        if (swell > 0.0F) {
            swell = Mth.clamp(swell, 0.0F, 1.0F);

            // Creeper formula, but a bit stronger so it's very visible
            float f1 = 1.0F + Mth.sin(swell * 100.0F) * swell * 0.15F;
            swell = swell * swell;
            swell = swell * swell;
            float xz = (1.0F + swell * 0.9F) * f1; // up to ~1.9x on X/Z
            float y  = (1.0F + swell * 0.4F) / f1; // a bit less on Y so it looks like bulging

            poseStack.scale(xz, y, xz);
        }
    }
}

