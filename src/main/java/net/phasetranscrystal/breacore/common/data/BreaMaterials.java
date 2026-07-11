package net.phasetranscrystal.breacore.common.data;

import net.phasetranscrystal.breacore.BreakdownCore;
import net.phasetranscrystal.breacore.api.BreaApi;
import net.phasetranscrystal.breacore.api.addon.AddonFinder;
import net.phasetranscrystal.breacore.api.addon.IBreaAddon;
import net.phasetranscrystal.breacore.api.material.MarkerMaterial;
import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.data.materials.*;

import net.minecraft.resources.Identifier;

import org.jetbrains.annotations.NotNull;

public class BreaMaterials {

    /// 化学颜料
    public static Material[] CHEMICAL_DYES;
    // region 元素周期表材料
    public static Material Actinium;
    public static Material Aluminium;
    public static Material Americium;
    public static Material Antimony;
    // endregion
    public static Material Argon;
    public static Material Arsenic;
    public static Material Astatine;
    public static Material Barium;
    public static Material Berkelium;
    public static Material Beryllium;
    public static Material Bismuth;
    public static Material Bohrium;
    public static Material Boron;
    public static Material Bromine;
    public static Material Caesium;
    public static Material Calcium;
    public static Material Californium;
    public static Material Carbon;
    public static Material Cadmium;
    public static Material Cerium;
    public static Material Chlorine;
    public static Material Chromium;
    public static Material Cobalt;
    public static Material Copernicium;
    public static Material Copper;
    public static Material Curium;
    public static Material Darmstadtium;
    public static Material Deuterium;
    public static Material Dubnium;
    public static Material Dysprosium;
    public static Material Einsteinium;
    public static Material Erbium;
    public static Material Europium;
    public static Material Fermium;
    public static Material Flerovium;
    public static Material Fluorine;
    public static Material Francium;
    public static Material Gadolinium;
    public static Material Gallium;
    public static Material Germanium;
    public static Material Gold;
    public static Material Hafnium;
    public static Material Hassium;
    public static Material Holmium;
    public static Material Hydrogen;
    public static Material Helium;
    public static Material Helium3;
    public static Material Indium;
    public static Material Iodine;
    public static Material Iridium;
    public static Material Iron;
    public static Material Krypton;
    public static Material Lanthanum;
    public static Material Lawrencium;
    public static Material Lead;
    public static Material Lithium;
    public static Material Livermorium;
    public static Material Lutetium;
    public static Material Magnesium;
    public static Material Mendelevium;
    public static Material Manganese;
    public static Material Meitnerium;
    public static Material Mercury;
    public static Material Molybdenum;
    public static Material Moscovium;
    public static Material Neodymium;
    public static Material Neon;
    public static Material Neptunium;
    public static Material Nickel;
    public static Material Nihonium;
    public static Material Niobium;
    public static Material Nitrogen;
    public static Material Nobelium;
    public static Material Oganesson;
    public static Material Osmium;
    public static Material Oxygen;
    public static Material Palladium;
    public static Material Phosphorus;
    public static Material Polonium;
    public static Material Platinum;
    public static Material Plutonium239;
    public static Material Plutonium241;
    public static Material Potassium;
    public static Material Praseodymium;
    public static Material Promethium;
    public static Material Protactinium;
    public static Material Radon;
    public static Material Radium;
    public static Material Rhenium;
    public static Material Rhodium;
    public static Material Roentgenium;
    public static Material Rubidium;
    public static Material Ruthenium;
    public static Material Rutherfordium;
    public static Material Samarium;
    public static Material Scandium;
    public static Material Seaborgium;
    public static Material Selenium;
    public static Material Silicon;
    public static Material Silver;
    public static Material Sodium;
    public static Material Strontium;
    public static Material Sulfur;
    public static Material Tantalum;
    public static Material Technetium;
    public static Material Tellurium;
    public static Material Tennessine;
    public static Material Terbium;
    public static Material Thorium;
    public static Material Thallium;
    public static Material Thulium;
    public static Material Tin;
    public static Material Titanium;
    public static Material Tritium;
    public static Material Tungsten;
    public static Material Uranium238;
    public static Material Uranium235;
    public static Material Vanadium;
    public static Material Xenon;
    public static Material Ytterbium;
    public static Material Yttrium;
    public static Material Zinc;
    public static Material Zirconium;
    // endregion
    // region 颜料材料
    public static Material DyeBlack;
    public static Material DyeRed;
    public static Material DyeGreen;
    public static Material DyeBrown;
    public static Material DyeBlue;
    public static Material DyePurple;
    public static Material DyeCyan;
    public static Material DyeLightGray;
    public static Material DyeGray;
    public static Material DyePink;
    public static Material DyeLime;
    public static Material DyeYellow;
    public static Material DyeLightBlue;
    public static Material DyeMagenta;
    public static Material DyeOrange;
    public static Material DyeWhite;
    /// 石头
    public static Material Stone;
    /// 花岗岩
    public static Material Granite;
    /// 闪长岩
    public static Material Diorite;
    /// 安山岩
    public static Material Andesite;
    /// 深板岩
    public static Material Deepslate;
    // endregion
    // region 石料
    /// 凝灰岩
    public static Material Tuff;
    /// 沙子
    public static Material SiliconDioxide;
    /// 沙砾
    public static Material Flint;
    /// 玄武岩
    public static Material Basalt;
    /// 下界岩
    public static Material Netherrack;
    /// 黑石
    public static Material Blackstone;
    /// 末地石
    public static Material Endstone;
    // endregion
    // TODO:原版材料
    public static Material Water;
    public static Material Lava;
    public static Material Milk;
    /// 黑曜石
    public static Material Obsidian;
    /// 冰
    public static Material Ice;
    /// 玻璃
    public static Material Glass;
    /// 混凝土
    public static Material Concrete;
    /// 粘土
    public static Material Clay;
    /// 红砖
    public static Material Brick;
    /// 方解石
    public static Material Calcite;
    /// 下界合金
    public static Material Netherite;
    /// 红石
    public static Material Redstone;
    /// 钻石
    public static Material Diamond;
    /// 煤
    public static Material Coal;
    /// 绿宝石
    public static Material Emerald;
    // region 矿物
    /// 青金石
    public static Material Lapis;
    /// 下界石英
    public static Material NetherQuartz;
    /// 塞特斯石英
    public static Material CertusQuartz;
    /// 荧石
    public static Material Glowstone;
    /// 紫水晶
    public static Material Amethyst;
    /// 下界之心
    public static Material NetherStar;
    /// 末影之眼
    public static Material EnderEye;
    /// 末影珍珠
    public static Material EnderPearl;
    /// 木头
    public static Material Wood;
    /// 去皮木头
    public static Material TreatedWood;
    // endregion
    // region 杂物
    /// 木炭
    public static Material Charcoal;
    /// 糖
    public static Material Sugar;
    /// 纸
    public static Material Paper;
    /// 火药
    public static Material Gunpowder;
    /// 回响碎片
    public static Material EchoShard;
    /// 骨
    public static Material Bone;
    /// 烈焰(烈焰粉,烈焰棒)
    public static Material Blaze;
    /// 蜡(蜜脾)
    public static Material Wax;
    /// 幽匿
    public static Material Sculk;
    /// 幽匿
    public static Material Air;
    public static Material NetherAir;
    public static Material EnderAir;
    public static Material LiquidAir;
    public static Material LiquidNetherAir;
    // endregion
    // 空气
    public static Material LiquidEnderAir;

    public static void init() {
        MarkerMaterials.register();
        ElementMaterials.register();
        FirstDegreeMaterials.register();
        OrganicChemistryMaterials.register();
        UnknownCompositionMaterials.register();
        SecondDegreeMaterials.register();
        HigherDegreeMaterials.register();

        MaterialFlagAddition.register();

        AddonFinder.getAddonList().forEach(IBreaAddon::addMaterial);

        CHEMICAL_DYES = new Material[] {
                DyeWhite, DyeOrange,
                DyeMagenta, DyeLightBlue,
                DyeYellow, DyeLime,
                DyePink, DyeGray,
                DyeLightGray, DyeCyan,
                DyePurple, DyeBlue,
                DyeBrown, DyeGreen,
                DyeRed, DyeBlack
        };
    }

    @NotNull
    public static Material get(String name) {
        var mat = BreaApi.materialManager.getMaterial(Identifier.parse(name));
        // mat could be null here due to the registrate grabbing a oldmaterial that isn't in the map
        if (mat == null) {
            BreakdownCore.LOGGER.warn("{} is not a known Material", name);
            return MarkerMaterial.NULL;
        }
        return mat;
    }
}
