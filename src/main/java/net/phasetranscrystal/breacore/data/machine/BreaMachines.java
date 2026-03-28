package net.phasetranscrystal.breacore.data.machine;

import static net.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;
import static net.phasetranscrystal.breacore.data.misc.BreaCreativeModeTabs.MACHINE;

public class BreaMachines {

    static {
        REGISTRATE.creativeModeTab(() -> MACHINE);
    }

    public static void init() {}
}
