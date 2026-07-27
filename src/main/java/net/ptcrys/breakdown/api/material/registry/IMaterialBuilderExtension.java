package net.ptcrys.breakdown.api.material.registry;

import net.ptcrys.breakdown.api.material.Material;
import net.ptcrys.breakdown.api.material.attributes.AttributeType;
import net.ptcrys.breakdown.api.material.attributes.GeneralAttribute;

public interface IMaterialBuilderExtension {

    private Material.Builder self() {
        return (Material.Builder) this;
    }

    default Material.Builder dust() {
        self().addAttribute(AttributeType.GENERAL);
        return self();
    }

    default Material.Builder dust(int harvestLevel) {
        return dust(harvestLevel, 0);
    }

    default Material.Builder dust(int harvestLevel, int burnTime) {
        self().setAttribute(AttributeType.GENERAL, new GeneralAttribute(harvestLevel, burnTime));
        return self();
    }

    default Material.Builder ingot() {
        self().addAttribute(AttributeType.INGOT);
        return self();
    }

    default Material.Builder ingot(int harvestLevel) {
        return ingot(harvestLevel, 0);
    }

    default Material.Builder ingot(int harvestLevel, int burnTime) {
        var general = self().getAttribute(AttributeType.GENERAL);
        if (general == null) dust(harvestLevel, burnTime);
        else {
            general.setHarvestLevel(harvestLevel);
            general.setBurnTime(burnTime);
        }
        self().addAttribute(AttributeType.INGOT);
        return self();
    }

    default Material.Builder gem() {
        self().addAttribute(AttributeType.GEM);
        return self();
    }

    default Material.Builder gem(int harvestLevel) {
        return gem(harvestLevel, 0);
    }

    default Material.Builder gem(int harvestLevel, int burnTime) {
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
