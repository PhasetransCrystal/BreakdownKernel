package net.phasetranscrystal.breacore.common.data;

import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistryCore;
import net.phasetranscrystal.breacore.common.block.debug.CheckMatBlock;
import net.phasetranscrystal.breacore.common.block.debug.FluidFurnaceBlock;
import net.phasetranscrystal.breacore.data.blocks.DebugBlocks;
import net.phasetranscrystal.registrylib.util.entry.BlockEntry;

import static net.phasetranscrystal.breacore.BreakdownCore.REGISTRATE;

public class BreaBlocks {

    public static BlockEntry<CheckMatBlock> MatCheckBlock;
    public static BlockEntry<FluidFurnaceBlock> FluidFurnaceBlock = REGISTRATE.block("fluid_furnace", FluidFurnaceBlock::new)
            .simpleItem()
            .lang("Fluid Furnace")
            .lang(BreaRegistryCore.LANG_ZH_CN, "流体熔炉")
            .register();

    public static void init() {
        DebugBlocks.init();
    }
}
