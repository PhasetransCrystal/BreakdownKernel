package net.phasetranscrystal.breacore.api.fluid.attribute;

import net.phasetranscrystal.brealib.BreaLib;

import net.minecraft.network.chat.Component;

public final class FluidAttributes {

    /**
     * Attribute for acidic fluids.
     */
    public static final FluidAttribute ACID = new FluidAttribute(BreaLib.id("acid"),
            list -> list.accept(Component.translatable("breacore.fluid.type_acid.tooltip")),
            list -> list.accept(Component.translatable("breacore.fluid_pipe.acid_proof")));

    private FluidAttributes() {}
}
