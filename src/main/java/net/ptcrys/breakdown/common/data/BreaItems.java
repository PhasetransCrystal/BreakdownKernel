package net.ptcrys.breakdown.common.data;

import net.ptcrys.breakdown.data.items.DebugItems;
import net.ptcrys.registrylib.composite.ComponentItem;
import net.ptcrys.registrylib.util.entry.ItemEntry;

import static net.ptcrys.breakdown.common.BreaRegistration.REGISTRATE;

public class BreaItems {

    static {
        REGISTRATE.defaultCreativeTab(BreaCreativeModeTabs.ITEM.getKey());
    }
    public static ItemEntry<ComponentItem> TestItem;

    public static void init() {
        DebugItems.init();
    }
}
