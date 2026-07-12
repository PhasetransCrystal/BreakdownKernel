package net.phasetranscrystal.breacore.common.data;

import net.phasetranscrystal.breacore.api.machine.MachineDefinition;
import net.phasetranscrystal.breacore.data.machine.DebugMachines;

import static net.phasetranscrystal.breacore.common.BreaRegistration.REGISTRATE;
import static net.phasetranscrystal.breacore.common.data.BreaCreativeModeTabs.MACHINE;

public class BreaMachines {

    static {
        REGISTRATE.defaultCreativeTab(MACHINE.getKey());
    }

    public static MachineDefinition TestMachine;

    public static void init() {
        DebugMachines.init();
    }
}
