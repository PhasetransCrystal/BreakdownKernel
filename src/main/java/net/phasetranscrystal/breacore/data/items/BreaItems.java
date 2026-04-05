package net.phasetranscrystal.breacore.data.items;

import net.phasetranscrystal.breacore.api.misc.AutoInitializeImpl;

public class BreaItems {

    public static void init() {
        AutoInitializeImpl.INSTANCE.originInit();
        DebugItems.init();
    }
}
