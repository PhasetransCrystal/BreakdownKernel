package net.phasetranscrystal.breacore.api.material.registry;

import net.phasetranscrystal.breacore.api.material.attributes.AttributeType;
import net.phasetranscrystal.breacore.api.material.attributes.GeneralAttribute;

public interface IMaterialBuilderExtension {

    private MaterialBuilder self() {
        return (MaterialBuilder) this;
    }

    default MaterialBuilder dust() {
        self().addAttribute(AttributeType.GENERAL);
        return self();
    }

    default MaterialBuilder dust(int harvestLevel) {
        return dust(harvestLevel, 0);
    }

    default MaterialBuilder dust(int harvestLevel, int burnTime) {
        self().setAttribute(AttributeType.GENERAL, new GeneralAttribute(harvestLevel, burnTime));
        return self();
    }

    default MaterialBuilder ingot() {
        self().addAttribute(AttributeType.INGOT);
        return self();
    }

    default MaterialBuilder ingot(int harvestLevel) {
        return ingot(harvestLevel, 0);
    }

    default MaterialBuilder ingot(int harvestLevel, int burnTime) {
        var general = self().getAttribute(AttributeType.GENERAL);
        if (general == null) dust(harvestLevel, burnTime);
        else {
            general.setHarvestLevel(harvestLevel);
            general.setBurnTime(burnTime);
        }
        self().addAttribute(AttributeType.INGOT);
        return self();
    }

    default MaterialBuilder gem() {
        self().addAttribute(AttributeType.GEM);
        return self();
    }

    default MaterialBuilder gem(int harvestLevel) {
        return gem(harvestLevel, 0);
    }

    default MaterialBuilder gem(int harvestLevel, int burnTime) {
        var general = self().getAttribute(AttributeType.GENERAL);
        if (general == null) dust(harvestLevel, burnTime);
        else {
            general.setHarvestLevel(harvestLevel);
            general.setBurnTime(burnTime);
        }
        self().addAttribute(AttributeType.GEM);
        return self();
    }
}
