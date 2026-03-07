package net.jerika.furmutage.mixins;

import net.jerika.furmutage.worldgen.FilteredStructureSetLookup;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * When disableVanillaStructures config is true, wraps the structure set lookup
 * so vanilla structure sets are excluded from placement (they do not generate).
 */
@Mixin(ChunkGenerator.class)
public class ChunkGeneratorStructureSetFilterMixin {

    @ModifyVariable(
            method = "createStructurePlacementCalculator",
            at = @At("HEAD"),
            argsOnly = true
    )
    private HolderLookup<StructureSet> furmutage_wrapStructureSetLookup(HolderLookup<StructureSet> original) {
        return FilteredStructureSetLookup.wrap(original);
    }
}
