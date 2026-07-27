package net.ptcrys.breakdown.api.addon;

import net.ptcrys.breakdown.api.registry.registrate.BreaRegistryCore;

@SuppressWarnings("unused")
public interface IBreaAddon {

    BreaRegistryCore getRegistrate();

    void initComplete();

    void addElement();

    void addMaterial();

    void addMaterialVariant();
}
