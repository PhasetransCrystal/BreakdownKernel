package net.ptcrys.breakdown.data.items;

import net.ptcrys.breakdown.api.registry.registrate.BreaRegistryCore;
import net.ptcrys.breakdown.common.data.BreaTooltips;

import net.minecraft.network.chat.Component;

import static net.ptcrys.breakdown.common.BreaRegistration.*;
import static net.ptcrys.breakdown.common.data.BreaCreativeModeTabs.*;
import static net.ptcrys.breakdown.common.data.BreaItems.*;

public class DebugItems {

    public static void init() {
        REGISTRATE.defaultCreativeTab(DEBUG_ITEMS.getKey());
        TestItem = REGISTRATE.componentItem("test_item")
                .addTooltip(Component.translatable(BreaTooltips.Debug))
                .lang("Test Item")
                .lang(BreaRegistryCore.LANG_ZH_CN, "测试用物品")
                .register();
    }
}
