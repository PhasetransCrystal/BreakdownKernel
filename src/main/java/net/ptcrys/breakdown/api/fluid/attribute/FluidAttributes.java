package net.ptcrys.breakdown.api.fluid.attribute;

import net.ptcrys.breakdown.BreaLib;

import net.minecraft.network.chat.Component;

public final class FluidAttributes {

    /**
     * Attribute for acidic fluids.
     */
    public static final FluidAttribute ACID = new FluidAttribute(BreaLib.id("acid"),
            list -> list.accept(Component.translatable("breakdown.fluid.type_acid.tooltip")),
            list -> list.accept(Component.translatable("breakdown.fluid_pipe.acid_proof")));

    private FluidAttributes() {}
}
