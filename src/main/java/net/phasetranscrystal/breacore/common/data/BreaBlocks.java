package net.phasetranscrystal.breacore.common.data;

import net.phasetranscrystal.registrylib.util.entry.BlockEntry;

import net.phasetranscrystal.breacore.common.block.debug.CheckMatBlock;
import net.phasetranscrystal.breacore.common.block.debug.FluidFurnaceBlock;
import net.phasetranscrystal.breacore.data.blocks.DebugBlocks;
import net.phasetranscrystal.breacore.data.blocks.GeneralBlocks;

import static net.phasetranscrystal.breacore.common.BreaRegistration.REGISTRATE;

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
