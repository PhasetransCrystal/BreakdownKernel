package net.ptcrys.breakdown.data.blocks;

import net.ptcrys.breakdown.BreaLib;
import net.ptcrys.breakdown.common.block.debug.FluidFurnaceBlock;
import net.ptcrys.breakdown.data.vanilla.BdCreativeTabs;
import net.ptcrys.registrylib.util.entry.BlockEntry;

import static net.ptcrys.breakdown.BreaLib.LangZhCn;

public final class BdBlocks {

    public static final BlockEntry<FluidFurnaceBlock> FluidFurnace = BreaLib.REGISTRY.block("fluid_furnace", FluidFurnaceBlock::new)
            .lang("Fluid Furnace")
            .lang(LangZhCn, "流体熔炉")
            .defaultItemTab(BdCreativeTabs.FUNCTIONAL.getKey())
            .register();

    private BdBlocks() {}

    public static void init() {
        DebugBlocks.init();
    }
}
