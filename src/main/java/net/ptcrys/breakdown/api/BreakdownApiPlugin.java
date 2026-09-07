package net.ptcrys.breakdown.api;

import net.ptcrys.breakdown.BreaLib;
import net.ptcrys.breakdown.api.equipment.BuiltinBdApiEquipment;
import net.ptcrys.breakdown.api.equipment.BuiltinBdApiEquipmentLang;
import net.ptcrys.breakdown.api.machine.*;
import net.ptcrys.breakdown.api.machine.common.component.resource.*;
import net.ptcrys.breakdown.api.machine.multiblock.BuiltinBdApiPartRoles;
import net.ptcrys.breakdown.api.machine.multiblock.BuiltinBdApiPropertyDisplays;
import net.ptcrys.breakdown.api.material.*;
import net.ptcrys.breakdown.api.ore.*;
import net.ptcrys.breakdown.api.ore.bindings.VanillaOreFeatureBridge;
import net.ptcrys.breakdown.api.pipe.BuiltinBdApiPipeDistributionStrategies;
import net.ptcrys.breakdown.api.pipe.BuiltinBdApiPipeLang;
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
        BuiltinBdApiResourceIntegrations.init();
    }

    @Override
    public void registerRecipeTypes(RecipeDomainRegistration recipe) {
        BuiltinBdApiRecipeTypes.init();
    }

    // ── material ─────────────────────────────────────────────────────────────

    @Override
    public void registerMaterialFoundation(MaterialDomainRegistration material) {
        BuiltinBdApiMaterialDataTypes.init();
        BuiltinBdApiFormDataTypes.init();
        BuiltinBdApiCreativeTabs.init();
        BuiltinBdApiMaterialForms.init();
        BuiltinBdApiMaterialFormLang.init();
    }

    @Override
    public void registerMaterialCreativeTabs(MaterialDomainRegistration material) {
        BuiltinBdApiCreativeTabs.init();
    }

    @Override
    public void registerMaterialFollowUps(MaterialDomainRegistration material) {
        // Pipes remain product tables on the official plugin until a dedicated pipe domain exists.
        BuiltinBdApiPipeDistributionStrategies.init();
    }

    // ── equipment / tools ────────────────────────────────────────────────────

    @Override
    public void registerEquipmentKinds(EquipmentDomainRegistration equipment) {
        BuiltinBdApiEquipment.init();
        BuiltinBdApiEquipmentLang.init();
    }

    // ── machine ──────────────────────────────────────────────────────────────

    @Override
    public void registerMachineFoundation(MachineDomainRegistration machine) {
        BuiltinBdApiCreativeTabs.init();
        // Resource integrations already run in registerRecipeFoundation; re-touch is no-op.
        BuiltinBdApiResourceIntegrations.init();
        BuiltinBdApiMachineRenderTypes.init();
        BuiltinBdApiPartRoles.init();
        BuiltinBdApiPropertyDisplays.init();
        ItemResourcePortMetadata.init();
        FluidResourcePortMetadata.init();
        ScalarResourcePortMetadata.init();
    }

    // ── ore ──────────────────────────────────────────────────────────────────

    @Override
    public void registerOreFoundation(OreDomainRegistration ore) {
        BuiltinBdApiOreLang.init();
        BuiltinBdApiOreShapes.init();
        BuiltinBdApiOreModes.init();
        BuiltinBdApiOrePolicies.init();
    }

    @Override
    public void registerOreDisplays(OreDomainRegistration ore) {
        BuiltinBdApiOreVeinDisplays.init();
    }

    @Override
    public void registerOreVeins(OreDomainRegistration ore) {
        BuiltinBdApiOreEnvironments.init();
    }

    @Override
    public void registerOreDatagen(OreDomainRegistration ore) {
        VanillaOreFeatureBridge.registerDatagen(ore.registry());
    }

    @Override
    public void registerLang(LangDomainRegistration lang) {
        BuiltinBdApiPipeLang.init();
        BuiltinBdApiMachineUiLang.init();
        BuiltinBdApiMachineFeedbackLang.init();
        BuiltinBdApiEquipmentLang.init();
        BuiltinBdApiMaterialFormLang.init();
        BuiltinBdApiOreLang.init();
    }
}
