package net.phasetranscrystal.breacore.data.items;

import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistryCore;
import net.phasetranscrystal.breacore.common.data.BreaTooltips;

import net.minecraft.network.chat.Component;

import static net.phasetranscrystal.breacore.common.BreaRegistration.*;
import static net.phasetranscrystal.breacore.common.data.BreaCreativeModeTabs.*;
import static net.phasetranscrystal.breacore.common.data.BreaItems.*;

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
