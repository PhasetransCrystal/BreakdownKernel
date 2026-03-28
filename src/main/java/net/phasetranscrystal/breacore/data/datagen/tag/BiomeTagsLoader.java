package net.phasetranscrystal.breacore.data.datagen.tag;

import net.phasetranscrystal.breacore.BreakdownCore;
import net.phasetranscrystal.breacore.data.tags.CustomTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class BiomeTagsLoader extends BiomeTagsProvider {

    public BiomeTagsLoader(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BreakdownCore.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(CustomTags.HAS_RUBBER_TREE).addTag(Tags.Biomes.IS_SWAMP).addTag(BiomeTags.IS_FOREST).addTag(BiomeTags.IS_JUNGLE);
    }
}
