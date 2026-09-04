package net.ptcrys.breakdown.data.material;

import net.ptcrys.breakdown.api.material.common.Element;
import net.ptcrys.breakdown.data.BreakdownKernalPlugin;
import net.ptcrys.topo.api.api.builtin.MaterialDomainRegistration;
import net.ptcrys.topo.api.material.Material;

import java.util.Objects;

import static net.ptcrys.breakdown.api.material.BuiltinBdMaterialDataTypes.ELEMENTS;
import static net.ptcrys.breakdown.api.material.BuiltinBdMaterialDataTypes.MASS;
import static net.ptcrys.breakdown.api.material.BuiltinBdMaterialDataTypes.PRIMARY_COLOR;
import static net.ptcrys.breakdown.api.material.BuiltinBdMaterialForms.BLOCK;
import static net.ptcrys.breakdown.api.material.BuiltinBdMaterialForms.DUST;
import static net.ptcrys.breakdown.api.material.BuiltinBdMaterialForms.GEM;
import static net.ptcrys.breakdown.api.material.BuiltinBdMaterialForms.INGOT;
import static net.ptcrys.breakdown.api.material.BuiltinBdMaterialForms.NUGGET;

/**
 * 瓦解核心内容插件·元素单质材料:118 种元素各注册为一个单质材料,
 * 组成经 {@code ELEMENTS.of(element, 1)} 声明。形态按类别分配
 * (金属→锭/粒/块/粉,非金属→粉,C/S 额外宝石),颜色按类别基准色。
 */
public final class BdElements {

    private static final MaterialDomainRegistration MATERIALS = BreakdownKernalPlugin.INSTANCE.material();

    // ── 类别基准色 ──────────────────────────────────────────────────────────

    private static final int ALKALI = 0xD9481E;          // 碱金属:红橙
    private static final int ALKALINE_EARTH = 0xE8A33D;  // 碱土金属:橙
    private static final int TRANSITION = 0x8E9BAA;      // 过渡金属:蓝灰
    private static final int POST_TRANSITION = 0xC9CDD0; // 主族金属:银灰
    private static final int METALLOID = 0x9CAF88;       // 类金属:灰绿
    private static final int NONMETAL = 0x4CAF50;        // 非金属:绿
    private static final int HALOGEN = 0xA5D6A7;         // 卤素:浅绿
    private static final int NOBLE_GAS = 0x9575CD;       // 惰性气体:紫
    private static final int OTHER_GAS = 0x81D4FA;       // 其他气体:浅蓝
    private static final int LANTHANIDE = 0x81C784;      // 镧系:浅绿
    private static final int ACTINIDE = 0xCE93D8;        // 锕系:浅紫

    // ── 碱金属(锭/粒/块/粉)───────────────────────────────────────────────

    public static final Material LITHIUM = metal("lithium", "Lithium", "锂", ALKALI, Element.LITHIUM);
    public static final Material SODIUM = metal("sodium", "Sodium", "钠", ALKALI, Element.SODIUM);
    public static final Material POTASSIUM = metal("potassium", "Potassium", "钾", ALKALI, Element.POTASSIUM);
    public static final Material RUBIDIUM = metal("rubidium", "Rubidium", "铷", ALKALI, Element.RUBIDIUM);
    public static final Material CAESIUM = metal("caesium", "Caesium", "铯", ALKALI, Element.CAESIUM);
    public static final Material FRANCIUM = metal("francium", "Francium", "钫", ALKALI, Element.FRANCIUM);

    // ── 碱土金属(锭/粒/块/粉)─────────────────────────────────────────────

    public static final Material BERYLLIUM = metal("beryllium", "Beryllium", "铍", ALKALINE_EARTH, Element.BERYLLIUM);
    public static final Material MAGNESIUM = metal("magnesium", "Magnesium", "镁", ALKALINE_EARTH, Element.MAGNESIUM);
    public static final Material CALCIUM = metal("calcium", "Calcium", "钙", ALKALINE_EARTH, Element.CALCIUM);
    public static final Material STRONTIUM = metal("strontium", "Strontium", "锶", ALKALINE_EARTH, Element.STRONTIUM);
    public static final Material BARIUM = metal("barium", "Barium", "钡", ALKALINE_EARTH, Element.BARIUM);
    public static final Material RADIUM = metal("radium", "Radium", "镭", ALKALINE_EARTH, Element.RADIUM);

    // ── 过渡金属(锭/粒/块/粉)─────────────────────────────────────────────

    public static final Material SCANDIUM = metal("scandium", "Scandium", "钪", TRANSITION, Element.SCANDIUM);
    public static final Material TITANIUM = metal("titanium", "Titanium", "钛", TRANSITION, Element.TITANIUM);
    public static final Material VANADIUM = metal("vanadium", "Vanadium", "钒", TRANSITION, Element.VANADIUM);
    public static final Material CHROMIUM = metal("chromium", "Chromium", "铬", TRANSITION, Element.CHROMIUM);
    public static final Material MANGANESE = metal("manganese", "Manganese", "锰", TRANSITION, Element.MANGANESE);
    public static final Material IRON = metal("iron", "Iron", "铁", TRANSITION, Element.IRON);
    public static final Material COBALT = metal("cobalt", "Cobalt", "钴", TRANSITION, Element.COBALT);
    public static final Material NICKEL = metal("nickel", "Nickel", "镍", TRANSITION, Element.NICKEL);
    public static final Material COPPER = metal("copper", "Copper", "铜", TRANSITION, Element.COPPER);
    public static final Material ZINC = metal("zinc", "Zinc", "锌", TRANSITION, Element.ZINC);
    public static final Material YTTRIUM = metal("yttrium", "Yttrium", "钇", TRANSITION, Element.YTTRIUM);
    public static final Material ZIRCONIUM = metal("zirconium", "Zirconium", "锆", TRANSITION, Element.ZIRCONIUM);
    public static final Material NIOBIUM = metal("niobium", "Niobium", "铌", TRANSITION, Element.NIOBIUM);
    public static final Material MOLYBDENUM = metal("molybdenum", "Molybdenum", "钼", TRANSITION, Element.MOLYBDENUM);
    public static final Material TECHNETIUM = metal("technetium", "Technetium", "锝", TRANSITION, Element.TECHNETIUM);
    public static final Material RUTHENIUM = metal("ruthenium", "Ruthenium", "钌", TRANSITION, Element.RUTHENIUM);
    public static final Material RHODIUM = metal("rhodium", "Rhodium", "铑", TRANSITION, Element.RHODIUM);
    public static final Material PALLADIUM = metal("palladium", "Palladium", "钯", TRANSITION, Element.PALLADIUM);
    public static final Material SILVER = metal("silver", "Silver", "银", TRANSITION, Element.SILVER);
    public static final Material CADMIUM = metal("cadmium", "Cadmium", "镉", TRANSITION, Element.CADMIUM);
    public static final Material HAFNIUM = metal("hafnium", "Hafnium", "铪", TRANSITION, Element.HAFNIUM);
    public static final Material TANTALUM = metal("tantalum", "Tantalum", "钽", TRANSITION, Element.TANTALUM);
    public static final Material TUNGSTEN = metal("tungsten", "Tungsten", "钨", TRANSITION, Element.TUNGSTEN);
    public static final Material RHENIUM = metal("rhenium", "Rhenium", "铼", TRANSITION, Element.RHENIUM);
    public static final Material OSMIUM = metal("osmium", "Osmium", "锇", TRANSITION, Element.OSMIUM);
    public static final Material IRIDIUM = metal("iridium", "Iridium", "铱", TRANSITION, Element.IRIDIUM);
    public static final Material PLATINUM = metal("platinum", "Platinum", "铂", TRANSITION, Element.PLATINUM);
    public static final Material GOLD = metal("gold", "Gold", "金", TRANSITION, Element.GOLD);
    public static final Material MERCURY = metal("mercury", "Mercury", "汞", TRANSITION, Element.MERCURY);
    public static final Material RUTHERFORDIUM = metal("rutherfordium", "Rutherfordium", "𬬻", TRANSITION, Element.RUTHERFORDIUM);
    public static final Material DUBNIUM = metal("dubnium", "Dubnium", "𬭊", TRANSITION, Element.DUBNIUM);
    public static final Material SEABORGIUM = metal("seaborgium", "Seaborgium", "𬭳", TRANSITION, Element.SEABORGIUM);
    public static final Material BOHRIUM = metal("bohrium", "Bohrium", "𬭛", TRANSITION, Element.BOHRIUM);
    public static final Material HASSIUM = metal("hassium", "Hassium", "𬭶", TRANSITION, Element.HASSIUM);
    public static final Material MEITNERIUM = metal("meitnerium", "Meitnerium", "鿏", TRANSITION, Element.MEITNERIUM);
    public static final Material DARMSTADTIUM = metal("darmstadtium", "Darmstadtium", "𫟼", TRANSITION, Element.DARMSTADTIUM);
    public static final Material ROENTGENIUM = metal("roentgenium", "Roentgenium", "𬬭", TRANSITION, Element.ROENTGENIUM);
    public static final Material COPERNICIUM = metal("copernicium", "Copernicium", "鿔", TRANSITION, Element.COPERNICIUM);

    // ── 主族金属(锭/粒/块/粉)─────────────────────────────────────────────

    public static final Material ALUMINIUM = metal("aluminium", "Aluminium", "铝", POST_TRANSITION, Element.ALUMINIUM);
    public static final Material GALLIUM = metal("gallium", "Gallium", "镓", POST_TRANSITION, Element.GALLIUM);
    public static final Material INDIUM = metal("indium", "Indium", "铟", POST_TRANSITION, Element.INDIUM);
    public static final Material TIN = metal("tin", "Tin", "锡", POST_TRANSITION, Element.TIN);
    public static final Material THALLIUM = metal("thallium", "Thallium", "铊", POST_TRANSITION, Element.THALLIUM);
    public static final Material LEAD = metal("lead", "Lead", "铅", POST_TRANSITION, Element.LEAD);
    public static final Material BISMUTH = metal("bismuth", "Bismuth", "铋", POST_TRANSITION, Element.BISMUTH);
    public static final Material POLONIUM = metal("polonium", "Polonium", "钋", POST_TRANSITION, Element.POLONIUM);
    public static final Material NIHONIUM = metal("nihonium", "Nihonium", "鿭", POST_TRANSITION, Element.NIHONIUM);
    public static final Material FLEROVIUM = metal("flerovium", "Flerovium", "𫓧", POST_TRANSITION, Element.FLEROVIUM);
    public static final Material MOSCOVIUM = metal("moscovium", "Moscovium", "镆", POST_TRANSITION, Element.MOSCOVIUM);
    public static final Material LIVERMORIUM = metal("livermorium", "Livermorium", "𫟷", POST_TRANSITION, Element.LIVERMORIUM);

    // ── 类金属(粉)────────────────────────────────────────────────────────

    public static final Material BORON = nonMetal("boron", "Boron", "硼", METALLOID, Element.BORON);
    public static final Material SILICON = nonMetal("silicon", "Silicon", "硅", METALLOID, Element.SILICON);
    public static final Material GERMANIUM = nonMetal("germanium", "Germanium", "锗", METALLOID, Element.GERMANIUM);
    public static final Material ARSENIC = nonMetal("arsenic", "Arsenic", "砷", METALLOID, Element.ARSENIC);
    public static final Material ANTIMONY = nonMetal("antimony", "Antimony", "锑", METALLOID, Element.ANTIMONY);
    public static final Material TELLURIUM = nonMetal("tellurium", "Tellurium", "碲", METALLOID, Element.TELLURIUM);
    public static final Material ASTATINE = nonMetal("astatine", "Astatine", "砹", METALLOID, Element.ASTATINE);

    // ── 非金属(粉;C/S 额外宝石)────────────────────────────────────────────

    public static final Material CARBON = gem("carbon", "Carbon", "碳", NONMETAL, Element.CARBON);
    public static final Material PHOSPHORUS = nonMetal("phosphorus", "Phosphorus", "磷", NONMETAL, Element.PHOSPHORUS);
    public static final Material SULFUR = gem("sulfur", "Sulfur", "硫", NONMETAL, Element.SULFUR);
    public static final Material SELENIUM = nonMetal("selenium", "Selenium", "硒", NONMETAL, Element.SELENIUM);
    public static final Material IODINE = nonMetal("iodine", "Iodine", "碘", NONMETAL, Element.IODINE);

    // ── 卤素(粉)──────────────────────────────────────────────────────────

    public static final Material FLUORINE = nonMetal("fluorine", "Fluorine", "氟", HALOGEN, Element.FLUORINE);
    public static final Material CHLORINE = nonMetal("chlorine", "Chlorine", "氯", HALOGEN, Element.CHLORINE);
    public static final Material BROMINE = nonMetal("bromine", "Bromine", "溴", HALOGEN, Element.BROMINE);

    // ── 惰性气体(粉)──────────────────────────────────────────────────────

    public static final Material HELIUM = nonMetal("helium", "Helium", "氦", NOBLE_GAS, Element.HELIUM);
    public static final Material NEON = nonMetal("neon", "Neon", "氖", NOBLE_GAS, Element.NEON);
    public static final Material ARGON = nonMetal("argon", "Argon", "氩", NOBLE_GAS, Element.ARGON);
    public static final Material KRYPTON = nonMetal("krypton", "Krypton", "氪", NOBLE_GAS, Element.KRYPTON);
    public static final Material XENON = nonMetal("xenon", "Xenon", "氙", NOBLE_GAS, Element.XENON);
    public static final Material RADON = nonMetal("radon", "Radon", "氡", NOBLE_GAS, Element.RADON);
    public static final Material OGANESSON = nonMetal("oganesson", "Oganesson", "鿫", NOBLE_GAS, Element.OGANESSON);

    // ── 其他气体(粉)──────────────────────────────────────────────────────

    public static final Material HYDROGEN = nonMetal("hydrogen", "Hydrogen", "氢", OTHER_GAS, Element.HYDROGEN);
    public static final Material NITROGEN = nonMetal("nitrogen", "Nitrogen", "氮", OTHER_GAS, Element.NITROGEN);
    public static final Material OXYGEN = nonMetal("oxygen", "Oxygen", "氧", OTHER_GAS, Element.OXYGEN);

    // ── 镧系(锭/粒/块/粉)─────────────────────────────────────────────────

    public static final Material LANTHANUM = metal("lanthanum", "Lanthanum", "镧", LANTHANIDE, Element.LANTHANUM);
    public static final Material CERIUM = metal("cerium", "Cerium", "铈", LANTHANIDE, Element.CERIUM);
    public static final Material PRASEODYMIUM = metal("praseodymium", "Praseodymium", "镨", LANTHANIDE, Element.PRASEODYMIUM);
    public static final Material NEODYMIUM = metal("neodymium", "Neodymium", "钕", LANTHANIDE, Element.NEODYMIUM);
    public static final Material PROMETHIUM = metal("promethium", "Promethium", "钷", LANTHANIDE, Element.PROMETHIUM);
    public static final Material SAMARIUM = metal("samarium", "Samarium", "钐", LANTHANIDE, Element.SAMARIUM);
    public static final Material EUROPIUM = metal("europium", "Europium", "铕", LANTHANIDE, Element.EUROPIUM);
    public static final Material GADOLINIUM = metal("gadolinium", "Gadolinium", "钆", LANTHANIDE, Element.GADOLINIUM);
    public static final Material TERBIUM = metal("terbium", "Terbium", "铽", LANTHANIDE, Element.TERBIUM);
    public static final Material DYSPROSIUM = metal("dysprosium", "Dysprosium", "镝", LANTHANIDE, Element.DYSPROSIUM);
    public static final Material HOLMIUM = metal("holmium", "Holmium", "钬", LANTHANIDE, Element.HOLMIUM);
    public static final Material ERBIUM = metal("erbium", "Erbium", "铒", LANTHANIDE, Element.ERBIUM);
    public static final Material THULIUM = metal("thulium", "Thulium", "铥", LANTHANIDE, Element.THULIUM);
    public static final Material YTTERBIUM = metal("ytterbium", "Ytterbium", "镱", LANTHANIDE, Element.YTTERBIUM);
    public static final Material LUTETIUM = metal("lutetium", "Lutetium", "镥", LANTHANIDE, Element.LUTETIUM);

    // ── 锕系(锭/粒/块/粉)─────────────────────────────────────────────────

    public static final Material ACTINIUM = metal("actinium", "Actinium", "锕", ACTINIDE, Element.ACTINIUM);
    public static final Material THORIUM = metal("thorium", "Thorium", "钍", ACTINIDE, Element.THORIUM);
    public static final Material PROTACTINIUM = metal("protactinium", "Protactinium", "镤", ACTINIDE, Element.PROTACTINIUM);
    public static final Material URANIUM = metal("uranium", "Uranium", "铀", ACTINIDE, Element.URANIUM);
    public static final Material NEPTUNIUM = metal("neptunium", "Neptunium", "镎", ACTINIDE, Element.NEPTUNIUM);
    public static final Material PLUTONIUM = metal("plutonium", "Plutonium", "钚", ACTINIDE, Element.PLUTONIUM);
    public static final Material AMERICIUM = metal("americium", "Americium", "镅", ACTINIDE, Element.AMERICIUM);
    public static final Material CURIUM = metal("curium", "Curium", "锔", ACTINIDE, Element.CURIUM);
    public static final Material BERKELIUM = metal("berkelium", "Berkelium", "锫", ACTINIDE, Element.BERKELIUM);
    public static final Material CALIFORNIUM = metal("californium", "Californium", "锎", ACTINIDE, Element.CALIFORNIUM);
    public static final Material EINSTEINIUM = metal("einsteinium", "Einsteinium", "锿", ACTINIDE, Element.EINSTEINIUM);
    public static final Material FERMIUM = metal("fermium", "Fermium", "镄", ACTINIDE, Element.FERMIUM);
    public static final Material MENDELEVIUM = metal("mendelevium", "Mendelevium", "钔", ACTINIDE, Element.MENDELEVIUM);
    public static final Material NOBELIUM = metal("nobelium", "Nobelium", "锘", ACTINIDE, Element.NOBELIUM);
    public static final Material LAWRENCIUM = metal("lawrencium", "Lawrencium", "铹", ACTINIDE, Element.LAWRENCIUM);

    private BdElements() {}

    /** 触发类加载,字段初始化即完成全部注册。 */
    public static void init() {
        Objects.requireNonNull(OGANESSON);
        Objects.requireNonNull(URANIUM);
    }

    /** 金属单质:锭 / 粒 / 块 / 粉。 */
    private static Material metal(String id, String en, String cn, int color, Element element) {
        return MATERIALS.material(id).lang(en, cn)
                .forms(INGOT, NUGGET, BLOCK, DUST)
                .data(PRIMARY_COLOR.rgb(color))
                .data(MASS.mass(element.mass()))
                .data(ELEMENTS.of(element, 1))
                .build();
    }

    /** 非金属单质:粉。 */
    private static Material nonMetal(String id, String en, String cn, int color, Element element) {
        return MATERIALS.material(id).lang(en, cn)
                .forms(DUST)
                .data(PRIMARY_COLOR.rgb(color))
                .data(MASS.mass(element.mass()))
                .data(ELEMENTS.of(element, 1))
                .build();
    }

    /** 宝石类单质:粉 + 宝石(C / S)。 */
    private static Material gem(String id, String en, String cn, int color, Element element) {
        return MATERIALS.material(id).lang(en, cn)
                .forms(DUST, GEM)
                .data(PRIMARY_COLOR.rgb(color))
                .data(MASS.mass(element.mass()))
                .data(ELEMENTS.of(element, 1))
                .build();
    }
}
