package net.phasetranscrystal.breacore.common.data;

import net.phasetranscrystal.registrylib.util.entry.BlockEntityTypeEntry;

import net.phasetranscrystal.breacore.common.blockentity.debug.FluidFurnaceBlockEntity;
import net.phasetranscrystal.breacore.data.blockEntities.GeneralBlockEntities;

public class BreaBlockEntities {

    public static BlockEntityTypeEntry<FluidFurnaceBlockEntity> FurnaceBlockEntity;

    public static void init() {
        GeneralBlockEntities.init();
    }
}
