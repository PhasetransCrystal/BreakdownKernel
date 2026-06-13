package net.phasetranscrystal.breacore.common.data;

import net.phasetranscrystal.breacore.api.misc.AutoInitializeImpl;
import net.phasetranscrystal.breacore.data.items.DebugItems;

public class BreaItems {

    public static void init() {
        AutoInitializeImpl.INSTANCE.originInit();
        DebugItems.init();
    }
}
