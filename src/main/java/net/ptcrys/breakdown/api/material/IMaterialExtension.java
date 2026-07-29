package net.ptcrys.breakdown.api.material;

import net.ptcrys.breakdown.api.material.attributes.AttributeType;

public interface IMaterialExtension {

    private Material self() {
        return (Material) this;
    }

    default int getBlockHarvestLevel() {
        if (!self().hasAttribute(AttributeType.GENERAL))
            throw new IllegalArgumentException("Material " + self().getMaterialInfo().getIdentifier() +
                    " does not have a harvest level! Is probably a Fluid");
        int harvestLevel = self().getAttribute(AttributeType.GENERAL).getHarvestLevel();
        return harvestLevel > 0 ? harvestLevel - 1 : harvestLevel;
    }

    default boolean hasFluid() {
        return self().hasAttribute(AttributeType.FLUID);
    }
}
