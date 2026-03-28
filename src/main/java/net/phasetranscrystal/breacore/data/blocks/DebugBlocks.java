package net.phasetranscrystal.breacore.data.blocks;

import net.phasetranscrystal.breacore.api.block.debug.CheckMatBlock;
import net.phasetranscrystal.breacore.api.block.debug.FluidFurnaceBlock;
import net.phasetranscrystal.breacore.api.blockentity.debug.FluidFurnaceBlockEntity;
import net.phasetranscrystal.breacore.data.misc.BreaCreativeModeTabs;

import static net.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;
import static net.phasetranscrystal.breacore.data.blocks.BreaBlocks.*;

public class DebugBlocks {

    static {
        REGISTRATE.creativeModeTab(() -> BreaCreativeModeTabs.DEBUG_ITEMS);
    }

    public static void init() {
        MatCheckBlock = REGISTRATE.block("mat_check", CheckMatBlock::new)
                .item()
                .build()
                .lang("Material Check Block")
                .register();
        FluidFurnaceBlock = REGISTRATE.block("fluid_furnace", FluidFurnaceBlock::new)
                .simpleBlockEntity(FluidFurnaceBlockEntity::new)
                .item()
                .build()
                .lang("Fluid Furnace")
                .register();
    }
}
