package net.jerika.furmutage.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class StalkAndHideGoal extends Goal {
    private final PathfinderMob mob;
    private LivingEntity target;
    private final double speedModifier;
    private final float attackRadius;
    private final float attackRadiusSqr;
    private Path path;
    private int pathRecalcTime;
    private int stalkingTime = 0;
    private int hideTime = 0;
    private static final int STALK_DURATION = 1000; //  stalking
    private static final int HIDE_DURATION = 600; //  hiding
    private boolean isHiding = false;
    private BlockPos hidePosition;

    public StalkAndHideGoal(PathfinderMob mob, double speedModifier, float attackRadius) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.attackRadius = attackRadius;
        this.attackRadiusSqr = attackRadius * attackRadius;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }
        
        // Only stalk players
        if (!(target instanceof Player)) {
            return false;
        }
        
        // Random chance (100% chance to use this behavior)
        if (this.mob.getRandom().nextInt(100) >= 100) {
            return false;
        }
        
        this.target = target;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.target == null || !this.target.isAlive()) {
            return false;
        }
        
        if (!(this.target instanceof Player)) {
            return false;
        }
        
        // Continue if we're still stalking or hiding
        return this.stalkingTime < STALK_DURATION + HIDE_DURATION;
    }

    @Override
    public void start() {
        this.stalkingTime = 0;
        this.hideTime = 0;
        this.isHiding = false;
        this.hidePosition = null;
        this.pathRecalcTime = 0;
    }

    @Override
    public void stop() {
        this.target = null;
        this.path = null;
        this.mob.getNavigation().stop();
        this.stalkingTime = 0;
        this.hideTime = 0;
        this.isHiding = false;
    }

    @Override
    public void tick() {
        if (this.target == null) {
            return;
        }
        
        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        double distanceSqr = this.mob.distanceToSqr(this.target);
        
        this.stalkingTime++;
        
        // Phase 1: Stalk (follow at a distance)
        if (this.stalkingTime < STALK_DURATION) {
            // Follow target but keep distance
            if (distanceSqr > this.attackRadiusSqr * 2) {
                // Too far, move closer
                this.pathRecalcTime++;
                if (this.pathRecalcTime >= 10) {
                    this.path = this.mob.getNavigation().createPath(this.target, 0);
                    this.pathRecalcTime = 0;
                }
                
                if (this.path != null) {
                    this.mob.getNavigation().moveTo(this.path, this.speedModifier * 0.7); // Slower stalking speed
                }
            } else {
                // Close enough, stop and wait
                this.mob.getNavigation().stop();
            }
        }
        // Phase 2: Hide behind blocks
        else if (this.stalkingTime < STALK_DURATION + HIDE_DURATION) {
            if (!this.isHiding) {
                // Find a position to hide behind (opposite side of target)
                this.hidePosition = findHidePosition();
                this.isHiding = true;
            }
            
            if (this.hidePosition != null) {
                this.hideTime++;
                double hideDistSqr = this.mob.distanceToSqr(this.hidePosition.getX() + 0.5, 
                                                           this.hidePosition.getY(), 
                                                           this.hidePosition.getZ() + 0.5);
                
                if (hideDistSqr > 4.0) {
                    // Move to hide position
                    this.pathRecalcTime++;
                    if (this.pathRecalcTime >= 10) {
                        this.path = this.mob.getNavigation().createPath(
                            this.hidePosition.getX() + 0.5,
                            this.hidePosition.getY(),
                            this.hidePosition.getZ() + 0.5,
                            0
                        );
                        this.pathRecalcTime = 0;
                    }
                    
                    if (this.path != null) {
                        this.mob.getNavigation().moveTo(this.path, this.speedModifier * 0.5); // Even slower when hiding
                    }
                } else {
                    // At hide position, stop
                    this.mob.getNavigation().stop();
                }
            }
        }
        // Phase 3: Attack (after hiding phase)
        else {
            // Move towards target and attack
            if (distanceSqr > this.attackRadiusSqr) {
                this.pathRecalcTime++;
                if (this.pathRecalcTime >= 10) {
                    this.path = this.mob.getNavigation().createPath(this.target, 0);
                    this.pathRecalcTime = 0;
                }
                
                if (this.path != null) {
                    this.mob.getNavigation().moveTo(this.path, this.speedModifier * 1.2); // Faster attack speed
                }
            } else {
                // Close enough to attack - set as target so other attack goals can handle it
                if (this.mob.getSensing().hasLineOfSight(this.target)) {
                    this.mob.setTarget(this.target);
                }
            }
        }
    }

    private BlockPos findHidePosition() {
        if (this.target == null) {
            return null;
        }
        
        // Find a position behind the target (opposite direction from mob to target)
        Vec3 mobPos = this.mob.position();
        Vec3 targetPos = this.target.position();
        Vec3 direction = mobPos.subtract(targetPos).normalize();
        
        // Look for a block position 3-5 blocks away from target in the opposite direction
        for (int distance = 3; distance <= 5; distance++) {
            BlockPos candidate = new BlockPos(
                (int)(targetPos.x + direction.x * distance),
                (int)targetPos.y,
                (int)(targetPos.z + direction.z * distance)
            );
            
            // Check if position is valid (has solid block and air above)
            if (this.mob.level().getBlockState(candidate).isSolid() &&
                this.mob.level().getBlockState(candidate.above()).isAir()) {
                return candidate;
            }
        }
        
        // Fallback: return position behind target
        return new BlockPos(
            (int)(targetPos.x + direction.x * 4),
            (int)targetPos.y,
            (int)(targetPos.z + direction.z * 4)
        );
    }
}

