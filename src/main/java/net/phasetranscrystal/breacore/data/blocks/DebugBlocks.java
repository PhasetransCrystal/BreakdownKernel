package net.phasetranscrystal.breacore.data.blocks;

import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistryCore;
import net.phasetranscrystal.breacore.common.block.debug.CheckMatBlock;
import net.phasetranscrystal.breacore.common.data.BreaTooltips;

import net.minecraft.network.chat.Component;

import static net.phasetranscrystal.breacore.common.BreaRegistration.*;
import static net.phasetranscrystal.breacore.common.data.BreaBlocks.*;
import static net.phasetranscrystal.breacore.common.data.BreaCreativeModeTabs.*;

public class DebugBlocks {

    public static void init() {
        REGISTRATE.defaultCreativeTab(DEBUG_ITEMS.getKey());
        MatCheckBlock = DEBUG_REGISTRATE.block("mat_check", CheckMatBlock::new)
                .item((builder) -> builder
                        .addTooltip(Component.translatable(BreaTooltips.Debug)))
                .lang("Material Check Block")
                .lang(BreaRegistryCore.LANG_ZH_CN, "材料检测方块")
                .register();
    }
}
