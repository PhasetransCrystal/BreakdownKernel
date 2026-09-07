package net.ptcrys.breakdown.data;

import net.ptcrys.breakdown.BreaLib;
import net.ptcrys.breakdown.data.material.BuiltinBdMaterials;
import net.ptcrys.breakdown.data.ore.BuiltinBdOreVeins;
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

    @Override
    public void registerLang(LangDomainRegistration lang) {
        TopoPlugin.super.registerLang(lang);
    }

    @Override
    public void registerOreDatagen(OreDomainRegistration ore) {
        TopoPlugin.super.registerOreDatagen(ore);
    }

    @Override
    public void registerOreVeins(OreDomainRegistration ore) {
        BuiltinBdOreVeins.init();
    }

    @Override
    public void registerOreDisplays(OreDomainRegistration ore) {
        TopoPlugin.super.registerOreDisplays(ore);
    }

    @Override
    public void registerOreFoundation(OreDomainRegistration ore) {
        TopoPlugin.super.registerOreFoundation(ore);
    }

    @Override
    public void registerMachineFollowUps(MachineDomainRegistration machine) {
        TopoPlugin.super.registerMachineFollowUps(machine);
    }

    @Override
    public void registerMachines(MachineDomainRegistration machine) {
        TopoPlugin.super.registerMachines(machine);
    }

    @Override
    public void registerMachineFoundation(MachineDomainRegistration machine) {
        TopoPlugin.super.registerMachineFoundation(machine);
    }

    @Override
    public void registerEquipmentKinds(EquipmentDomainRegistration equipment) {
        TopoPlugin.super.registerEquipmentKinds(equipment);
    }

    @Override
    public void registerMaterialFollowUps(MaterialDomainRegistration material) {
        TopoPlugin.super.registerMaterialFollowUps(material);
    }

    @Override
    public void registerMaterialCreativeTabs(MaterialDomainRegistration material) {
        TopoPlugin.super.registerMaterialCreativeTabs(material);
    }

    @Override
    public void registerMaterials(MaterialDomainRegistration material) {
        BuiltinBdMaterials.init();
    }

    @Override
    public void registerMaterialFoundation(MaterialDomainRegistration material) {
        TopoPlugin.super.registerMaterialFoundation(material);
    }

    @Override
    public void registerRecipes(RecipeDomainRegistration recipe) {
        TopoPlugin.super.registerRecipes(recipe);
    }

    @Override
    public void registerRecipeTypes(RecipeDomainRegistration recipe) {
        TopoPlugin.super.registerRecipeTypes(recipe);
    }

    @Override
    public void registerRecipeFoundation(RecipeDomainRegistration recipe) {
        TopoPlugin.super.registerRecipeFoundation(recipe);
    }
}
