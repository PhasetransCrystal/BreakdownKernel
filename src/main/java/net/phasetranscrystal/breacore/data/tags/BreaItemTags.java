package net.phasetranscrystal.breacore.data.tags;

import net.phasetranscrystal.brealib.BreaLib;

import net.minecraft.tags.ItemTags;

import static net.phasetranscrystal.breacore.common.data.BreaTags.*;

public class BreaItemTags {

    static {
        DEBUG_ITEM = ItemTags.create(BreaLib.id("debug"));
    }

    public static void init() {}
}
