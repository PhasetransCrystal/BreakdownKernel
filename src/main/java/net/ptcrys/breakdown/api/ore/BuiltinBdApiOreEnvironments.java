package net.ptcrys.breakdown.api.ore;

import net.ptcrys.breakdown.api.material.BuiltinBdApiMaterialForms;
import net.ptcrys.topo.api.ore.OreDimensionRule;
import net.ptcrys.topo.api.ore.OreEnvironment;
import net.ptcrys.topo.api.ore.OreEnvironments;
import net.ptcrys.topo.api.ore.OreHostRule;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public final class BuiltinBdApiOreEnvironments {

    private static final TagKey<Block> STONE_ORE_REPLACEABLES = vanillaBlockTag("stone_ore_replaceables");
    private static final TagKey<Block> DEEPSLATE_ORE_REPLACEABLES = vanillaBlockTag("deepslate_ore_replaceables");
    private static final TagKey<Block> BASE_STONE_NETHER = vanillaBlockTag("base_stone_nether");

    private static final OreDimensionRule OVERWORLD = new OreDimensionRule(
            Level.OVERWORLD, BiomeTags.IS_OVERWORLD, BuiltinBdApiOreLang.DIM_OVERWORLD);
    private static final OreDimensionRule NETHER = new OreDimensionRule(
            Level.NETHER, BiomeTags.IS_NETHER, BuiltinBdApiOreLang.DIM_NETHER);

    private static final OreHostRule STONE_HOST = new OreHostRule(
            STONE_ORE_REPLACEABLES, BuiltinBdApiMaterialForms.ORE, BuiltinBdApiOreLang.HOST_STONE);
    private static final OreHostRule DEEPSLATE_HOST = new OreHostRule(
            DEEPSLATE_ORE_REPLACEABLES, BuiltinBdApiMaterialForms.DEEPSLATE_ORE, BuiltinBdApiOreLang.HOST_DEEPSLATE);
    private static final OreHostRule NETHERRACK_HOST = new OreHostRule(
            BASE_STONE_NETHER, BuiltinBdApiMaterialForms.NETHERRACK_ORE, BuiltinBdApiOreLang.HOST_NETHERRACK);

    public static final OreEnvironment OVERWORLD_STONE = OreEnvironments.of(OVERWORLD, STONE_HOST);

    public static final OreEnvironment OVERWORLD_DEEPSLATE = OreEnvironments.of(OVERWORLD, DEEPSLATE_HOST);

    public static final OreEnvironment OVERWORLD_STONE_AND_DEEPSLATE = OreEnvironments.of(OVERWORLD, STONE_HOST, DEEPSLATE_HOST);

    public static final OreEnvironment NETHER_NETHERRACK = OreEnvironments.of(NETHER, NETHERRACK_HOST);

    private BuiltinBdApiOreEnvironments() {}

    public static void init() {}

    private static TagKey<Block> vanillaBlockTag(String path) {
        return TagKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(path));
    }
}
