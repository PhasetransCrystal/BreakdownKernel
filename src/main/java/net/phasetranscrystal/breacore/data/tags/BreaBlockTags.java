package net.phasetranscrystal.breacore.data.tags;

import net.phasetranscrystal.brealib.BreaLib;

import net.minecraft.tags.BlockTags;

import static net.phasetranscrystal.breacore.common.data.BreaTags.*;

public class BreaBlockTags {

    static {
        DEBUG_BLOCK = BlockTags.create(BreaLib.id("debug"));
        MACHINE_BLOCK = BlockTags.create(BreaLib.id("machine"));
    }

    public static void init() {}
}
