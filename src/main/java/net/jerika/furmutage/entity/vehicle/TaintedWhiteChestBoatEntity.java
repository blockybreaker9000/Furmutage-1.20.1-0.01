package net.jerika.furmutage.entity.vehicle;

import net.jerika.furmutage.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class TaintedWhiteChestBoatEntity extends ChestBoat {

    public TaintedWhiteChestBoatEntity(EntityType<? extends TaintedWhiteChestBoatEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public Item getDropItem() {
        return ModItems.TAINTED_WHITE_CHEST_BOAT.get();
    }
}

