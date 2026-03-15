package net.phasetranscrystal.breacore.data.datagen.tag;

import net.phasetranscrystal.breacore.BreakdownCore;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class DamageTagsLoader extends TagsProvider<DamageType> {

    protected DamageTagsLoader(PackOutput output, ResourceKey<? extends Registry<DamageType>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, registryKey, lookupProvider, BreakdownCore.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // DamageTypeData.allInNamespace(BreakdownCore.MOD_ID).forEach(damageTypeData ->
        // damageTypeData.tags.forEach(damageTypeTagKey -> tag(damageTypeTagKey).add(damageTypeData.key)));
    }
}
