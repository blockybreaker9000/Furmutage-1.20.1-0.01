package net.jerika.furmutage.entity.client.renderer;

import net.jerika.furmutage.entity.client.model.LatexNetherMantaRayMaleModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.LatexNetherMantaRayMaleEntity;
import net.jerika.furmutage.furmutage;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LatexNetherMantaRayMaleRenderer extends AdvancedHumanoidRenderer<LatexNetherMantaRayMaleEntity, LatexNetherMantaRayMaleModel<LatexNetherMantaRayMaleEntity>> {
    public LatexNetherMantaRayMaleRenderer(EntityRendererProvider.Context context) {
        super(context, new LatexNetherMantaRayMaleModel<>(context.bakeLayer(ModModelLayers.LATEX_NETHER_MANTA_RAY_MALE_LAYER)),
                ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(LatexNetherMantaRayMaleEntity entity) {
        return new ResourceLocation(furmutage.MOD_ID, "textures/entity/latex_nether_manta_ray_male.png");
    }
}
