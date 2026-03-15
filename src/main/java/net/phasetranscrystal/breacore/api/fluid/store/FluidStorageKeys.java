package net.phasetranscrystal.breacore.api.fluid.store;

import net.phasetranscrystal.brealib.BreaLib;

import net.phasetranscrystal.breacore.api.fluid.FluidState;
import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.material.property.FluidProperty;
import net.phasetranscrystal.breacore.api.material.property.PropertyKey;
import net.phasetranscrystal.breacore.data.materials.BreaMaterialIconTypes;

import org.jetbrains.annotations.NotNull;

public final class FluidStorageKeys {

    public static final FluidStorageKey LIQUID = new FluidStorageKey(BreaLib.id("liquid"),
            "liquids",
            BreaMaterialIconTypes.liquid,
            m -> prefixedRegisteredName("liquid_", FluidStorageKeys.LIQUID, m),
            m -> m.hasProperty(PropertyKey.DUST) ? "breacore.fluid.liquid_generic" : "breacore.fluid.generic",
            FluidState.LIQUID, 0);

    public static final FluidStorageKey GAS = new FluidStorageKey(BreaLib.id("gas"),
            "gases",
            BreaMaterialIconTypes.gas,
            m -> postfixedRegisteredName("_gas", FluidStorageKeys.GAS, m),
            m -> {
                if (m.hasProperty(PropertyKey.DUST)) {
                    return "breacore.fluid.gas_vapor";
                }
                if (m.isElement()) {
                    FluidProperty property = m.getProperty(PropertyKey.FLUID);
                    if (m.isElement() || (property != null && property.getPrimaryKey() != FluidStorageKeys.LIQUID)) {
                        return "breacore.fluid.gas_generic";
                    }
                }
                return "breacore.fluid.generic";
            },
            FluidState.GAS, 0);

    public static final FluidStorageKey PLASMA = new FluidStorageKey(BreaLib.id("plasma"),
            "plasmas",
            BreaMaterialIconTypes.plasma,
            m -> m.getName() + "_plasma",
            m -> "breacore.fluid.plasma",
            FluidState.PLASMA, -1);

    public static final FluidStorageKey MOLTEN = new FluidStorageKey(BreaLib.id("molten"),
            "molten",
            BreaMaterialIconTypes.molten,
            m -> "molten_" + m.getName(),
            m -> "breacore.fluid.molten",
            FluidState.LIQUID, -1);

    private FluidStorageKeys() {}

    private static @NotNull String prefixedRegisteredName(@NotNull String prefix, @NotNull FluidStorageKey key,
                                                          @NotNull Material material) {
        FluidProperty property = material.getProperty(PropertyKey.FLUID);
        if (property != null && property.getPrimaryKey() != key) {
            return prefix + material.getName();
        }
        return material.getName();
    }

    private static @NotNull String postfixedRegisteredName(@NotNull String postfix, @NotNull FluidStorageKey key,
                                                           @NotNull Material material) {
        FluidProperty property = material.getProperty(PropertyKey.FLUID);
        if (property != null && property.getPrimaryKey() != key) {
            return material.getName() + postfix;
        }
        return material.getName();
    }
}
