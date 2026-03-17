package net.phasetranscrystal.breacore.data.items;

import net.phasetranscrystal.breacore.data.misc.BreaCreativeModeTabs;

import static net.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;

public class DebugItems {

    static {
        REGISTRATE.creativeModeTab(() -> BreaCreativeModeTabs.DEBUG_ITEMS);
    }

    public static void init() {}
}
