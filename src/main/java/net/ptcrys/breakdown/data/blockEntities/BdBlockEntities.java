package net.ptcrys.breakdown.data.blockEntities;

import net.ptcrys.breakdown.BreaLib;
import net.ptcrys.breakdown.common.blockentity.debug.FluidFurnaceBlockEntity;
import net.ptcrys.breakdown.data.blocks.BdBlocks;
import net.ptcrys.registrylib.util.entry.BlockEntityTypeEntry;

public final class BdBlockEntities {

    public static BlockEntityTypeEntry<FluidFurnaceBlockEntity> FluidFurnace = BreaLib.REGISTRY.blockEntity("fluid_furnace", FluidFurnaceBlockEntity::new)
            .validBlock(BdBlocks.FluidFurnace)
            .register();

    public static void init() {}

    private BdBlockEntities() {}
}
