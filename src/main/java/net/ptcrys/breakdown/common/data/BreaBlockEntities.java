package net.ptcrys.breakdown.common.data;

import net.ptcrys.breakdown.common.blockentity.debug.FluidFurnaceBlockEntity;
import net.ptcrys.breakdown.data.blockEntities.GeneralBlockEntities;
import net.ptcrys.registrylib.util.entry.BlockEntityTypeEntry;

public class BreaBlockEntities {

    public static BlockEntityTypeEntry<FluidFurnaceBlockEntity> FurnaceBlockEntity;

    public static void init() {
        GeneralBlockEntities.init();
    }
}
