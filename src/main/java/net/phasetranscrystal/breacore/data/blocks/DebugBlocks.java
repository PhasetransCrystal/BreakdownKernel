package net.phasetranscrystal.breacore.data.blocks;

import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistryCore;
import net.phasetranscrystal.breacore.common.block.debug.CheckMatBlock;
import net.phasetranscrystal.breacore.common.data.BreaCreativeModeTabs;
import net.phasetranscrystal.breacore.common.data.BreaTooltips;

import static net.phasetranscrystal.breacore.common.BreaRegistration.DEBUG_REGISTRATE;
import static net.phasetranscrystal.breacore.common.BreaRegistration.REGISTRATE;
import static net.phasetranscrystal.breacore.common.data.BreaBlocks.MatCheckBlock;

public class DebugBlocks {

    static {
        REGISTRATE.defaultCreativeTab(BreaCreativeModeTabs.DEBUG_ITEMS.getKey());
    }

    public static void init() {
        MatCheckBlock = DEBUG_REGISTRATE.block("mat_check", CheckMatBlock::new)
                .item((builder) -> builder
                        .addTooltip(BreaTooltips.DebugItem))
                .lang("Material Check Block")
                .lang(BreaRegistryCore.LANG_ZH_CN, "材料检测方块")
                .register();
    }
}
