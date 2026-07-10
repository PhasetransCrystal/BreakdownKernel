package net.phasetranscrystal.breacore.common.data;

import net.phasetranscrystal.breacore.data.tags.BreaItemTags;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BreaTags {

    public static TagKey<Item> DEBUG_ITEM;

    public static void init() {
        BreaItemTags.init();
    }
}
