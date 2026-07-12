package net.phasetranscrystal.breacore.data.tags;

import net.phasetranscrystal.brealib.BreaLib;

import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import static net.phasetranscrystal.breacore.common.data.BreaTags.*;

public class BreaFluidTags {

    static {
        MATERIAL_FLUID = createTag("material");
    }

    public static void init() {}

    private static TagKey<Fluid> createTag(String key) {
        return FluidTags.create(BreaLib.id(key));
    }
}
