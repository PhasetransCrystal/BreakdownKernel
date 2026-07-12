package net.phasetranscrystal.breacore.data.tags;

import net.phasetranscrystal.brealib.BreaLib;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static net.phasetranscrystal.breacore.common.data.BreaTags.*;

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
