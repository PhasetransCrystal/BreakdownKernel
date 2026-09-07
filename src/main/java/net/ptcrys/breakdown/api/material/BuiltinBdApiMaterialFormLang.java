package net.ptcrys.breakdown.api.material;

import net.ptcrys.breakdown.api.BreakdownApiPlugin;
import net.ptcrys.topo.api.api.builtin.LangDomainRegistration;
import net.ptcrys.topo.api.api.lang.LangKey;

/** 瓦解核心内置材料 tooltip 文案(骨架):材料名 / 形态 / 数量 / 质量。 */
public final class BuiltinBdApiMaterialFormLang {

    private static final LangDomainRegistration LANG = BreakdownApiPlugin.INSTANCE.lang();

    public static final LangKey TOOLTIP_MATERIAL_MATERIAL = LANG.key("tooltip", "material.material", "Material", "材料");
    public static final LangKey TOOLTIP_MATERIAL_FORM = LANG.key("tooltip", "material.form", "Form", "形态");
    public static final LangKey TOOLTIP_MATERIAL_AMOUNT = LANG.key("tooltip", "material.amount", "Amount", "数量");
    public static final LangKey TOOLTIP_MATERIAL_MASS = LANG.key("tooltip", "material.mass", "Mass", "质量");
    public static final LangKey TOOLTIP_MATERIAL_AMOUNT_INGOT = LANG.key("tooltip", "material.amount_ingot", "%s ingot", "%s锭");
    public static final LangKey TOOLTIP_MATERIAL_AMOUNT_INGOTS = LANG.key("tooltip", "material.amount_ingots", "%s ingots", "%s锭");
    public static final LangKey TOOLTIP_MATERIAL_AMOUNT_UNITS = LANG.key("tooltip", "material.amount_units", "%s units", "%s份");

    private BuiltinBdApiMaterialFormLang() {}

    public static void init() {}
}
