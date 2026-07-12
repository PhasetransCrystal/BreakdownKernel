package net.phasetranscrystal.breacore.common.data;

import net.phasetranscrystal.registrylib.composite.ComponentItem;
import net.phasetranscrystal.registrylib.util.entry.ItemEntry;

import net.phasetranscrystal.breacore.data.items.DebugItems;

import static net.phasetranscrystal.breacore.common.BreaRegistration.REGISTRATE;

public class BreaItems {

    static {
        REGISTRATE.defaultCreativeTab(BreaCreativeModeTabs.ITEM.getKey());
    }
    public static ItemEntry<ComponentItem> TestItem;

    public static void init() {
        DebugItems.init();
    }
}
