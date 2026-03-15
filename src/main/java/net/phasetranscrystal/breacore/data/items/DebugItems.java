package net.phasetranscrystal.breacore.data.items;

import net.phasetranscrystal.breacore.data.misc.BreaCreativeModeTabs;

import static net.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;
import static net.phasetranscrystal.breacore.data.items.BreaItems.*;
import static net.phasetranscrystal.breacore.data.tags.CustomTags.DEBUG_ITEMS;

public class DebugItems {

    static {
        REGISTRATE.creativeModeTab(() -> BreaCreativeModeTabs.DEBUG_ITEMS);
    }

    public static void init() {}
}
