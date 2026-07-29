package net.ptcrys.breakdown.api.machine.feature;

import net.ptcrys.breakdown.api.machine.MetaMachine;

public interface IMachineFeature {

    default MetaMachine self() {
        return (MetaMachine) this;
    }
}
