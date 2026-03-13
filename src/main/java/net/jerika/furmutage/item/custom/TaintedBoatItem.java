package net.jerika.furmutage.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.function.Supplier;

import net.minecraft.world.entity.EntityType;

/**
 * Item that places a custom boat entity when used on a block (e.g. ground or water).
 */
public class TaintedBoatItem extends Item {

    private final Supplier<EntityType<? extends Boat>> boatType;

    public TaintedBoatItem(Supplier<EntityType<? extends Boat>> boatType, Properties properties) {
        super(properties);
        this.boatType = boatType;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockPos blockPos = context.getClickedPos();
        Direction face = context.getClickedFace();

        if (face != Direction.UP) {
            return InteractionResult.PASS;
        }

        double x = blockPos.getX() + 0.5;
        double y = blockPos.getY() + 1.0;
        double z = blockPos.getZ() + 0.5;

        AABB aabb = new AABB(x - 1.0, y - 1.0, z - 1.0, x + 1.0, y + 1.0, z + 1.0);
        if (!level.getEntitiesOfClass(Boat.class, aabb, boat -> true).isEmpty()) {
            return InteractionResult.FAIL;
        }

        Boat boat = boatType.get().create(level);
        if (boat == null) {
            return InteractionResult.FAIL;
        }

        boat.setPos(x, y, z);
        if (context.getPlayer() != null) {
            boat.setYRot(context.getPlayer().getYRot());
            if (!context.getPlayer().getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }
        level.addFreshEntity(boat);

        return InteractionResult.CONSUME;
    }
}
