package net.phasetranscrystal.breacore.data.blocks;

import net.phasetranscrystal.breacore.api.block.debug.CheckMatBlock;
import net.phasetranscrystal.breacore.api.block.debug.MuiTestBlock;
import net.phasetranscrystal.breacore.api.blockentity.debug.TestBlockEntity;
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
        TestMuiBlock = REGISTRATE.block("mui_test_block", MuiTestBlock::new)
                .simpleBlockEntity(TestBlockEntity::new)
                .item()
                .build()
                .lang("MUI Test Block")
                .register();
    }
}
