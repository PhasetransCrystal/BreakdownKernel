package net.phasetranscrystal.breacore.data.blockEntities;

import net.phasetranscrystal.breacore.common.blockentity.debug.FluidFurnaceBlockEntity;
import net.phasetranscrystal.breacore.common.data.BreaBlocks;

import static net.phasetranscrystal.breacore.common.BreaRegistration.*;
import static net.phasetranscrystal.breacore.common.data.BreaBlockEntities.*;

public class GeneralBlockEntities {

    public static void init() {
        FurnaceBlockEntity = REGISTRATE.blockEntity("fluid_furnace", FluidFurnaceBlockEntity::new)
                .validBlock(BreaBlocks.FluidFurnaceBlock)
                .register();
    }
}
