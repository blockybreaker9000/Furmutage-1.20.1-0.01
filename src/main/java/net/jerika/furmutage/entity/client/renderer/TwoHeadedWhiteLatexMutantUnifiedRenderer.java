package net.jerika.furmutage.entity.client.renderer;

import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.client.model.TwoHeadedWhiteLatexMutantUnifiedModel;
import net.jerika.furmutage.entity.custom.TwoHeadedWhiteLatexMutantUnifiedEntity;
import net.jerika.furmutage.furmutage;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class TwoHeadedWhiteLatexMutantUnifiedRenderer extends AdvancedHumanoidRenderer<TwoHeadedWhiteLatexMutantUnifiedEntity, TwoHeadedWhiteLatexMutantUnifiedModel<TwoHeadedWhiteLatexMutantUnifiedEntity>> {
    public TwoHeadedWhiteLatexMutantUnifiedRenderer(EntityRendererProvider.Context context) {
        super(context, new TwoHeadedWhiteLatexMutantUnifiedModel<>(context.bakeLayer(ModModelLayers.TWO_HEADED_WHITE_LATEX_MUTANT_UNIFIED_LAYER)),
                ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(TwoHeadedWhiteLatexMutantUnifiedEntity entity) {
        return new ResourceLocation(furmutage.MOD_ID, "textures/entity/two_headed_white_latex_mutant_unified.png");
    }
}
