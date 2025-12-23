package net.jerika.furmutage.entity.client.renderer;

import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom renderer for white latex infected sheep with custom texture.
 */
public class WhiteLatexSheepRenderer extends SheepRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(furmutage.MOD_ID, "textures/entity/white_latex_sheep.png");

    public WhiteLatexSheepRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(net.minecraft.world.entity.animal.Sheep pEntity) {
        return TEXTURE;
    }
}

