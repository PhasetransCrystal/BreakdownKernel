package net.phasetranscrystal.breacore.data.blocks;

import net.phasetranscrystal.brealib.util.FormattingUtil;

import net.phasetranscrystal.breacore.BreakdownCore;
import net.phasetranscrystal.breacore.api.BreaApi;
import net.phasetranscrystal.breacore.api.block.MaterialBlock;
import net.phasetranscrystal.breacore.api.block.OreBlock;
import net.phasetranscrystal.breacore.api.item.MaterialBlockItem;
import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.material.property.PropertyKey;
import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistrate;
import net.phasetranscrystal.breacore.api.tag.TagPrefix;
import net.phasetranscrystal.breacore.data.tagprefix.BreaTagPrefixes;

import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.world.level.block.Blocks;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;

public class BreaMaterialBlocks {

    // Reference Table Builders
    static ImmutableTable.Builder<TagPrefix, Material, BlockEntry<? extends MaterialBlock>> MATERIAL_BLOCKS_BUILDER = ImmutableTable
            .builder();
    // Reference Tables
    public static Table<TagPrefix, Material, BlockEntry<? extends MaterialBlock>> MATERIAL_BLOCKS;

    // Material Blocks
    public static void generateMaterialBlocks() {
        BreakdownCore.LOGGER.debug("Generating GTCEu Material Blocks...");

        for (TagPrefix tagPrefix : TagPrefix.values()) {
            if (!TagPrefix.ORES.containsKey(tagPrefix) && tagPrefix.doGenerateBlock()) {
                for (Material material : BreaApi.materialManager) {
                    BreaRegistrate registrate = BreaRegistrate.createIgnoringListenerErrors(material.getModid());
                    if (tagPrefix.doGenerateBlock(material)) {
                        registerMaterialBlock(tagPrefix, material, registrate);
                    }
                }
            }
        }
        BreakdownCore.LOGGER.debug("Generating GTCEu Material Blocks... Complete!");
    }

    private static void registerMaterialBlock(TagPrefix tagPrefix, Material material, BreaRegistrate registrate) {
        MATERIAL_BLOCKS_BUILDER.put(tagPrefix, material, registrate
                .block(tagPrefix.idPattern().formatted(material.getName()),
                        properties -> new MaterialBlock(properties, tagPrefix, material))
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> tagPrefix.blockProperties().properties().apply(p).noLootTable())
                .transform(BreaBlocks.unificationBlock(tagPrefix, material))
                .setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.LOOT, NonNullBiConsumer.noop())
                .blockstate(() -> (ctx, prov) -> {
                    prov.create(ctx.getEntry(), prov.getBuilder()
                            .texture(TextureSlot.ALL, ctx.getId())
                            .parent(ModelTemplates.CUBE_ALL.model.get())
                            .build(ctx.getEntry()));
                })
                // .color(() -> MaterialBlock::tintedColor)
                .item(MaterialBlockItem::new)
                .model(NonNullBiConsumer::noop)
                .model(() -> (ctx, prov) -> {
                    prov.generateBlockItem(ctx.getEntry(), l -> l);
                })
                // .color(() -> MaterialBlockItem::tintColor)
                .build()
                .register());
    }

    // Material Ore Blocks
    public static void generateOreBlocks() {
        BreakdownCore.LOGGER.debug("Generating GTCEu Ore Blocks...");
        for (Material material : BreaApi.materialManager) {
            if (allowOreBlock(material)) {
                BreaRegistrate registrate = BreaRegistrate.createIgnoringListenerErrors(material.getModid());
                registerOreBlock(material, registrate);
            }
        }
        BreakdownCore.LOGGER.debug("Generating GTCEu Ore Blocks... Complete!");
    }

    private static boolean allowOreBlock(Material material) {
        return material.hasProperty(PropertyKey.ORE);
    }

    private static void registerOreBlock(Material material, BreaRegistrate registrate) {
        for (var ore : TagPrefix.ORES.entrySet()) {
            if (ore.getKey().isIgnored(material)) continue;
            var oreTag = ore.getKey();
            final TagPrefix.OreType oreType = ore.getValue();
            var entry = registrate
                    .block("%s%s_ore".formatted(
                            oreTag != BreaTagPrefixes.ore ? FormattingUtil.toLowerCaseUnder(oreTag.name) + "_" : "",
                            material.getName()),
                            properties -> new OreBlock(properties, oreTag, material, true))
                    .initialProperties(() -> {
                        if (oreType.stoneType().get().isAir()) { // if the block is not registered (yet), fallback to
                            // stone
                            return Blocks.IRON_ORE;
                        }
                        return oreType.stoneType().get().getBlock();
                    })
                    .properties(properties -> BreaBlocks.copy(oreType.template().get(), properties).noLootTable())
                    .transform(BreaBlocks.unificationBlock(oreTag, material))
                    .blockstate(NonNullBiConsumer::noop)
                    .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                    .setData(ProviderType.LOOT, NonNullBiConsumer.noop())
                    .blockstate(() -> (ctx, prov) -> {
                        prov.create(ctx.getEntry(), prov.getBuilder()
                                .texture(TextureSlot.ALL, ctx.getId())
                                .parent(ModelTemplates.CUBE_ALL.model.get())
                                .build(ctx.getEntry()));
                    })
                    // .color(() -> MaterialBlock::tintedColor)
                    .item(MaterialBlockItem::new)
                    .model(NonNullBiConsumer::noop)
                    .model(() -> (ctx, prov) -> {
                        prov.generateBlockItem(ctx.getEntry(), l -> l);
                    })
                    // .color(() -> MaterialBlockItem::tintColor)
                    .build()
                    .register();
            MATERIAL_BLOCKS_BUILDER.put(oreTag, material, entry);
        }
    }
}
