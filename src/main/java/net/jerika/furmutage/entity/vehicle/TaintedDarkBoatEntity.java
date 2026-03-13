package net.jerika.furmutage.entity.vehicle;

import net.jerika.furmutage.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class TaintedDarkBoatEntity extends Boat {

    public TaintedDarkBoatEntity(EntityType<? extends TaintedDarkBoatEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public Item getDropItem() {
        return ModItems.TAINTED_DARK_BOAT.get();
    }
}
