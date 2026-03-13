package net.jerika.furmutage.entity.client.renderer;

import com.mojang.datafixers.util.Pair;
import net.jerika.furmutage.furmutage;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

public class TaintedDarkChestBoatRenderer extends BoatRenderer {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(furmutage.MOD_ID, "textures/entity/boat/tainted_dark_chest_boat.png");

    public TaintedDarkChestBoatRenderer(EntityRendererProvider.Context context) {
        super(context, true);
    }

    @Override
    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
        Pair<ResourceLocation, ListModel<Boat>> pair = super.getModelWithLocation(boat);
        return Pair.of(TEXTURE, pair.getSecond());
    }

    @Override
    public ResourceLocation getTextureLocation(Boat boat) {
        return TEXTURE;
    }
}

