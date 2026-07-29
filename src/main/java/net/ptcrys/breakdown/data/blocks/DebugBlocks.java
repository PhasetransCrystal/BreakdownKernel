package net.ptcrys.breakdown.data.blocks;

import net.ptcrys.breakdown.api.registry.registrate.BreaRegistryCore;
import net.ptcrys.breakdown.common.block.debug.CheckMatBlock;
import net.ptcrys.breakdown.common.data.BreaTooltips;

import net.minecraft.network.chat.Component;

import static net.ptcrys.breakdown.common.BreaRegistration.*;
import static net.ptcrys.breakdown.common.data.BreaBlocks.*;
import static net.ptcrys.breakdown.common.data.BreaCreativeModeTabs.*;

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
