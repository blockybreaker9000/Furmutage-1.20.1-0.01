package net.jerika.furmutage.ai;

import net.jerika.furmutage.entity.custom.TSCDroneEntity;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.util.Mth;

public class DroneMoveControl extends MoveControl {
    private final TSCDroneEntity drone;
    private float speedMultiplier = 0.1F;

    public DroneMoveControl(TSCDroneEntity pMob) {
        super(pMob);
        this.drone = pMob;
    }

    @Override
    public void tick() {
        if (this.operation == Operation.MOVE_TO) {
            this.operation = Operation.WAIT;
            double d0 = this.wantedX - this.drone.getX();
            double d1 = this.wantedY - this.drone.getY();
            double d2 = this.wantedZ - this.drone.getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            
            if (d3 < 2.500000277905201E-7D) {
                this.drone.setYya(0.0F);
                this.drone.setZza(0.0F);
                return;
            }
            
            float f = (float)(Mth.atan2(d2, d0) * (180F / (float)Math.PI)) - 90.0F;
            this.drone.setYRot(this.rotlerp(this.drone.getYRot(), f, 90.0F));
            
            float speed = (float)(this.speedModifier * this.drone.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED));
            
            if (this.drone.isInWater()) {
                this.drone.setSpeed(speed * 0.02F);
                float f1 = -(float)(Mth.atan2(d1, Math.sqrt(d0 * d0 + d2 * d2)) * (180F / (float)Math.PI));
                f1 = Mth.clamp(Mth.wrapDegrees(f1), -85.0F, 85.0F);
                this.drone.setXRot(this.rotlerp(this.drone.getXRot(), f1, 5.0F));
                float f2 = Mth.cos(this.drone.getXRot() * ((float)Math.PI / 180F));
                float f3 = Mth.sin(this.drone.getXRot() * ((float)Math.PI / 180F));
                this.drone.zza = f2 * speed;
                this.drone.yya = -f3 * speed;
            } else {
                this.drone.setSpeed(speed * 0.1F);
                float f4 = (float)(Math.sqrt(d0 * d0 + d2 * d2) * 0.05F);
                float f1 = -(float)(Mth.atan2(d1, Math.sqrt(d0 * d0 + d2 * d2)) * (180F / (float)Math.PI));
                f1 = Mth.clamp(Mth.wrapDegrees(f1), -85.0F, 85.0F);
                this.drone.setXRot(this.rotlerp(this.drone.getXRot(), f1, 5.0F));
                float f2 = Mth.cos(this.drone.getXRot() * ((float)Math.PI / 180F));
                float f3 = Mth.sin(this.drone.getXRot() * ((float)Math.PI / 180F));
                this.drone.zza = f2 * speed;
                this.drone.yya = -f3 * speed;
            }
        } else {
            this.drone.setYya(0.0F);
            this.drone.setZza(0.0F);
        }
    }
}

