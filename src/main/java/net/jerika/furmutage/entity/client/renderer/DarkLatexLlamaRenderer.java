package net.jerika.furmutage.entity.client.renderer;

import net.jerika.furmutage.entity.custom.DarkLatexLlamaEntity;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LlamaRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom renderer for dark latex infected llama with custom texture.
 */
public class DarkLatexLlamaRenderer extends LlamaRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(furmutage.MOD_ID, "textures/entity/dark_latex_llama.png");

    public DarkLatexLlamaRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, ModelLayers.LLAMA);
    }

    @Override
    public ResourceLocation getTextureLocation(net.minecraft.world.entity.animal.horse.Llama pEntity) {
        return TEXTURE;
    }
}

