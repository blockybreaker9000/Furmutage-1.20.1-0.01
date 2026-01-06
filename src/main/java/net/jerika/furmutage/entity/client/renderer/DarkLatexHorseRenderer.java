package net.jerika.furmutage.entity.client.renderer;

import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Horse;

/**
 * Custom renderer for dark latex infected horse with custom texture.
 * Note: HorseRenderer is final, so we extend MobRenderer directly.
 */
public class DarkLatexHorseRenderer extends MobRenderer<Horse, HorseModel<Horse>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(furmutage.MOD_ID, "textures/entity/dark_latex_horse.png");

    public DarkLatexHorseRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new HorseModel<>(pContext.bakeLayer(ModelLayers.HORSE)), 1.1f);
    }

    @Override
    public ResourceLocation getTextureLocation(Horse pEntity) {
        return TEXTURE;
    }
}

