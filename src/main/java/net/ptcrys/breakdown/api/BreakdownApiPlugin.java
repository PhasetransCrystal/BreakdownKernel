package net.ptcrys.breakdown.api;

import net.ptcrys.breakdown.BreaLib;
import net.ptcrys.breakdown.api.machine.*;
import net.ptcrys.breakdown.api.machine.common.component.resource.*;
import net.ptcrys.breakdown.api.material.*;
import net.ptcrys.breakdown.api.ore.*;
import net.ptcrys.breakdown.api.ore.bindings.VanillaOreFeatureBridge;
import net.ptcrys.breakdown.api.recipe.*;
import net.ptcrys.breakdown.api.vanilla.*;
import net.ptcrys.registrylib.RegistryCore;
import net.ptcrys.topo.api.api.builtin.*;
import net.ptcrys.topo.api.api.plugin.TopoPlugin;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 瓦解核心内容插件(骨架):注册内置构件——材料数据类型/形态、配方类型、资源集成、UI 图标、
 * 创造栏与基础文案。具体材料/机器等产品内容由另一个 plugin 通过同一 {@link TopoPlugin} 路径注册。
 */
@Accessors(fluent = true)
public class BreakdownApiPlugin implements TopoPlugin {

    public static final BreakdownApiPlugin INSTANCE = new BreakdownApiPlugin();
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

    private BreakdownApiPlugin() {}

    @Override
    public String modId() {
        return BreaLib.Core_ID;
    }

    @Override
    public RegistryCore registry() {
        return BreaLib.REGISTRY;
    }

    // ── recipe ───────────────────────────────────────────────────────────────

    @Override
    public void registerRecipeFoundation(RecipeDomainRegistration recipe) {
        // Pairs machine resource types with recipe capabilities (ITEM/FLUID/ENERGY/…).
        BuiltinBdResourceIntegrations.init();
    }

    @Override
    public void registerRecipeTypes(RecipeDomainRegistration recipe) {
        // BuiltinBdProductionLines.init();
        BuiltinBdRecipeTypes.init();
    }

    @Override
    public void registerRecipes(RecipeDomainRegistration recipe) {
        // BuiltinBdProductionLineRecipes.init();
    }

    // ── material ─────────────────────────────────────────────────────────────

    @Override
    public void registerMaterialFoundation(MaterialDomainRegistration material) {
        BuiltinBdMaterialDataTypes.init();
        BuiltinBdFormDataTypes.init();
        BuiltinBdCreativeTabs.init();
        BuiltinBdMaterialForms.init();
        BuiltinBdMaterialFormLang.init();
    }

    @Override
    public void registerMaterials(MaterialDomainRegistration material) {}

    @Override
    public void registerMaterialCreativeTabs(MaterialDomainRegistration material) {
        BuiltinBdCreativeTabs.init();
    }

    @Override
    public void registerMaterialFollowUps(MaterialDomainRegistration material) {
        // Production-line recipes moved to registerRecipes (recipe domain).
    }

    // ── equipment / tools ────────────────────────────────────────────────────

    @Override
    public void registerEquipmentKinds(EquipmentDomainRegistration equipment) {
        BuiltinBdEquipment.init();
        BuiltinBdEquipmentLang.init();
    }

    // ── machine ──────────────────────────────────────────────────────────────

    @Override
    public void registerMachineFoundation(MachineDomainRegistration machine) {
        BuiltinBdCreativeTabs.init();
        // Resource integrations already run in registerRecipeFoundation; re-touch is no-op.
        BuiltinBdResourceIntegrations.init();
        BuiltinBdMachineRenderTypes.init();
        BuiltinBdPartRoles.init();
        BuiltinBdPropertyDisplays.init();
        ItemResourcePortMetadata.init();
        FluidResourcePortMetadata.init();
        ScalarResourcePortMetadata.init();
    }

    @Override
    public void registerMachines(MachineDomainRegistration machine) {
        // Catalog order: casings → parts → controllers → ME → single-block → gametest fixtures.
        BuiltinBdCasingBlocks.init();
        BuiltinBdPartMachines.init();
        BuiltinBdControllerMachines.init();
        BuiltinBdMeMachines.init();
        BuiltinBdMachines.init();
    }

    @Override
    public void registerMachineFollowUps(MachineDomainRegistration machine) {
        // Machine workbench + process recipes after catalogs freeze (see code-style §3.11).
        BuiltinBdMachineCraftingRecipes.initRecipes();
        // Pipes remain product tables on the official plugin until a dedicated pipe domain exists.
        BuiltinBdPipeDistributionStrategies.init();
        BuiltinBdPipes.init();
        BuiltinBdPipeLang.init();
    }

    // ── ore ──────────────────────────────────────────────────────────────────

    @Override
    public void registerOreFoundation(OreDomainRegistration ore) {
        BuiltinBdOreLang.init();
        BuiltinBdOreShapes.init();
        BuiltinBdOreModes.init();
        BuiltinBdOrePolicies.init();
    }

    @Override
    public void registerOreDisplays(OreDomainRegistration ore) {
        BuiltinBdOreVeinDisplays.init();
    }

    @Override
    public void registerOreVeins(OreDomainRegistration ore) {
        BuiltinBdOreEnvironments.init();
        // BuiltinBdOreVeins.init();
    }

    @Override
    public void registerOreDatagen(OreDomainRegistration ore) {
        VanillaOreFeatureBridge.registerDatagen(ore.registry());
    }

    // ── lang (product catalogs; API keys live on OfficialTopoAPIPlugin) ─────────

    @Override
    public void registerLang(LangDomainRegistration lang) {
        BuiltinBdPipeLang.init();
        BuiltinBdMachineUiLang.init();
        BuiltinBdMachineFeedbackLang.init();
        BuiltinBdEquipmentLang.init();
        BuiltinBdMaterialFormLang.init();
        BuiltinBdOreLang.init();
    }
}
