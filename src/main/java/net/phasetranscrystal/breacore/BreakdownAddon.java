package net.phasetranscrystal.breacore;

import net.phasetranscrystal.breacore.api.addon.BreaAddon;
import net.phasetranscrystal.breacore.api.addon.IBreaAddon;
import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistryCore;

import lombok.extern.log4j.Log4j2;

@Log4j2
@BreaAddon(BreakdownCore.MOD_ID)
public class BreakdownAddon implements IBreaAddon {

    @Override
    public BreaRegistryCore getRegistrate() {
        return BreakdownCore.REGISTRATE;
    }

    @Override
    public void initComplete() {
        log.info("BreaAddon initComplete");
    }

    @Override
    public void addElement() {
        log.info("BreaAddon addElement");
    }

    @Override
    public void addMaterial() {
        log.info("BreaAddon addMaterial");
    }

    @Override
    public void addMaterialVariant() {
        log.info("BreaAddon addMaterialVariant");
    }
}
