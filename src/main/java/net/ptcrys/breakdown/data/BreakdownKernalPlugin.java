package net.ptcrys.breakdown.data;

import net.ptcrys.breakdown.BreaLib;
import net.ptcrys.breakdown.data.material.BuiltinBdMaterials;
import net.ptcrys.breakdown.data.ore.BuiltinBdOreVeins;
import net.ptcrys.topo.api.api.builtin.*;
import net.ptcrys.topo.api.api.plugin.TopoPlugin;

import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class BreakdownKernalPlugin extends TopoPlugin {

    public static final BreakdownKernalPlugin INSTANCE = new BreakdownKernalPlugin();

    private BreakdownKernalPlugin() {
        super(BreaLib.REGISTRY);
    }

    @Override
    public void registerEquipmentKinds(EquipmentDomainRegistration equipment) {
        super.registerEquipmentKinds(equipment);
    }

    @Override
    public void registerLang(LangDomainRegistration lang) {
        super.registerLang(lang);
    }

    @Override
    public void registerMachineFoundation(MachineDomainRegistration machine) {
        super.registerMachineFoundation(machine);
    }

    @Override
    public void registerMachines(MachineDomainRegistration machine) {
        super.registerMachines(machine);
    }

    @Override
    public void registerMachineFollowUps(MachineDomainRegistration machine) {
        super.registerMachineFollowUps(machine);
    }

    @Override
    public void registerMaterialFoundation(MaterialDomainRegistration material) {
        super.registerMaterialFoundation(material);
    }

    @Override
    public void registerMaterialCreativeTabs(MaterialDomainRegistration material) {
        super.registerMaterialCreativeTabs(material);
    }

    @Override
    public void registerMaterialFollowUps(MaterialDomainRegistration material) {
        super.registerMaterialFollowUps(material);
    }

    @Override
    public void registerMaterials(MaterialDomainRegistration material) {
        BuiltinBdMaterials.init();
    }

    @Override
    public void registerOreFoundation(OreDomainRegistration ore) {
        super.registerOreFoundation(ore);
    }

    @Override
    public void registerOreDisplays(OreDomainRegistration ore) {
        super.registerOreDisplays(ore);
    }

    @Override
    public void registerOreDatagen(OreDomainRegistration ore) {
        super.registerOreDatagen(ore);
    }

    @Override
    public void registerOreVeins(OreDomainRegistration ore) {
        BuiltinBdOreVeins.init();
    }

    @Override
    public void registerRecipeFoundation(RecipeDomainRegistration recipe) {
        super.registerRecipeFoundation(recipe);
    }

    @Override
    public void registerRecipeTypes(RecipeDomainRegistration recipe) {
        super.registerRecipeTypes(recipe);
    }

    @Override
    public void registerRecipes(RecipeDomainRegistration recipe) {
        super.registerRecipes(recipe);
    }
}
