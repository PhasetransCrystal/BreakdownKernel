package net.ptcrys.breakdown.common.data;

import net.ptcrys.breakdown.api.machine.MachineDefinition;
import net.ptcrys.breakdown.data.machine.DebugMachines;

import static net.ptcrys.breakdown.common.BreaRegistration.REGISTRATE;
import static net.ptcrys.breakdown.common.data.BreaCreativeModeTabs.MACHINE;

public class BreaMachines {

    static {
        REGISTRATE.defaultCreativeTab(MACHINE.getKey());
    }

    public static MachineDefinition TestMachine;

    public static void init() {
        DebugMachines.init();
    }
}
