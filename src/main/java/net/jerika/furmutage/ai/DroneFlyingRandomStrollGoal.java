package net.jerika.furmutage.ai;

import net.jerika.furmutage.entity.custom.TSCDroneEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class DroneFlyingRandomStrollGoal extends Goal {
    private final TSCDroneEntity drone;
    private double wantedX;
    private double wantedY;
    private double wantedZ;
    private final double speedModifier;

    public DroneFlyingRandomStrollGoal(TSCDroneEntity pMob, double pSpeedModifier) {
        this.drone = pMob;
        this.speedModifier = pSpeedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.drone.getRandom().nextInt(10) != 0) {
            return false;
        } else {
            Vec3 vec3 = this.getRandomPos();
            if (vec3 == null) {
                return false;
            } else {
                this.wantedX = vec3.x;
                this.wantedY = vec3.y;
                this.wantedZ = vec3.z;
                return true;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.drone.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.drone.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
    }

    @javax.annotation.Nullable
    private Vec3 getRandomPos() {
        RandomSource randomsource = this.drone.getRandom();
        double currentX = this.drone.getX();
        double currentY = this.drone.getY();
        double currentZ = this.drone.getZ();
        
        // Generate random position in air, keeping a good hover height
        for(int i = 0; i < 10; ++i) {
            double randomX = currentX + (double)(randomsource.nextInt(20) - 10);
            double randomY = currentY + (double)(randomsource.nextInt(6) - 3);
            double randomZ = currentZ + (double)(randomsource.nextInt(20) - 10);
            
            // Make sure Y is at a reasonable height (not too low, not too high)
            if (randomY < currentY - 2 || randomY > currentY + 4) {
                continue;
            }
            
            Vec3 vec3 = new Vec3(randomX, randomY, randomZ);
            if (this.drone.level().noCollision(this.drone, this.drone.getBoundingBox().move(vec3.subtract(this.drone.position())))) {
                return vec3;
            }
        }

        return null;
    }
}

