package net.phasetranscrystal.breacore.data.items;

import net.phasetranscrystal.breacore.common.data.BreaCreativeModeTabs;

import static net.phasetranscrystal.breacore.BreakdownCore.REGISTRATE;

public class DebugItems {

    static {
        REGISTRATE.defaultCreativeTab(BreaCreativeModeTabs.DEBUG_ITEMS.getKey());
    }

    public static void init() {}
}
