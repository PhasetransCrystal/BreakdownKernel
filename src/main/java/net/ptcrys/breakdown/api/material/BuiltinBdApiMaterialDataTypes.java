package net.ptcrys.breakdown.api.material;

import net.ptcrys.breakdown.api.BreakdownApiPlugin;
import net.ptcrys.breakdown.api.material.common.*;
import net.ptcrys.topo.api.api.builtin.MaterialDomainRegistration;

/** 瓦解核心内置材料数据类型(骨架):颜色/质量为渲染文案基础,物理量为工艺基础,元素组成为复合材料基础。 */
public final class BuiltinBdApiMaterialDataTypes {

    private static final MaterialDomainRegistration MATERIALS = BreakdownApiPlugin.INSTANCE.material();

    /** 材料主色:形态渲染的着色依据。 */
    public static final RgbColorDataType PRIMARY_COLOR = MATERIALS.dataType(
            "primary_color", new RgbColorDataType(MATERIALS.id("primary_color")));

    /** 材料副色:供需要双色的形态(如分层渲染)使用。 */
    public static final RgbColorDataType SECONDARY_COLOR = MATERIALS.dataType(
            "secondary_color", new RgbColorDataType(MATERIALS.id("secondary_color")));

    /** 材料质量(相对原子质量,取整):配方质量守恒校验与文案依据。 */
    public static final MassDataType MASS = MATERIALS.dataType(
            "mass", new MassDataType(MATERIALS.id("mass")));

    // ── 基础物理量(单位:开尔文 K)────────────────────────────────────────────

    /** 通用温度:加工 / 工作温度等工艺参数。 */
    public static final TemperatureDataType TEMPERATURE = MATERIALS.dataType(
            "temperature", new TemperatureDataType(MATERIALS.id("temperature")));

    /** 熔点:材料熔化的温度,熔化 / 浇铸配方判定依据。 */
    public static final TemperatureDataType MELTING_POINT = MATERIALS.dataType(
            "melting_point", new TemperatureDataType(MATERIALS.id("melting_point")));

    /** 沸点:材料沸腾的温度,蒸馏 / 蒸发配方判定依据。 */
    public static final TemperatureDataType BOILING_POINT = MATERIALS.dataType(
            "boiling_point", new TemperatureDataType(MATERIALS.id("boiling_point")));

    /** 元素组成:复合材料(合金/化合物)声明自身由哪些元素、各多少构成。 */
    public static final ElementCompositionDataType ELEMENTS = MATERIALS.dataType(
            "elements", new ElementCompositionDataType(MATERIALS.id("elements")));

    /** Declaring tool stats is what makes a material's tools exist (equipment domain driver). */
    public static final ToolStatsDataType TOOL_STATS = MATERIALS.dataType(
            "tool_stats", new ToolStatsDataType(MATERIALS.id("tool_stats")));

    /** Declaring armor stats is what makes a material's armor exist (equipment domain driver). */
    public static final ArmorStatsDataType ARMOR_STATS = MATERIALS.dataType(
            "armor_stats", new ArmorStatsDataType(MATERIALS.id("armor_stats")));

    private BuiltinBdApiMaterialDataTypes() {}

    public static void init() {}
}
