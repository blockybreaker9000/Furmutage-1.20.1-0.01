package net.jerika.furmutage.item.custom.armor.client;

import net.jerika.furmutage.item.custom.armor.ThunderiumArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ThunderiumArmorRenderer extends GeoArmorRenderer<ThunderiumArmorItem> {
    public ThunderiumArmorRenderer() {
        super(new ThunderiumArmorModel());
    }
}


