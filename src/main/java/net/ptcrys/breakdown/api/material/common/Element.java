package net.ptcrys.breakdown.api.material.common;

import java.util.Objects;

/**
 * 化学元素定义(骨架):118 种元素 + 常用同位素,每个为公开静态字段。
 * 作为材料的数据类型元素,供复合材料(合金/化合物)通过
 * {@link ElementCompositionDataType} 声明组成。
 */
public final class Element {

    private final String symbol;
    private final String en;
    private final String cn;
    private final int atomicNumber;
    private final int mass;

    private Element(String symbol, String en, String cn, int atomicNumber, int mass) {
        this.symbol = Objects.requireNonNull(symbol, "symbol");
        this.en = Objects.requireNonNull(en, "en");
        this.cn = Objects.requireNonNull(cn, "cn");
        this.atomicNumber = atomicNumber;
        this.mass = mass;
    }

    // ── 118 种元素(原子序数 1-118)──────────────────────────────────────────

    public static final Element HYDROGEN = new Element("H", "Hydrogen", "氢", 1, 1);
    public static final Element HELIUM = new Element("He", "Helium", "氦", 2, 4);
    public static final Element LITHIUM = new Element("Li", "Lithium", "锂", 3, 7);
    public static final Element BERYLLIUM = new Element("Be", "Beryllium", "铍", 4, 9);
    public static final Element BORON = new Element("B", "Boron", "硼", 5, 11);
    public static final Element CARBON = new Element("C", "Carbon", "碳", 6, 12);
    public static final Element NITROGEN = new Element("N", "Nitrogen", "氮", 7, 14);
    public static final Element OXYGEN = new Element("O", "Oxygen", "氧", 8, 16);
    public static final Element FLUORINE = new Element("F", "Fluorine", "氟", 9, 19);
    public static final Element NEON = new Element("Ne", "Neon", "氖", 10, 20);
    public static final Element SODIUM = new Element("Na", "Sodium", "钠", 11, 23);
    public static final Element MAGNESIUM = new Element("Mg", "Magnesium", "镁", 12, 24);
    public static final Element ALUMINIUM = new Element("Al", "Aluminium", "铝", 13, 27);
    public static final Element SILICON = new Element("Si", "Silicon", "硅", 14, 28);
    public static final Element PHOSPHORUS = new Element("P", "Phosphorus", "磷", 15, 31);
    public static final Element SULFUR = new Element("S", "Sulfur", "硫", 16, 32);
    public static final Element CHLORINE = new Element("Cl", "Chlorine", "氯", 17, 35);
    public static final Element ARGON = new Element("Ar", "Argon", "氩", 18, 40);
    public static final Element POTASSIUM = new Element("K", "Potassium", "钾", 19, 39);
    public static final Element CALCIUM = new Element("Ca", "Calcium", "钙", 20, 40);
    public static final Element SCANDIUM = new Element("Sc", "Scandium", "钪", 21, 45);
    public static final Element TITANIUM = new Element("Ti", "Titanium", "钛", 22, 48);
    public static final Element VANADIUM = new Element("V", "Vanadium", "钒", 23, 51);
    public static final Element CHROMIUM = new Element("Cr", "Chromium", "铬", 24, 52);
    public static final Element MANGANESE = new Element("Mn", "Manganese", "锰", 25, 55);
    public static final Element IRON = new Element("Fe", "Iron", "铁", 26, 56);
    public static final Element COBALT = new Element("Co", "Cobalt", "钴", 27, 59);
    public static final Element NICKEL = new Element("Ni", "Nickel", "镍", 28, 59);
    public static final Element COPPER = new Element("Cu", "Copper", "铜", 29, 64);
    public static final Element ZINC = new Element("Zn", "Zinc", "锌", 30, 65);
    public static final Element GALLIUM = new Element("Ga", "Gallium", "镓", 31, 70);
    public static final Element GERMANIUM = new Element("Ge", "Germanium", "锗", 32, 73);
    public static final Element ARSENIC = new Element("As", "Arsenic", "砷", 33, 75);
    public static final Element SELENIUM = new Element("Se", "Selenium", "硒", 34, 79);
    public static final Element BROMINE = new Element("Br", "Bromine", "溴", 35, 80);
    public static final Element KRYPTON = new Element("Kr", "Krypton", "氪", 36, 84);
    public static final Element RUBIDIUM = new Element("Rb", "Rubidium", "铷", 37, 85);
    public static final Element STRONTIUM = new Element("Sr", "Strontium", "锶", 38, 88);
    public static final Element YTTRIUM = new Element("Y", "Yttrium", "钇", 39, 89);
    public static final Element ZIRCONIUM = new Element("Zr", "Zirconium", "锆", 40, 91);
    public static final Element NIOBIUM = new Element("Nb", "Niobium", "铌", 41, 93);
    public static final Element MOLYBDENUM = new Element("Mo", "Molybdenum", "钼", 42, 96);
    public static final Element TECHNETIUM = new Element("Tc", "Technetium", "锝", 43, 98);
    public static final Element RUTHENIUM = new Element("Ru", "Ruthenium", "钌", 44, 101);
    public static final Element RHODIUM = new Element("Rh", "Rhodium", "铑", 45, 103);
    public static final Element PALLADIUM = new Element("Pd", "Palladium", "钯", 46, 106);
    public static final Element SILVER = new Element("Ag", "Silver", "银", 47, 108);
    public static final Element CADMIUM = new Element("Cd", "Cadmium", "镉", 48, 112);
    public static final Element INDIUM = new Element("In", "Indium", "铟", 49, 115);
    public static final Element TIN = new Element("Sn", "Tin", "锡", 50, 119);
    public static final Element ANTIMONY = new Element("Sb", "Antimony", "锑", 51, 122);
    public static final Element TELLURIUM = new Element("Te", "Tellurium", "碲", 52, 128);
    public static final Element IODINE = new Element("I", "Iodine", "碘", 53, 127);
    public static final Element XENON = new Element("Xe", "Xenon", "氙", 54, 131);
    public static final Element CAESIUM = new Element("Cs", "Caesium", "铯", 55, 133);
    public static final Element BARIUM = new Element("Ba", "Barium", "钡", 56, 137);
    public static final Element LANTHANUM = new Element("La", "Lanthanum", "镧", 57, 139);
    public static final Element CERIUM = new Element("Ce", "Cerium", "铈", 58, 140);
    public static final Element PRASEODYMIUM = new Element("Pr", "Praseodymium", "镨", 59, 141);
    public static final Element NEODYMIUM = new Element("Nd", "Neodymium", "钕", 60, 144);
    public static final Element PROMETHIUM = new Element("Pm", "Promethium", "钷", 61, 145);
    public static final Element SAMARIUM = new Element("Sm", "Samarium", "钐", 62, 150);
    public static final Element EUROPIUM = new Element("Eu", "Europium", "铕", 63, 152);
    public static final Element GADOLINIUM = new Element("Gd", "Gadolinium", "钆", 64, 157);
    public static final Element TERBIUM = new Element("Tb", "Terbium", "铽", 65, 159);
    public static final Element DYSPROSIUM = new Element("Dy", "Dysprosium", "镝", 66, 163);
    public static final Element HOLMIUM = new Element("Ho", "Holmium", "钬", 67, 165);
    public static final Element ERBIUM = new Element("Er", "Erbium", "铒", 68, 167);
    public static final Element THULIUM = new Element("Tm", "Thulium", "铥", 69, 169);
    public static final Element YTTERBIUM = new Element("Yb", "Ytterbium", "镱", 70, 173);
    public static final Element LUTETIUM = new Element("Lu", "Lutetium", "镥", 71, 175);
    public static final Element HAFNIUM = new Element("Hf", "Hafnium", "铪", 72, 178);
    public static final Element TANTALUM = new Element("Ta", "Tantalum", "钽", 73, 181);
    public static final Element TUNGSTEN = new Element("W", "Tungsten", "钨", 74, 184);
    public static final Element RHENIUM = new Element("Re", "Rhenium", "铼", 75, 186);
    public static final Element OSMIUM = new Element("Os", "Osmium", "锇", 76, 190);
    public static final Element IRIDIUM = new Element("Ir", "Iridium", "铱", 77, 192);
    public static final Element PLATINUM = new Element("Pt", "Platinum", "铂", 78, 195);
    public static final Element GOLD = new Element("Au", "Gold", "金", 79, 197);
    public static final Element MERCURY = new Element("Hg", "Mercury", "汞", 80, 201);
    public static final Element THALLIUM = new Element("Tl", "Thallium", "铊", 81, 204);
    public static final Element LEAD = new Element("Pb", "Lead", "铅", 82, 207);
    public static final Element BISMUTH = new Element("Bi", "Bismuth", "铋", 83, 209);
    public static final Element POLONIUM = new Element("Po", "Polonium", "钋", 84, 209);
    public static final Element ASTATINE = new Element("At", "Astatine", "砹", 85, 210);
    public static final Element RADON = new Element("Rn", "Radon", "氡", 86, 222);
    public static final Element FRANCIUM = new Element("Fr", "Francium", "钫", 87, 223);
    public static final Element RADIUM = new Element("Ra", "Radium", "镭", 88, 226);
    public static final Element ACTINIUM = new Element("Ac", "Actinium", "锕", 89, 227);
    public static final Element THORIUM = new Element("Th", "Thorium", "钍", 90, 232);
    public static final Element PROTACTINIUM = new Element("Pa", "Protactinium", "镤", 91, 231);
    public static final Element URANIUM = new Element("U", "Uranium", "铀", 92, 238);
    public static final Element NEPTUNIUM = new Element("Np", "Neptunium", "镎", 93, 237);
    public static final Element PLUTONIUM = new Element("Pu", "Plutonium", "钚", 94, 244);
    public static final Element AMERICIUM = new Element("Am", "Americium", "镅", 95, 243);
    public static final Element CURIUM = new Element("Cm", "Curium", "锔", 96, 247);
    public static final Element BERKELIUM = new Element("Bk", "Berkelium", "锫", 97, 247);
    public static final Element CALIFORNIUM = new Element("Cf", "Californium", "锎", 98, 251);
    public static final Element EINSTEINIUM = new Element("Es", "Einsteinium", "锿", 99, 252);
    public static final Element FERMIUM = new Element("Fm", "Fermium", "镄", 100, 257);
    public static final Element MENDELEVIUM = new Element("Md", "Mendelevium", "钔", 101, 258);
    public static final Element NOBELIUM = new Element("No", "Nobelium", "锘", 102, 259);
    public static final Element LAWRENCIUM = new Element("Lr", "Lawrencium", "铹", 103, 262);
    public static final Element RUTHERFORDIUM = new Element("Rf", "Rutherfordium", "𬬻", 104, 267);
    public static final Element DUBNIUM = new Element("Db", "Dubnium", "𬭊", 105, 268);
    public static final Element SEABORGIUM = new Element("Sg", "Seaborgium", "𬭳", 106, 269);
    public static final Element BOHRIUM = new Element("Bh", "Bohrium", "𬭛", 107, 270);
    public static final Element HASSIUM = new Element("Hs", "Hassium", "𬭶", 108, 269);
    public static final Element MEITNERIUM = new Element("Mt", "Meitnerium", "鿏", 109, 278);
    public static final Element DARMSTADTIUM = new Element("Ds", "Darmstadtium", "𫟼", 110, 281);
    public static final Element ROENTGENIUM = new Element("Rg", "Roentgenium", "𬬭", 111, 282);
    public static final Element COPERNICIUM = new Element("Cn", "Copernicium", "鿔", 112, 285);
    public static final Element NIHONIUM = new Element("Nh", "Nihonium", "鿭", 113, 286);
    public static final Element FLEROVIUM = new Element("Fl", "Flerovium", "𫓧", 114, 289);
    public static final Element MOSCOVIUM = new Element("Mc", "Moscovium", "镆", 115, 290);
    public static final Element LIVERMORIUM = new Element("Lv", "Livermorium", "𫟷", 116, 293);
    public static final Element TENNESSINE = new Element("Ts", "Tennessine", "鿬", 117, 294);
    public static final Element OGANESSON = new Element("Og", "Oganesson", "鿫", 118, 294);

    // ── 常用稳定同位素 ──────────────────────────────────────────────────────

    public static final Element HYDROGEN_2 = new Element("H-2", "Hydrogen-2", "氘", 1, 2);
    public static final Element CARBON_13 = new Element("C-13", "Carbon-13", "碳-13", 6, 13);
    public static final Element NITROGEN_15 = new Element("N-15", "Nitrogen-15", "氮-15", 7, 15);
    public static final Element OXYGEN_17 = new Element("O-17", "Oxygen-17", "氧-17", 8, 17);
    public static final Element OXYGEN_18 = new Element("O-18", "Oxygen-18", "氧-18", 8, 18);
    public static final Element IRON_56 = new Element("Fe-56", "Iron-56", "铁-56", 26, 56);
    public static final Element IODINE_127 = new Element("I-127", "Iodine-127", "碘-127", 53, 127);
    public static final Element LEAD_208 = new Element("Pb-208", "Lead-208", "铅-208", 82, 208);

    // ── 核相关放射性同位素 ──────────────────────────────────────────────────

    public static final Element HYDROGEN_3 = new Element("H-3", "Hydrogen-3", "氚", 1, 3);
    public static final Element CARBON_14 = new Element("C-14", "Carbon-14", "碳-14", 6, 14);
    public static final Element FLUORINE_18 = new Element("F-18", "Fluorine-18", "氟-18", 9, 18);
    public static final Element PHOSPHORUS_32 = new Element("P-32", "Phosphorus-32", "磷-32", 15, 32);
    public static final Element SULFUR_35 = new Element("S-35", "Sulfur-35", "硫-35", 16, 35);
    public static final Element COBALT_60 = new Element("Co-60", "Cobalt-60", "钴-60", 27, 60);
    public static final Element STRONTIUM_90 = new Element("Sr-90", "Strontium-90", "锶-90", 38, 90);
    public static final Element TECHNETIUM_99M = new Element("Tc-99m", "Technetium-99m", "锝-99m", 43, 99);
    public static final Element IODINE_131 = new Element("I-131", "Iodine-131", "碘-131", 53, 131);
    public static final Element CAESIUM_137 = new Element("Cs-137", "Caesium-137", "铯-137", 55, 137);
    public static final Element THORIUM_232 = new Element("Th-232", "Thorium-232", "钍-232", 90, 232);
    public static final Element URANIUM_235 = new Element("U-235", "Uranium-235", "铀-235", 92, 235);
    public static final Element URANIUM_238 = new Element("U-238", "Uranium-238", "铀-238", 92, 238);
    public static final Element PLUTONIUM_239 = new Element("Pu-239", "Plutonium-239", "钚-239", 94, 239);
    public static final Element PLUTONIUM_241 = new Element("Pu-241", "Plutonium-241", "钚-241", 94, 241);
    public static final Element AMERICIUM_241 = new Element("Am-241", "Americium-241", "镅-241", 95, 241);
    public static final Element CALIFORNIUM_252 = new Element("Cf-252", "Californium-252", "锎-252", 98, 252);

    public String symbol() {
        return symbol;
    }

    public String en() {
        return en;
    }

    public String cn() {
        return cn;
    }

    /** 原子序数(质子数)。 */
    public int atomicNumber() {
        return atomicNumber;
    }

    /** 相对原子质量(取整)。 */
    public int mass() {
        return mass;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
