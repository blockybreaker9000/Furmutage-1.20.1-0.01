package net.jerika.furmutage.item.custom.armor.client;

import net.jerika.furmutage.furmutage;
import net.jerika.furmutage.item.custom.armor.ThunderiumArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ThunderiumArmorModel extends GeoModel<ThunderiumArmorItem> {
    private static final ResourceLocation MODEL = new ResourceLocation(furmutage.MOD_ID, "geo/thunderium_armor.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(furmutage.MOD_ID, "textures/models/armor/thunderium_armor.png");

    @Override
    public ResourceLocation getModelResource(ThunderiumArmorItem animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(ThunderiumArmorItem animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(ThunderiumArmorItem animatable) {
        // No separate animation file for now
        return null;
    }
}

