package net.jerika.furmutage.entity.vehicle;

import net.jerika.furmutage.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class TaintedDarkChestBoatEntity extends ChestBoat {

    public TaintedDarkChestBoatEntity(EntityType<? extends TaintedDarkChestBoatEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public Item getDropItem() {
        return ModItems.TAINTED_DARK_CHEST_BOAT.get();
    }
}

