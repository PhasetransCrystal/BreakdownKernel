package net.ptcrys.breakdown.data;

import net.ptcrys.breakdown.BreaLib;
import net.ptcrys.breakdown.data.material.*;
import net.ptcrys.breakdown.data.recipe.*;
import net.ptcrys.breakdown.data.vanilla.*;
import net.ptcrys.registrylib.RegistryCore;
import net.ptcrys.topo.api.api.builtin.*;
import net.ptcrys.topo.api.api.plugin.TopoPlugin;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class BreakdownPlugin implements TopoPlugin {

    public static final BreakdownPlugin INSTANCE = new BreakdownPlugin();
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

    private BreakdownPlugin() {}

    @Override
    public String modId() {
        return BreaLib.Core_ID;
    }

    @Override
    public RegistryCore registry() {
        return BreaLib.REGISTRY;
    }

    @Override
    public void registerRecipeFoundation(RecipeDomainRegistration recipe) {
        BdResourceIntegrations.init();
    }

    @Override
    public void registerRecipeTypes(RecipeDomainRegistration recipe) {
        BdProductionLines.init();
        BdRecipeTypes.init();
    }

    @Override
    public void registerRecipes(RecipeDomainRegistration recipe) {
        BdProductionLines.init();
    }

    @Override
    public void registerMaterialFoundation(MaterialDomainRegistration material) {
        BdMaterialDataTypes.init();
        BdFormDataTypes.init();
        BdCreativeTabs.init();
        BdMaterialForms.init();
        BdMaterialFormLang.init();
    }

    @Override
    public void registerMaterials(MaterialDomainRegistration material) {
        BdMaterials.init();
    }

    @Override
    public void registerMaterialCreativeTabs(MaterialDomainRegistration material) {
        BdCreativeTabs.init();
    }

    @Override
    public void registerMaterialFollowUps(MaterialDomainRegistration material) {}

    @Override
    public void registerEquipmentKinds(EquipmentDomainRegistration equipment) {}

    @Override
    public void registerMachineFoundation(MachineDomainRegistration machine) {}

    @Override
    public void registerMachines(MachineDomainRegistration machine) {}

    @Override
    public void registerMachineFollowUps(MachineDomainRegistration machine) {}

    @Override
    public void registerOreFoundation(OreDomainRegistration ore) {}

    @Override
    public void registerOreDisplays(OreDomainRegistration ore) {}

    @Override
    public void registerOreVeins(OreDomainRegistration ore) {}

    @Override
    public void registerOreDatagen(OreDomainRegistration ore) {}

    @Override
    public void registerLang(LangDomainRegistration lang) {}
}
