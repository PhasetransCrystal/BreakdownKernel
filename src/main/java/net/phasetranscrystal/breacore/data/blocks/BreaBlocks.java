package net.phasetranscrystal.breacore.data.blocks;

import net.phasetranscrystal.breacore.common.block.debug.CheckMatBlock;
import net.phasetranscrystal.breacore.common.block.debug.FluidFurnaceBlock;

import com.tterrag.registrate.util.entry.BlockEntry;

public class BreaBlocks {

    public static BlockEntry<CheckMatBlock> MatCheckBlock;

    public static BlockEntry<FluidFurnaceBlock> FluidFurnaceBlock;

    public static void init() {
        DebugBlocks.init();
    }
}
