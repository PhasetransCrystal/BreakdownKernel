package net.ptcrys.breakdown.data.tags;

import net.ptcrys.breakdown.BreaLib;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static net.ptcrys.breakdown.common.data.BreaTags.*;

public class BreaBlockTags {

    static {
        DEBUG_BLOCK = createTag("debug");
        MATERIAL_BLOCK = createTag("material");
        MACHINE_BLOCK = createTag("machine");
    }

    public static void init() {}

    private static TagKey<Block> createTag(String key) {
        return BlockTags.create(BreaLib.id(key));
    }
}
