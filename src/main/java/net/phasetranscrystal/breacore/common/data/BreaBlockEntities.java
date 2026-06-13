package net.phasetranscrystal.breacore.common.data;

import net.phasetranscrystal.breacore.common.blockentity.debug.FluidFurnaceBlockEntity;
import net.phasetranscrystal.registrylib.util.entry.BlockEntityTypeEntry;

import static net.phasetranscrystal.breacore.BreakdownCore.REGISTRATE;

public class BreaBlockEntities {

    public static BlockEntityTypeEntry<FluidFurnaceBlockEntity> FurnaceBlockEntity = REGISTRATE.blockEntity("fluid_furnace", FluidFurnaceBlockEntity::new)
            .validBlock(BreaBlocks.FluidFurnaceBlock)
            .register();

    public static void init() {}
}
