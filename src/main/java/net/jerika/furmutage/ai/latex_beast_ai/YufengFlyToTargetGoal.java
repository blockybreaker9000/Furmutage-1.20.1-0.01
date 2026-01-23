package net.jerika.furmutage.ai.latex_beast_ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/**
 * Makes Yufeng entities fly when they have a target
 */
public class YufengFlyToTargetGoal extends Goal {
    private final Mob mob;
    private static final double FLY_HEIGHT = 4.0D; // Fly 4 blocks above target
    private static final int COOLDOWN_TICKS = 20 * 120; // 2 minutes at 20 tps
    private int cooldownRemaining = 0;

    public YufengFlyToTargetGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // Respect cooldown: do not start flying again until cooldown expires
        if (cooldownRemaining > 0) {
            cooldownRemaining--;
            return false;
        }

        LivingEntity target = mob.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }

        // Only activate if target is on ground and we're not already high enough
        if (!target.onGround()) {
            return false; // Target is already in air, no need to fly
        }

        // Check if we're below the target or close to ground level
        double targetY = target.getY();
        double mobY = mob.getY();
        
        // Fly if we're more than 2 blocks below target, or if we're at ground level and target is far
        return mobY < targetY + 2.0D;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = mob.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }

        // Stop flying once we're sufficiently above the target
        double targetY = target.getY();
        double mobY = mob.getY();
        
        // Continue flying until we reach the desired height above target
        return mobY < targetY + FLY_HEIGHT;
    }

    @Override
    public void start() {
        // Try to enable flying mode using reflection (Changed mod method)
        tryEnableFlying();
    }

    @Override
    public void tick() {
        LivingEntity target = mob.getTarget();
        if (target == null) {
            return;
        }

        double currentY = mob.getY();
        double targetY = target.getY();
        
        // Trigger wing flapping animation when flying
        tryTriggerWingFlap();
        
        // Stop flying if we're already above the target
        if (currentY > targetY + 0.5D) {
            // Re-enable gravity and stop flying
            mob.setNoGravity(false);
            // Slow down vertical movement to let gravity take over
            Vec3 motion = mob.getDeltaMovement();
            mob.setDeltaMovement(motion.x, Math.min(motion.y, 0.0D), motion.z);
            return;
        }

        // Calculate desired position above target (but don't go too high)
        double targetX = target.getX();
        double desiredY = Math.min(target.getY() + FLY_HEIGHT, targetY + 3.0D); // Cap at 3 blocks above target
        double targetZ = target.getZ();

        // Look at target
        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

        // Enable flying mode
        mob.setNoGravity(true);
        
        // Set pose that might trigger wing flapping animation
        // Try setting a custom pose that Changed mod might recognize for flying
        trySetFlyingPose();

        // Calculate direction to target position
        double dx = targetX - mob.getX();
        double dy = desiredY - currentY;
        double dz = targetZ - mob.getZ();
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (distance > 0.5D) {
            // Normalize direction and apply movement
            double speed = 0.5D;
            Vec3 direction = new Vec3(dx / distance, dy / distance, dz / distance);
            Vec3 motion = direction.scale(speed);
            
            // Stop upward movement if we're getting close to or above target level
            if (currentY >= targetY - 0.5D) {
                // Don't go higher, just move horizontally
                motion = new Vec3(motion.x, Math.min(motion.y, 0.0D), motion.z);
            } else if (currentY < desiredY - 1.0D) {
                // Apply vertical lift if we're too low
                motion = new Vec3(motion.x, Math.max(motion.y, 0.3D), motion.z);
            }
            
            mob.setDeltaMovement(motion);
        } else {
            // Hover in place above target
            Vec3 motion = mob.getDeltaMovement();
            mob.setDeltaMovement(motion.x * 0.8, 0.0D, motion.z * 0.8);
        }

        // Try to maintain flight if on ground
        if (mob.onGround() && currentY < targetY) {
            Vec3 motion = mob.getDeltaMovement();
            mob.setDeltaMovement(motion.x, 0.4D, motion.z);
        }
    }

    @Override
    public void stop() {
        // Re-enable gravity when done flying
        mob.setNoGravity(false);
        // Reset pose
        mob.setPose(Pose.STANDING);

        // Start cooldown so this flying goal cannot activate again for 2 minutes
        cooldownRemaining = COOLDOWN_TICKS;
    }
    
    /**
     * Try to set a pose that might trigger wing flapping animation
     */
    private void trySetFlyingPose() {
        try {
            // Try setting the entity to a flying-like pose
            // Changed mod might check for certain poses to trigger wing animations
            mob.setPose(Pose.FALL_FLYING);
        } catch (Exception e) {
            // If FALL_FLYING doesn't work, try other poses
            try {
                mob.setPose(Pose.SWIMMING);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Try to enable flying mode using Changed mod's reflection methods
     */
    private void tryEnableFlying() {
        try {
            // Try to find a method to set flying mode
            Class<?> entityClass = mob.getClass();
            
            // Look for methods like setFlying, setFlightMode, etc.
            java.lang.reflect.Method[] methods = entityClass.getMethods();
            for (java.lang.reflect.Method method : methods) {
                String methodName = method.getName().toLowerCase();
                if ((methodName.contains("fly") || methodName.contains("flight")) && 
                    method.getParameterCount() == 1 && 
                    method.getParameterTypes()[0] == boolean.class) {
                    try {
                        method.invoke(mob, true);
                        return;
                    } catch (Exception ignored) {
                    }
                }
            }

            // Fallback: try to find a field
            try {
                java.lang.reflect.Field flyingField = entityClass.getDeclaredField("flying");
                flyingField.setAccessible(true);
                flyingField.setBoolean(mob, true);
            } catch (NoSuchFieldException ignored) {
                // Try other common field names
                try {
                    java.lang.reflect.Field flightField = entityClass.getDeclaredField("flightMode");
                    flightField.setAccessible(true);
                    flightField.setBoolean(mob, true);
                } catch (NoSuchFieldException ignored2) {
                }
            }
        } catch (Exception e) {
            // If reflection fails, we'll just use setNoGravity
        }
    }

    /**
     * Try to trigger wing flapping animation using Changed mod's methods
     */
    private void tryTriggerWingFlap() {
        try {
            Class<?> entityClass = mob.getClass();
            
            // Look for methods related to wings/flapping
            java.lang.reflect.Method[] methods = entityClass.getMethods();
            for (java.lang.reflect.Method method : methods) {
                String methodName = method.getName().toLowerCase();
                // Try methods like flapWings, setWingFlap, animateWings, etc.
                if ((methodName.contains("wing") || methodName.contains("flap")) && 
                    method.getParameterCount() <= 1) {
                    try {
                        if (method.getParameterCount() == 0) {
                            method.invoke(mob);
                        } else if (method.getParameterTypes()[0] == boolean.class) {
                            method.invoke(mob, true);
                        } else if (method.getParameterTypes()[0] == float.class || 
                                   method.getParameterTypes()[0] == Float.class) {
                            method.invoke(mob, 1.0f);
                        }
                        return;
                    } catch (Exception ignored) {
                    }
                }
            }
            
            // Try to find and set a wing flapping field
            try {
                java.lang.reflect.Field[] fields = entityClass.getDeclaredFields();
                for (java.lang.reflect.Field field : fields) {
                    String fieldName = field.getName().toLowerCase();
                    if (fieldName.contains("wing") || fieldName.contains("flap") || 
                        fieldName.contains("flying")) {
                        field.setAccessible(true);
                        Class<?> fieldType = field.getType();
                        if (fieldType == boolean.class || fieldType == Boolean.class) {
                            field.setBoolean(mob, true);
                        } else if (fieldType == float.class || fieldType == Float.class) {
                            field.setFloat(mob, 1.0f);
                        }
                    }
                }
            } catch (Exception ignored) {
            }
            
            // Try to trigger animation state if it exists
            try {
                java.lang.reflect.Field animField = entityClass.getDeclaredField("wingFlapAnimationState");
                animField.setAccessible(true);
                Object animState = animField.get(mob);
                if (animState != null && animState.getClass().getName().contains("AnimationState")) {
                    java.lang.reflect.Method startMethod = animState.getClass().getMethod("start", int.class);
                    startMethod.invoke(animState, mob.tickCount);
                }
            } catch (Exception ignored) {
            }
            
        } catch (Exception e) {
            // If reflection fails, try alternative approach
            // The wing flapping might be handled client-side by the Changed mod's renderer
        }
    }
}

