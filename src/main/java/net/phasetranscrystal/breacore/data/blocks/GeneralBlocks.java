package net.phasetranscrystal.breacore.data.blocks;

import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistryCore;
import net.phasetranscrystal.breacore.common.block.debug.FluidFurnaceBlock;
import net.phasetranscrystal.breacore.common.data.translation.ComponentSlang;

import static net.phasetranscrystal.breacore.common.BreaRegistration.*;
import static net.phasetranscrystal.breacore.common.data.BreaBlocks.*;
import static net.phasetranscrystal.breacore.common.data.BreaCreativeModeTabs.*;

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
