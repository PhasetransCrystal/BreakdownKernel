package net.phasetranscrystal.breacore.data.blocks;

import net.phasetranscrystal.breacore.api.block.debug.CheckMatBlock;
import net.phasetranscrystal.breacore.api.block.debug.MuiTestBlock;

import com.tterrag.registrate.util.entry.BlockEntry;

public class BreaBlocks {

    public static BlockEntry<CheckMatBlock> MatCheckBlock;

    public static BlockEntry<MuiTestBlock> TestMuiBlock;

    public static void init() {
        DebugBlocks.init();
    }
}
