package net.jerika.furmutage.client;

import net.jerika.furmutage.client.renderer.ThunderiumLatexHumanRenderer;
import net.ltxprogrammer.changed.client.RegisterComplexRenderersEvent;
import net.ltxprogrammer.changed.entity.beast.LatexHuman;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class FurmutageClientEvents {
    @SubscribeEvent(priority = net.minecraftforge.eventbus.api.EventPriority.LOWEST)
    public static void registerComplexEntityRenderers(RegisterComplexRenderersEvent event) {
        // Get the latex_human entity type from Changed mod registry
        EntityType<?> latexHumanTypeRaw = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation("changed", "latex_human"));
        if (latexHumanTypeRaw != null) {
            // Cast to the correct type for ComplexRenderer registration
            @SuppressWarnings("unchecked")
            EntityType<LatexHuman> latexHumanType = (EntityType<LatexHuman>) latexHumanTypeRaw;
            
            // Use LOWEST priority to run after Changed mod registers its renderers,
            // then use reflection to replace them
            try {
                // Access the internal renderers map using reflection
                java.lang.reflect.Field renderersField = RegisterComplexRenderersEvent.class.getDeclaredField("renderers");
                renderersField.setAccessible(true);
                @SuppressWarnings("unchecked")
                java.util.Map<Object, net.minecraft.client.renderer.entity.EntityRendererProvider<?>> renderers = 
                    (java.util.Map<Object, net.minecraft.client.renderer.entity.EntityRendererProvider<?>>) renderersField.get(event);
                
                // Find the keys for latex_human renderers by iterating through the map
                Object defaultKeyToRemove = null;
                Object slimKeyToRemove = null;
                
                for (Object key : renderers.keySet()) {
                    String keyStr = key.toString();
                    if (keyStr.contains("latex_human")) {
                        if (keyStr.contains("default") || keyStr.contains("\"default\"")) {
                            defaultKeyToRemove = key;
                        } else if (keyStr.contains("slim") || keyStr.contains("\"slim\"")) {
                            slimKeyToRemove = key;
                        }
                    }
                }
                
                // Remove existing entries if found
                if (defaultKeyToRemove != null) {
                    renderers.remove(defaultKeyToRemove);
                }
                if (slimKeyToRemove != null) {
                    renderers.remove(slimKeyToRemove);
                }
                
                // Now register our custom renderers (they should be the only ones now)
                event.registerEntityRenderer(latexHumanType, "default", ThunderiumLatexHumanRenderer.forModelSize(false));
                event.registerEntityRenderer(latexHumanType, "slim", ThunderiumLatexHumanRenderer.forModelSize(true));
                
            } catch (Exception e) {
                // If reflection fails, log a warning
                // The mixin (LatexHumanoidArmorLayerMixin) will still handle armor texture replacement
                net.jerika.furmutage.furmutage.LOGGER.warn("Could not replace LatexHuman renderer with ThunderiumLatexHumanRenderer: {}. Armor texture replacement via mixin will still work.", e.getMessage());
            }
        }
    }
}
