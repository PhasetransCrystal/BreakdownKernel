package net.phasetranscrystal.breacore.data.materials;

import net.phasetranscrystal.breacore.api.BreaApi;
import net.phasetranscrystal.breacore.api.addon.AddonFinder;
import net.phasetranscrystal.breacore.api.addon.IBreaAddon;
import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.material.attributes.AttributeType;
import net.phasetranscrystal.breacore.api.material.variants.MaterialVariant;

import java.util.function.Predicate;

import static net.phasetranscrystal.breacore.data.materials.MaterialVariants.Conditions.*;

public class MaterialVariants {

    public enum Conditions implements Predicate<Material> {

        hasGeneralAttribute(mat -> mat.hasAttribute(AttributeType.GENERAL)),
        hasIngotAttribute(mat -> mat.hasAttribute(AttributeType.INGOT)),
        hasGemAttribute(mat -> mat.hasAttribute(AttributeType.GEM)),
        ;

        private final Predicate<Material> predicate;

        Conditions(Predicate<Material> predicate) {
            this.predicate = predicate;
        }

        @Override
        public boolean test(Material material) {
            return predicate.test(material);
        }
    }

    public static final MaterialVariant ingot = new MaterialVariant("ingot")
            .materialAmount(BreaApi.M)
            .generateItem(true)
            .generationCondition(hasIngotAttribute);
    public static final MaterialVariant gem = new MaterialVariant("gem")
            .langValue("%s")
            .materialAmount(BreaApi.M)
            .generateItem(true)
            .generationCondition(hasGemAttribute);
    public static final MaterialVariant nugget = new MaterialVariant("nugget")
            .materialAmount(BreaApi.M / 9)
            .generateItem(true)
            .generationCondition(hasIngotAttribute);

    public static final MaterialVariant dust = new MaterialVariant("dust")
            .materialAmount(BreaApi.M)
            .generateItem(true)
            .generationCondition(hasGeneralAttribute);

    public static final MaterialVariant dye = new MaterialVariant("dye")
            .materialAmount(-1);

    public static final MaterialVariant block = new MaterialVariant("block")
            .langValue("Block of %s")
            .materialAmount(BreaApi.M * 9)
            .generateBlock(true)
            .generationCondition(material -> hasIngotAttribute.test(material) || hasGemAttribute.test(material));

    public static void init() {
        AddonFinder.getAddonList().forEach(IBreaAddon::addMaterialVariant);
    }
}
