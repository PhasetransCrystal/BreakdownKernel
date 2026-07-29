package net.ptcrys.breakdown.data.tags;

import net.ptcrys.breakdown.BreaLib;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static net.ptcrys.breakdown.common.data.BreaTags.*;

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
