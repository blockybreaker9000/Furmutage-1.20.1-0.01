package net.jerika.furmutage.mixins.render;

import net.ltxprogrammer.changed.client.renderer.layers.LatexHumanoidArmorLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LatexHumanoidArmorLayer.class)
public class LatexHumanoidArmorLayerMixin {
    @ModifyVariable(
        method = "getArmorResource",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/client/ForgeHooksClient;getArmorTexture(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;Ljava/lang/String;Lnet/minecraft/world/entity/EquipmentSlot;Ljava/lang/String;)Ljava/lang/String;"
        ),
        ordinal = 0
    )
    private String replaceArmorTexturePath(String original, Entity entity, ItemStack stack, EquipmentSlot slot, String type) {
        // Replace armor texture path to use Thunderium armor texture
        if (original != null && original.contains("textures/models/armor/") && !original.contains("thunderium")) {
            // Use single thunderium_armor.png texture for all armor
            String overlay = type != null ? "_" + type : "";
            return String.format("furmutage:textures/models/armor/thunderium_armor%s.png", overlay);
        }
        return original;
    }
}
