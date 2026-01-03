package net.jerika.furmutage.entity.projectiles;

import net.jerika.furmutage.entity.ModEntities;
import net.jerika.furmutage.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.registries.ForgeRegistries;

public class DarkLatexBottleProjectile extends ThrowableItemProjectile {
    public DarkLatexBottleProjectile(EntityType<? extends DarkLatexBottleProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public DarkLatexBottleProjectile(Level level, LivingEntity shooter) {
        super(ModEntities.DARK_LATEX_BOTTLE_PROJECTILE.get(), shooter, level);
    }

    public DarkLatexBottleProjectile(Level level, double x, double y, double z) {
        super(ModEntities.DARK_LATEX_BOTTLE_PROJECTILE.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.DARKLATEXBOTTLED.get();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        
        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            // Spawn dark_latex_wolf_pup at impact location
            EntityType<?> darkLatexWolfPupType = ForgeRegistries.ENTITY_TYPES.getValue(
                    net.minecraft.resources.ResourceLocation.tryParse("changed:dark_latex_wolf_pup")
            );
            
            if (darkLatexWolfPupType != null && darkLatexWolfPupType.create(serverLevel) instanceof PathfinderMob) {
                PathfinderMob pup = (PathfinderMob) darkLatexWolfPupType.create(serverLevel);
                if (pup != null) {
                    pup.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                    try {
                        pup.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(pup.blockPosition()),
                                MobSpawnType.EVENT, null, null);
                    } catch (IllegalArgumentException e) {
                        // Catch errors from Changed mod trying to use attributes that don't exist in 1.20.1
                        // (e.g., attack_knockback)
                        if (e.getMessage() != null && e.getMessage().contains("attack_knockback")) {
                            net.jerika.furmutage.furmutage.LOGGER.debug("Ignoring attack_knockback attribute error from Changed mod: {}", e.getMessage());
                        } else {
                            throw e; // Re-throw if it's a different error
                        }
                    }
                    serverLevel.addFreshEntity(pup);
                }
            }
            
            // Spawn particles or effects here if desired
            this.level().levelEvent(2002, this.blockPosition(), 0);
            this.discard();
        }
    }
}

