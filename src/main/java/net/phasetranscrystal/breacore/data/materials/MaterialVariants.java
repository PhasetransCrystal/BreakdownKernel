package net.phasetranscrystal.breacore.data.materials;

import net.phasetranscrystal.breacore.api.BreaApi;
import net.phasetranscrystal.breacore.api.addon.AddonFinder;
import net.phasetranscrystal.breacore.api.addon.IBreaAddon;
import net.phasetranscrystal.breacore.api.material.register.MaterialVariant;

import static net.phasetranscrystal.breacore.api.material.register.RegisterActions.GeneralItem;
import static net.phasetranscrystal.breacore.api.material.register.RegisterConditions.*;

public class MaterialVariants {

    public static final MaterialVariant ingot = new MaterialVariant("ingot")
            .materialAmount(BreaApi.M)
            .addCondition(GenerateIngot)
            .addAction(GeneralItem);
    public static final MaterialVariant gem = new MaterialVariant("gem")
            .langValue("%s")
            .materialAmount(BreaApi.M)
            .addCondition(GenerateGem)
            .addAction(GeneralItem);
    public static final MaterialVariant nugget = new MaterialVariant("nugget")
            .materialAmount(BreaApi.M / 9)
            .addCondition(GenerateIngot)
            .addAction(GeneralItem);

    public static final MaterialVariant dust = new MaterialVariant("dust")
            .materialAmount(BreaApi.M)
            .addCondition(GenerateDust)
            .addAction(GeneralItem);

    public static final MaterialVariant dye = new MaterialVariant("dye")
            .materialAmount(-1);

    public static final MaterialVariant block = new MaterialVariant("block")
            .langValue("Block of %s")
            .materialAmount(BreaApi.M * 9);

    public static final MaterialVariant liquid = new MaterialVariant("liquid")
            .langValue("Liquid of %s")
            .materialAmount(BreaApi.M);

    public static final MaterialVariant melt = new MaterialVariant("melt")
            .langValue("Melt of %s")
            .materialAmount(BreaApi.M);

    public static void init() {
        AddonFinder.getAddonList().forEach(IBreaAddon::addMaterialVariant);
    }
}
