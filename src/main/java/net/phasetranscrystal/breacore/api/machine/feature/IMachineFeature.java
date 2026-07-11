package net.phasetranscrystal.breacore.api.machine.feature;

import net.phasetranscrystal.breacore.api.machine.MetaMachine;

public interface IMachineFeature {

    default MetaMachine self() {
        return (MetaMachine) this;
    }
}
