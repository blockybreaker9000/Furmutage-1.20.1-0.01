package net.jerika.furmutage.entity.client.renderer;

import net.jerika.furmutage.entity.client.model.LatexHumanFleshModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.LatexHumanFleshEntity;
import net.jerika.furmutage.furmutage;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LatexHumanFleshRenderer extends AdvancedHumanoidRenderer<LatexHumanFleshEntity, LatexHumanFleshModel<LatexHumanFleshEntity>> {
    public LatexHumanFleshRenderer(EntityRendererProvider.Context context) {
        super(context, new LatexHumanFleshModel<>(context.bakeLayer(ModModelLayers.LATEX_HUMAN_FLESH_LAYER)),
                ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(LatexHumanFleshEntity entity) {
        return new ResourceLocation(furmutage.MOD_ID, "textures/entity/latex_human_flesh.png");
    }
}
