package net.ptcrys.breakdown.data.blocks;

import net.ptcrys.breakdown.api.registry.registrate.BreaRegistryCore;
import net.ptcrys.breakdown.common.block.debug.FluidFurnaceBlock;
import net.ptcrys.breakdown.common.data.translation.ComponentSlang;

import static net.ptcrys.breakdown.common.BreaRegistration.*;
import static net.ptcrys.breakdown.common.data.BreaBlocks.*;
import static net.ptcrys.breakdown.common.data.BreaCreativeModeTabs.*;

public class GeneralBlocks {

    public static void init() {
        REGISTRATE.defaultCreativeTab(GENERAL_BLOCK.getKey());
        FluidFurnaceBlock = REGISTRATE.block("fluid_furnace", FluidFurnaceBlock::new)
                .item(builder -> builder
                        .addTooltip(ComponentSlang.INSTANCE.getCapacity().invoke("10 B").get()))
                .lang("Fluid Furnace")
                .lang(BreaRegistryCore.LANG_ZH_CN, "流体熔炉")
                .register();
    }
}
