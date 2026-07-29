package net.ptcrys.breakdown;

import net.ptcrys.breakdown.api.addon.BreaAddon;
import net.ptcrys.breakdown.api.addon.IBreaAddon;
import net.ptcrys.breakdown.api.registry.registrate.BreaRegistryCore;
import net.ptcrys.breakdown.common.BreaRegistration;

import lombok.extern.log4j.Log4j2;

@Log4j2
@BreaAddon(BreaLib.Core_ID)
public class BreakdownAddon implements IBreaAddon {

    @Override
    public BreaRegistryCore getRegistrate() {
        return BreaRegistration.REGISTRATE;
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
