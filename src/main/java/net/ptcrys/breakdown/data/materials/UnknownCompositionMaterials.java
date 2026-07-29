package net.ptcrys.breakdown.data.materials;

public class UnknownCompositionMaterials {

    public static void register() {
        /*
         * Gunpowder = new MaterialBuilder(BreaLib.id("gunpowder"))
         * .dust(0)
         * .color(0xa4a4a4).secondaryColor(0x767676).iconSet(DULL)
         * .flags(FLAMMABLE, EXPLOSIVE, NO_SMELTING, NO_SMASHING)
         * .buildAndRegister();
         * 
         * Stone = new MaterialBuilder(BreaLib.id("stone"))
         * .dust(2)
         * .color(0x8f8f8f).secondaryColor(0x898989).iconSet(DULL)
         * .flags(MORTAR_GRINDABLE, GENERATE_GEAR, NO_SMASHING, NO_SMELTING)
         * .buildAndRegister();
         * 
         * Lava = new MaterialBuilder(BreaLib.id("lava"))
         * .fluid().color(0xFF4000).buildAndRegister();
         * 
         * Netherite = new MaterialBuilder(BreaLib.id("netherite"))
         * .ingot().color(0x4b4042).secondaryColor(0x474447)
         * .buildAndRegister();
         * 
         * Glowstone = new MaterialBuilder(BreaLib.id("glowstone"))
         * .dust(1)
         * .liquid(new FluidRegisterBuilder().temperature(500))
         * .color(0xfcb34c).secondaryColor(0xce7533).iconSet(DULL)
         * .flags(NO_SMASHING, GENERATE_PLATE, EXCLUDE_PLATE_COMPRESSOR_RECIPE,
         * EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES)
         * .buildAndRegister();
         * 
         * NetherStar = new MaterialBuilder(BreaLib.id("nether_star"))
         * .gem(4)
         * .color(0xfeffc6).secondaryColor(0x7fd7e2)
         * .iconSet(DULL)
         * .flags(NO_SMASHING, NO_SMELTING, GENERATE_LENS)
         * .buildAndRegister();
         * 
         * Endstone = new MaterialBuilder(BreaLib.id("endstone"))
         * .dust(1)
         * .color(0xf6fabd).secondaryColor(0xc5be8b).iconSet(DULL)
         * .flags(NO_SMASHING)
         * .buildAndRegister();
         * 
         * Netherrack = new MaterialBuilder(BreaLib.id("netherrack"))
         * .dust(1)
         * .color(0x7c4249).secondaryColor(0x400b0b).iconSet(DULL)
         * .flags(NO_SMASHING, FLAMMABLE)
         * .buildAndRegister();
         * 
         * Milk = new MaterialBuilder(BreaLib.id("milk"))
         * .liquid(new FluidRegisterBuilder()
         * .temperature(295)
         * .customStill())
         * .color(0xfffbf0).secondaryColor(0xf6eac8).iconSet(DULL)
         * .buildAndRegister();
         * 
         * Wood = new MaterialBuilder(BreaLib.id("wood"))
         * .wood()
         * .color(0xc29f6d).secondaryColor(0x643200).iconSet(DULL)
         * .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_BOLT_SCREW, GENERATE_LONG_ROD, FLAMMABLE, GENERATE_GEAR,
         * GENERATE_FRAME)
         * .buildAndRegister();
         * 
         * Paper = new MaterialBuilder(BreaLib.id("paper"))
         * .dust(0)
         * .color(0xFAFAFA).secondaryColor(0x878787).iconSet(DULL)
         * .flags(GENERATE_PLATE, FLAMMABLE, NO_SMELTING, NO_SMASHING,
         * MORTAR_GRINDABLE, EXCLUDE_PLATE_COMPRESSOR_RECIPE)
         * .buildAndRegister();
         * 
         * // These colors are much nicer looking than those in MC's EnumDyeColor
         * DyeBlack = new MaterialBuilder(BreaLib.id("black_dye"))
         * .fluid().color(0x202020).buildAndRegister();
         * 
         * DyeRed = new MaterialBuilder(BreaLib.id("red_dye"))
         * .fluid().color(0xFF0000).buildAndRegister();
         * 
         * DyeGreen = new MaterialBuilder(BreaLib.id("green_dye"))
         * .fluid().color(0x00FF00).buildAndRegister();
         * 
         * DyeBrown = new MaterialBuilder(BreaLib.id("brown_dye"))
         * .fluid().color(0x604000).buildAndRegister();
         * 
         * DyeBlue = new MaterialBuilder(BreaLib.id("blue_dye"))
         * .fluid().color(0x0020FF).buildAndRegister();
         * 
         * DyePurple = new MaterialBuilder(BreaLib.id("purple_dye"))
         * .fluid().color(0x800080).buildAndRegister();
         * 
         * DyeCyan = new MaterialBuilder(BreaLib.id("cyan_dye"))
         * .fluid().color(0x00FFFF).buildAndRegister();
         * 
         * DyeLightGray = new MaterialBuilder(BreaLib.id("light_gray_dye"))
         * .fluid().color(0xC0C0C0).buildAndRegister();
         * 
         * DyeGray = new MaterialBuilder(BreaLib.id("gray_dye"))
         * .fluid().color(0x808080).buildAndRegister();
         * 
         * DyePink = new MaterialBuilder(BreaLib.id("pink_dye"))
         * .fluid().color(0xFFC0C0).buildAndRegister();
         * 
         * DyeLime = new MaterialBuilder(BreaLib.id("lime_dye"))
         * .fluid().color(0x80FF80).buildAndRegister();
         * 
         * DyeYellow = new MaterialBuilder(BreaLib.id("yellow_dye"))
         * .fluid().color(0xFFFF00).buildAndRegister();
         * 
         * DyeLightBlue = new MaterialBuilder(BreaLib.id("light_blue_dye"))
         * .fluid().color(0x6080FF).buildAndRegister();
         * 
         * DyeMagenta = new MaterialBuilder(BreaLib.id("magenta_dye"))
         * .fluid().color(0xFF00FF).buildAndRegister();
         * 
         * DyeOrange = new MaterialBuilder(BreaLib.id("orange_dye"))
         * .fluid().color(0xFF8000).buildAndRegister();
         * 
         * DyeWhite = new MaterialBuilder(BreaLib.id("white_dye"))
         * .fluid().color(0xFFFFFF).buildAndRegister();
         * 
         * TreatedWood = new MaterialBuilder(BreaLib.id("treated_wood"))
         * .wood()
         * .color(0x644218).secondaryColor(0x4e0b00).iconSet(DULL)
         * .flags(GENERATE_PLATE, FLAMMABLE, GENERATE_ROD, GENERATE_FRAME)
         * .buildAndRegister();
         * 
         * Sculk = new MaterialBuilder(BreaLib.id("sculk"))
         * .dust(1)
         * .color(0x015a5c).secondaryColor(0x001616).iconSet(DULL)
         * .buildAndRegister();
         * 
         * Wax = new MaterialBuilder(BreaLib.id("wax"))
         * .ingot().fluid()
         * .color(0xfabf29)
         * .flags(NO_SMELTING)
         * .buildAndRegister();
         */
    }
}
