package net.jerika.furmutage.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

/**
 * Custom wood types for tainted white/dark wood.
 */
public class ModWoodTypes {

    public static final WoodType TAINTED_WHITE = register(
            "tainted_white",
            BlockSetType.OAK,
            SoundType.WOOD
    );

    public static final WoodType TAINTED_DARK = register(
            "tainted_dark",
            BlockSetType.OAK,
            SoundType.WOOD
    );

    private static WoodType register(String name, BlockSetType setType, SoundType soundType) {
        // Use namespaced ID so textures resolve under our modid.
        String fullName = "furmutage:" + name;
        WoodType woodType = new WoodType(
                fullName,
                setType,
                soundType,
                soundType,
                SoundEvents.FENCE_GATE_CLOSE,
                SoundEvents.FENCE_GATE_OPEN
        );
        return WoodType.register(woodType);
    }
}

