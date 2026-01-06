package net.jerika.furmutage.entity.client.renderer;

import net.jerika.furmutage.entity.custom.DarkLatexCowEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom renderer for dark latex infected cow with custom texture.
 */
public class DarkLatexCowRenderer extends CowRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(furmutage.MOD_ID, "textures/entity/dark_latex_cow.png");

    public DarkLatexCowRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(net.minecraft.world.entity.animal.Cow pEntity) {
        return TEXTURE;
    }
}

