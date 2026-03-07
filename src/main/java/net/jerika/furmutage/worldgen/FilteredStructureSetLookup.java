package net.jerika.furmutage.worldgen;

import net.jerika.furmutage.config.ModCommonConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Wraps a HolderLookup&lt;StructureSet&gt; and, when config disables vanilla structures,
 * filters out vanilla structure sets so they are not used for placement.
 */
public final class FilteredStructureSetLookup implements HolderLookup<StructureSet> {

    private final HolderLookup<StructureSet> delegate;
    private final Set<ResourceLocation> disabledSetIds;

    public FilteredStructureSetLookup(HolderLookup<StructureSet> delegate, Set<ResourceLocation> disabledSetIds) {
        this.delegate = delegate;
        this.disabledSetIds = disabledSetIds;
    }

    public static HolderLookup<StructureSet> wrap(HolderLookup<StructureSet> original) {
        if (!ModCommonConfig.DISABLE_VANILLA_STRUCTURES.get()) {
            return original;
        }
        Set<ResourceLocation> ids = net.jerika.furmutage.event.VanillaStructureDisableEvents.getVanillaStructureSetIds();
        if (ids.isEmpty()) {
            return original;
        }
        return new FilteredStructureSetLookup(original, ids);
    }

    private boolean isDisabled(ResourceLocation id) {
        return id != null && disabledSetIds.contains(id);
    }

    @Override
    public Optional<Holder.Reference<StructureSet>> get(ResourceKey<StructureSet> key) {
        if (isDisabled(key.location())) {
            return Optional.empty();
        }
        return delegate.get(key);
    }

    @Override
    public Optional<HolderSet.Named<StructureSet>> get(TagKey<StructureSet> tag) {
        return delegate.get(tag);
    }

    @Override
    public Stream<Holder.Reference<StructureSet>> listElements() {
        return delegate.listElements()
                .filter(ref -> !isDisabled(ref.key().location()));
    }

    @Override
    public Stream<HolderSet.Named<StructureSet>> listTags() {
        return delegate.listTags();
    }
}
