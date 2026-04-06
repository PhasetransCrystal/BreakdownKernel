package net.phasetranscrystal.breacore.api.addon;

import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistrate;

@SuppressWarnings("unused")
public interface IBreaAddon {

    BreaRegistrate getRegistrate();

    void initComplete();

    void addElement();

    void addMaterial();

    void addMaterialVariant();
}
