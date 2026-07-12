package net.phasetranscrystal.breacore.data.tags;

import net.phasetranscrystal.brealib.BreaLib;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static net.phasetranscrystal.breacore.common.data.BreaTags.*;

public class BreaItemTags {

    static {
        DEBUG_ITEM = createTag("debug");
        MATERIAL_ITEM = createTag("material");
    }

    public static void init() {}

    private static TagKey<Item> createTag(String key) {
        return ItemTags.create(BreaLib.id(key));
    }
}
