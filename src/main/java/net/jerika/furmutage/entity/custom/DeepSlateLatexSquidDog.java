package net.jerika.furmutage.entity.custom;

import net.ltxprogrammer.changed.entity.Gender;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.beast.AbstractLatexSquidDog;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class DeepSlateLatexSquidDog extends AbstractLatexSquidDog {
    public DeepSlateLatexSquidDog(EntityType<? extends DeepSlateLatexSquidDog> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.ABSORPTION;
    }

    @Override
    public Gender getGender() {
        return Gender.FEMALE;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes();
    }

    // Custom spawn rule used by SpawnPlacements.register (use a unique name to avoid erasure clashes)
    public static boolean checkDeepSlateSpawnRules(EntityType<DeepSlateLatexSquidDog> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        // Check if we're at deepslate level (Y < 0)
        if (pos.getY() >= 0) {
            return false;
        }
        
        // Check if we're in water
        if (!world.getFluidState(pos).is(FluidTags.WATER)) {
            return false;
        }
        
        // Check if the block below is also water (underwater spawning)
        if (!world.getFluidState(pos.below()).is(FluidTags.WATER)) {
            return false;
        }
        
        // Additional random chance to control spawn rate
        return random.nextFloat() < 0.1f; // 10% chance
    }
}
