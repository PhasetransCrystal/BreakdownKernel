package net.phasetranscrystal.breacore.data.materials;

import net.phasetranscrystal.breacore.api.BreaApi;
import net.phasetranscrystal.breacore.api.addon.AddonFinder;
import net.phasetranscrystal.breacore.api.addon.IBreaAddon;
import net.phasetranscrystal.breacore.api.material.register.MaterialVariant;

import static net.phasetranscrystal.breacore.api.material.register.RegisterActions.*;
import static net.phasetranscrystal.breacore.api.material.register.RegisterConditions.*;
import static net.phasetranscrystal.breacore.data.misc.BreaCreativeModeTabs.*;

public class MaterialVariants {

    public static final MaterialVariant ingot = new MaterialVariant("ingot")
            .itemCreativeTab(() -> MATERIAL_ITEM)
            .materialAmount(BreaApi.M)
            .addCondition(GenerateIngot)
            .addAction(GeneralItem);
    public static final MaterialVariant gem = new MaterialVariant("gem")
            .itemCreativeTab(() -> MATERIAL_ITEM)
            .langValue("%s")
            .materialAmount(BreaApi.M)
            .addCondition(GenerateGem)
            .addAction(GeneralItem);
    public static final MaterialVariant nugget = new MaterialVariant("nugget")
            .itemCreativeTab(() -> MATERIAL_ITEM)
            .materialAmount(BreaApi.M / 9)
            .addCondition(GenerateIngot)
            .addAction(GeneralItem);

    public static final MaterialVariant dust = new MaterialVariant("dust")
            .itemCreativeTab(() -> MATERIAL_ITEM)
            .materialAmount(BreaApi.M)
            .addCondition(GenerateDust)
            .addAction(GeneralItem);

    public static final MaterialVariant dye = new MaterialVariant("dye")
            .itemCreativeTab(() -> MATERIAL_ITEM)
            .materialAmount(-1);

    public static final MaterialVariant block = new MaterialVariant("block")
            .itemCreativeTab(() -> MATERIAL_BLOCK)
            .langValue("Block of %s")
            .materialAmount(BreaApi.M * 9)
            .addCondition(material -> GenerateGem.validate(material) || GenerateIngot.validate(material))
            .addAction(GeneralBlock);

    public static final MaterialVariant liquid = new MaterialVariant("liquid")
            .itemCreativeTab(() -> MATERIAL_FLUID)
            .langValue("Liquid of %s")
            .materialAmount(BreaApi.M)
            .addCondition(GenerateFluid)
            .addAction(GeneralFluid);

    public static final MaterialVariant melt = new MaterialVariant("melt")
            .itemCreativeTab(() -> MATERIAL_FLUID)
            .langValue("Melt of %s")
            .materialAmount(BreaApi.M)
            .addCondition(GenerateFluid)
            .addAction(GeneralFluid);

    public static void init() {
        AddonFinder.getAddonList().forEach(IBreaAddon::addMaterialVariant);
    }
}
