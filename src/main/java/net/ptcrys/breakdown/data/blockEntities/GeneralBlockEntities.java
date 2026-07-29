package net.ptcrys.breakdown.data.blockEntities;

import net.ptcrys.breakdown.common.blockentity.debug.FluidFurnaceBlockEntity;
import net.ptcrys.breakdown.common.data.BreaBlocks;

import static net.ptcrys.breakdown.common.BreaRegistration.*;
import static net.ptcrys.breakdown.common.data.BreaBlockEntities.*;

public class GeneralBlockEntities {

    public static void init() {
        FurnaceBlockEntity = REGISTRATE.blockEntity("fluid_furnace", FluidFurnaceBlockEntity::new)
                .validBlock(BreaBlocks.FluidFurnaceBlock)
                .register();
    }
}
