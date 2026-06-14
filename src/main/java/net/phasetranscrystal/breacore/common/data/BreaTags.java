package net.phasetranscrystal.breacore.common.data;

import net.phasetranscrystal.brealib.BreaLib;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BreaTags {

    public static TagKey<Item> DEBUG = ItemTags.create(BreaLib.id("debug"));
}
