package net.ptcrys.breakdown.common.data;

import net.ptcrys.breakdown.common.block.debug.CheckMatBlock;
import net.ptcrys.breakdown.common.block.debug.FluidFurnaceBlock;
import net.ptcrys.breakdown.data.blocks.DebugBlocks;
import net.ptcrys.breakdown.data.blocks.GeneralBlocks;
import net.ptcrys.registrylib.util.entry.BlockEntry;

import static net.ptcrys.breakdown.common.BreaRegistration.REGISTRATE;

public class BreaBlocks {

    static {
        REGISTRATE.defaultCreativeTab(BreaCreativeModeTabs.GENERAL_BLOCK.getKey());
    }

    public static BlockEntry<CheckMatBlock> MatCheckBlock;
    public static BlockEntry<FluidFurnaceBlock> FluidFurnaceBlock;

    public static void init() {
        DebugBlocks.init();
        GeneralBlocks.init();
    }
}
