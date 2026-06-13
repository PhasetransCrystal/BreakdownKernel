package net.phasetranscrystal.breacore.common.data;

import net.phasetranscrystal.breacore.data.machine.DebugMachines;

import static net.phasetranscrystal.breacore.BreakdownCore.REGISTRATE;
import static net.phasetranscrystal.breacore.common.data.BreaCreativeModeTabs.MACHINE;

public class BreaMachines {

    static {
        REGISTRATE.defaultCreativeTab(MACHINE.getKey());
    }

    public static void init() {
        DebugMachines.init();
    }
}
