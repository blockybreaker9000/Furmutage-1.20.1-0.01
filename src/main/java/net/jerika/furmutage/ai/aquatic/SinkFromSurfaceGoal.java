package net.jerika.furmutage.ai.aquatic;

import net.jerika.furmutage.entity.custom.DeepLatexSquidEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class SinkFromSurfaceGoal extends Goal {
    private final DeepLatexSquidEntity mob;
    private double wantedX;
    private double wantedY;
    private double wantedZ;
    private final double speedModifier;
    private final Level level;

    public SinkFromSurfaceGoal(DeepLatexSquidEntity entity, double speed) {
        this.mob = entity;
        this.speedModifier = speed;
        this.level = entity.level();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob.getTarget() != null) {
            return false;
        } else if (!this.mob.isInWater()) {
            return false;
        } else if (this.mob.wantsToSurface()) {
            return false;
        } else if (!level.getBlockState(this.mob.blockPosition()).isAir()) {
            return false;
        } else if (!this.mob.canFitInWater(this.mob.position())) {
            return false;
        } else {
            this.wantedX = this.mob.getX();
            this.wantedY = this.mob.getY() - 1.0;
            this.wantedZ = this.mob.getZ();
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.getTarget() != null) {
            return false;
        }

        return !this.mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
    }

    @Nullable
    private Vec3 getWaterPos() {
        RandomSource random = this.mob.getRandom();
        BlockPos blockpos = this.mob.blockPosition();

        for(int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.offset(random.nextInt(20) - 10, 2 - random.nextInt(8), random.nextInt(20) - 10);
            if (this.level.getBlockState(blockpos1).is(Blocks.WATER)) {
                return Vec3.atBottomCenterOf(blockpos1);
            }
        }

        return null;
    }
}

