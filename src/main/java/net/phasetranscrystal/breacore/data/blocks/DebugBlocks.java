package net.phasetranscrystal.breacore.data.blocks;

import net.phasetranscrystal.breacore.api.block.debug.CheckMatBlock;
import net.phasetranscrystal.breacore.api.block.debug.FluidFurnaceBlock;
import net.phasetranscrystal.breacore.api.blockentity.debug.FluidFurnaceBlockEntity;
import net.phasetranscrystal.breacore.data.misc.BreaCreativeModeTabs;

import static net.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;
import static net.phasetranscrystal.breacore.data.blocks.BreaBlocks.*;
import static net.phasetranscrystal.breacore.data.tags.CustomTags.DEBUG_ITEMS;

public class DebugBlocks {

    static {
        REGISTRATE.creativeModeTab(() -> BreaCreativeModeTabs.DEBUG_ITEMS);
    }

    public static void init() {
        MatCheckBlock = REGISTRATE.block("matcheckblock", CheckMatBlock::new)
                .item()
                .tag(DEBUG_ITEMS)
                .build()
                .lang("Material Check Block")
                .register();
        FluidFurnaceBlock = REGISTRATE.block("fluid_furnace", FluidFurnaceBlock::new)
                .simpleBlockEntity(FluidFurnaceBlockEntity::new)
                .item()
                .tag(DEBUG_ITEMS)
                .build()
                .lang("Fluid Furnace")
                .register();
    }
}
