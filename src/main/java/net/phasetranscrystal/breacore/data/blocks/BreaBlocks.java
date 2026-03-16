package net.phasetranscrystal.breacore.data.blocks;

import net.phasetranscrystal.brealib.util.memoization.CacheMemoizer;

import net.phasetranscrystal.breacore.api.block.debug.CheckMatBlock;
import net.phasetranscrystal.breacore.api.block.debug.MuiTestBlock;
import net.phasetranscrystal.breacore.api.material.ItemMaterialData;
import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.material.stack.MaterialEntry;
import net.phasetranscrystal.breacore.api.tag.TagPrefix;
import net.phasetranscrystal.breacore.common.block.StoneBlockType;
import net.phasetranscrystal.breacore.common.block.StoneTypes;
import net.phasetranscrystal.breacore.data.items.MaterialItems;
import net.phasetranscrystal.breacore.data.misc.BreaCreativeModeTabs;
import net.phasetranscrystal.breacore.mixins.BlockPropertiesAccessor;

import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.common.Tags;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static net.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;

public class BreaBlocks {

    public static BlockEntry<CheckMatBlock> MatCheckBlock;

    public static BlockEntry<MuiTestBlock> TestMuiBlock;
    public static Table<StoneBlockType, StoneTypes, BlockEntry<Block>> STONE_BLOCKS;

    public static void init() {
        DebugBlocks.init();
        // Decor Blocks
        generateStoneBlocks();

        // Procedural Blocks
        REGISTRATE.creativeModeTab(() -> BreaCreativeModeTabs.MATERIAL_BLOCK);
        BreaMaterialBlocks.generateMaterialBlocks();   // Compressed Blocks
        BreaMaterialBlocks.generateOreBlocks();        // Ore Blocks
        BreaMaterialBlocks.MATERIAL_BLOCKS = BreaMaterialBlocks.MATERIAL_BLOCKS_BUILDER.build();

        BreaMaterialBlocks.MATERIAL_BLOCKS_BUILDER = null;
    }

    public static <P, T extends Block,
            S2 extends BlockBuilder<T, P>> NonNullFunction<S2, S2> unificationBlock(@NotNull TagPrefix tagPrefix,
                                                                                    @NotNull Material mat) {
        return builder -> {
            builder.onRegister(block -> {
                Supplier<Block> blockSupplier = CacheMemoizer.memoizeBlockSupplier(() -> block);
                MaterialEntry entry = new MaterialEntry(tagPrefix, mat);
                MaterialItems.toUnify.put(entry, blockSupplier);
                ItemMaterialData.registerMaterialEntry(blockSupplier, entry);
            });
            return builder;
        };
    }

    public static void generateStoneBlocks() {
        // Stone type blocks
        ImmutableTable.Builder<StoneBlockType, StoneTypes, BlockEntry<Block>> builder = ImmutableTable.builder();
        for (StoneTypes strata : StoneTypes.values()) {
            if (!strata.generateBlocks) continue;
            for (StoneBlockType type : StoneBlockType.values()) {
                String blockId = type.blockId.formatted(strata.getSerializedName());
                if (BuiltInRegistries.BLOCK.containsKey(Identifier.parse(blockId))) continue;
                var entry = REGISTRATE.block(blockId, Block::new)
                        .initialProperties(() -> Blocks.STONE)
                        .properties(p -> p.strength(type.hardness, type.resistance).mapColor(strata.mapColor))
                        .transform(type == StoneBlockType.STONE ?
                                BreaBlocks.unificationBlock(strata.getTagPrefix(), strata.getMaterial()) :
                                builder2 -> builder2)
                        .tag(BlockTags.MINEABLE_WITH_PICKAXE, Tags.Blocks.NEEDS_WOOD_TOOL)
                        .loot((tables, block) -> {
                            if (type == StoneBlockType.STONE) {
                                tables.add(block, tables.createSingleItemTableWithSilkTouch(block,
                                        STONE_BLOCKS.get(StoneBlockType.COBBLE, strata).get()));
                            } else {
                                tables.add(block, tables.createSingleItemTable(block));
                            }
                        })
                        .item()
                        .build();
                if (type == StoneBlockType.STONE && strata.isNatural()) {
                    entry.tag(BlockTags.STONE_ORE_REPLACEABLES, BlockTags.BASE_STONE_OVERWORLD,
                            BlockTags.DRIPSTONE_REPLACEABLE, BlockTags.MOSS_REPLACEABLE);
                    // .blockstate(GTModels.randomRotatedModel(GTCEu.id(ModelProvider.BLOCK_FOLDER + "/stones/" +
                    // strata.getSerializedName() + "/" + type.id));
                } else {
                    entry.blockstate(() -> (ctx, prov) -> {
                        prov.create(ctx.getEntry(), prov.getBuilder()
                                .texture(TextureSlot.ALL, prov.modLoc("block/stones/" + strata.getSerializedName() + "/" + type.id))
                                .build(ctx.getEntry()));
                    });
                }
                if (type == StoneBlockType.STONE) {
                    entry.tag(Tags.Blocks.STONES);
                }
                if (type == StoneBlockType.COBBLE) {
                    entry.tag(Tags.Blocks.COBBLESTONES);
                }
                builder.put(type, strata, entry.register());
            }
        }
        STONE_BLOCKS = builder.build();
    }

    /**
     * kinda nasty block property copy function because one doesn't exist.
     *
     * @param props the props to copy
     * @return a shallow copy of the block properties like {@link BlockBehaviour.Properties#ofFullCopy(BlockBehaviour)}
     *         does
     */
    public static BlockBehaviour.Properties copy(BlockBehaviour.Properties props, BlockBehaviour.Properties newProps) {
        if (props == null) {
            return newProps;
        }
        newProps.destroyTime(((BlockPropertiesAccessor) props).getDestroyTime());
        newProps.explosionResistance(((BlockPropertiesAccessor) props).getExplosionResistance());
        if (!((BlockPropertiesAccessor) props).isHasCollision()) newProps.noCollision();
        if (((BlockPropertiesAccessor) props).isIsRandomlyTicking()) newProps.randomTicks();
        newProps.lightLevel(((BlockPropertiesAccessor) props).getLightEmission());
        newProps.mapColor(((BlockPropertiesAccessor) props).getMapColor());
        newProps.sound(((BlockPropertiesAccessor) props).getSoundType());
        newProps.friction(((BlockPropertiesAccessor) props).getFriction());
        newProps.speedFactor(((BlockPropertiesAccessor) props).getSpeedFactor());
        if (((BlockPropertiesAccessor) props).isDynamicShape()) newProps.dynamicShape();
        if (!((BlockPropertiesAccessor) props).isCanOcclude()) newProps.noOcclusion();
        if (((BlockPropertiesAccessor) props).isIsAir()) newProps.air();
        if (((BlockPropertiesAccessor) props).isIgnitedByLava()) newProps.ignitedByLava();
        if (((BlockPropertiesAccessor) props).isLiquid()) newProps.liquid();
        if (((BlockPropertiesAccessor) props).isForceSolidOff()) newProps.forceSolidOff();
        if (((BlockPropertiesAccessor) props).isForceSolidOn()) newProps.forceSolidOn();
        newProps.pushReaction(((BlockPropertiesAccessor) props).getPushReaction());
        if (((BlockPropertiesAccessor) props).isRequiresCorrectToolForDrops()) newProps.requiresCorrectToolForDrops();
        ((BlockPropertiesAccessor) newProps).setOffsetFunction(((BlockPropertiesAccessor) props).getOffsetFunction());
        if (!((BlockPropertiesAccessor) props).isSpawnTerrainParticles()) newProps.noTerrainParticles();
        ((BlockPropertiesAccessor) newProps)
                .setRequiredFeatures(((BlockPropertiesAccessor) props).getRequiredFeatures());
        newProps.emissiveRendering(((BlockPropertiesAccessor) props).getEmissiveRendering());
        newProps.instrument(((BlockPropertiesAccessor) props).getInstrument());
        if (((BlockPropertiesAccessor) props).isReplaceable()) newProps.replaceable();
        return newProps;
    }
}
