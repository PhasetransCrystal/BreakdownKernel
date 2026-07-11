package net.phasetranscrystal.breacore.api.addon;

import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistryCore;

@SuppressWarnings("unused")
public interface IBreaAddon {

    BreaRegistryCore getRegistrate();

    void initComplete();

    void addElement();

    void addMaterial();

    void addMaterialVariant();
}
