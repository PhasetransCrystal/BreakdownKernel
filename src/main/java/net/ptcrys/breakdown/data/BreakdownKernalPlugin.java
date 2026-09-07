package net.ptcrys.breakdown.data;

import net.ptcrys.breakdown.BreaLib;
import net.ptcrys.registrylib.RegistryCore;
import net.ptcrys.topo.api.api.builtin.*;
import net.ptcrys.topo.api.api.plugin.TopoPlugin;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class BreakdownKernalPlugin implements TopoPlugin {

    public static final BreakdownKernalPlugin INSTANCE = new BreakdownKernalPlugin();
    @Getter
    private final MaterialDomainRegistration material = MaterialDomainRegistration.of(this);
    @Getter
    private final EquipmentDomainRegistration equipment = EquipmentDomainRegistration.of(this);
    @Getter
    private final RecipeDomainRegistration recipe = RecipeDomainRegistration.of(this);
    @Getter
    private final MachineDomainRegistration machine = MachineDomainRegistration.of(this);
    @Getter
    private final OreDomainRegistration ore = OreDomainRegistration.of(this);
    @Getter
    private final LangDomainRegistration lang = LangDomainRegistration.of(this);

    private BreakdownKernalPlugin() {}

    @Override
    public String modId() {
        return BreaLib.Core_ID;
    }

    @Override
    public RegistryCore registry() {
        return BreaLib.REGISTRY;
    }
}
