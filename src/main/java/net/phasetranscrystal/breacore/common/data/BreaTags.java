package net.phasetranscrystal.breacore.common.data;

import net.phasetranscrystal.breacore.data.tags.BreaBlockTags;
import net.phasetranscrystal.breacore.data.tags.BreaFluidTags;
import net.phasetranscrystal.breacore.data.tags.BreaItemTags;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class BreaTags {

    public static TagKey<Item> DEBUG_ITEM;
    public static TagKey<Block> DEBUG_BLOCK;

    public static TagKey<Item> MATERIAL_ITEM;
    public static TagKey<Block> MATERIAL_BLOCK;
    public static TagKey<Fluid> MATERIAL_FLUID;

    public static TagKey<Block> MACHINE_BLOCK;

    public static void init() {
        BreaItemTags.init();
        BreaBlockTags.init();
        BreaFluidTags.init();
    }
}
