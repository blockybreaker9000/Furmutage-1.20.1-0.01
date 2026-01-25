package net.jerika.furmutage.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.jerika.furmutage.entity.client.model.DeepCaveHypnoCatModel;
import net.jerika.furmutage.entity.client.model.ModModelLayers;
import net.jerika.furmutage.entity.custom.DeepCaveHypnoCat;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;

public class DeepCaveHypnoCatRenderer extends MobRenderer<DeepCaveHypnoCat, DeepCaveHypnoCatModel> {
    public DeepCaveHypnoCatRenderer(EntityRendererProvider.Context context) {
        super(context,
                new DeepCaveHypnoCatModel(context.bakeLayer(ModModelLayers.DEEP_CAVE_HYPNO_CAT_LAYER)),
                0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(DeepCaveHypnoCat entity) {
        return new ResourceLocation(furmutage.MOD_ID, "textures/entity/deep_cave_hypno_cat.png");
    }

    @Override
    protected void scale(DeepCaveHypnoCat entity, PoseStack poseStack, float partialTick) {
        float f = 1.0F;
        poseStack.scale(f, f, f);
    }

    @Override
    public void render(DeepCaveHypnoCat entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // Check light level at entity position
        BlockPos pos = entity.blockPosition();
        int blockLight = entity.level().getBrightness(LightLayer.BLOCK, pos);
        int skyLight = entity.level().getBrightness(LightLayer.SKY, pos);
        int totalLight = Math.max(blockLight, skyLight);
        
        // Check if any nearby player is holding a light source
        boolean playerHasLightSource = isPlayerHoldingLightSource(entity, 16.0D);
        
        // Calculate alpha based on light level (0-15 scale)
        // No light (0) = invisible (alpha 0.0), full light (15) = fully visible (alpha 1.0)
        // If player has light source, make fully visible
        float alpha = playerHasLightSource ? 1.0F : Mth.clamp(totalLight / 15.0F, 0.0F, 1.0F);
        
        // If completely dark and no player with light source, don't render at all
        if (alpha <= 0.0F) {
            return;
        }
        
        // Apply alpha to rendering (blend state is managed by render types)
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        
        // Reset color
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
    
    /**
     * Check if any player within range is holding a light source item or block
     */
    private boolean isPlayerHoldingLightSource(DeepCaveHypnoCat entity, double range) {
        if (entity.level() == null) {
            return false;
        }
        
        for (Player player : entity.level().players()) {
            if (player.distanceToSqr(entity) <= range * range) {
                // Check main hand
                ItemStack mainHand = player.getMainHandItem();
                if (isLightSource(mainHand)) {
                    return true;
                }
                
                // Check off hand
                ItemStack offHand = player.getOffhandItem();
                if (isLightSource(offHand)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Check if an item stack is a light source
     */
    private boolean isLightSource(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        
        Item item = stack.getItem();
        
        // Check if it's a block item with light emission
        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            // Check if block emits light (light emission > 0)
            if (block.defaultBlockState().getLightEmission() > 0) {
                return true;
            }
        }
        
        // Check for common light source items
        return item == Items.TORCH ||
               item == Items.SOUL_TORCH ||
               item == Items.REDSTONE_TORCH ||
               item == Items.LANTERN ||
               item == Items.SOUL_LANTERN ||
               item == Items.GLOWSTONE ||
               item == Items.SEA_LANTERN ||
               item == Items.SHROOMLIGHT ||
               item == Items.CANDLE ||
               item == Items.JACK_O_LANTERN ||
               item == Items.BEACON ||
               item == Items.END_ROD ||
               item == Items.CAMPFIRE ||
               item == Items.SOUL_CAMPFIRE ||
               item == Items.MAGMA_BLOCK;
    }
}
