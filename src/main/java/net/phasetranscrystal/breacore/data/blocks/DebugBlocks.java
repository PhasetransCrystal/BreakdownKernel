package net.phasetranscrystal.breacore.data.blocks;

import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistryCore;
import net.phasetranscrystal.breacore.common.block.debug.CheckMatBlock;
import net.phasetranscrystal.breacore.common.data.BreaCreativeModeTabs;

import static net.phasetranscrystal.breacore.BreakdownCore.REGISTRATE;
import static net.phasetranscrystal.breacore.common.data.BreaBlocks.MatCheckBlock;

public class DebugBlocks {

    static {
        REGISTRATE.defaultCreativeTab(BreaCreativeModeTabs.DEBUG_ITEMS.getKey());
    }

    public static void init() {
        MatCheckBlock = REGISTRATE.block("mat_check", CheckMatBlock::new)
                .simpleItem()
                .lang("Material Check Block")
                .lang(BreaRegistryCore.LANG_ZH_CN, "材料检测方块")
                .register();
    }
}
