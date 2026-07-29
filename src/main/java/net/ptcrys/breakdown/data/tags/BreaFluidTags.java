package net.ptcrys.breakdown.data.tags;

import net.ptcrys.breakdown.BreaLib;

import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import static net.ptcrys.breakdown.common.data.BreaTags.*;

public class BreaFluidTags {

    static {
        MATERIAL_FLUID = createTag("material");
    }

    public static void init() {}

    private static TagKey<Fluid> createTag(String key) {
        return FluidTags.create(BreaLib.id(key));
    }
}
