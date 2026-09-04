package net.ptcrys.breakdown.api.recipe;

import net.ptcrys.breakdown.api.BreakdownApiPlugin;
import net.ptcrys.breakdown.api.recipe.craft.CookingRecipeBuilder;
import net.ptcrys.breakdown.api.recipe.craft.CookingTopoRecipeType;
import net.ptcrys.breakdown.api.recipe.craft.ShapedCraftingRecipeType;
import net.ptcrys.breakdown.api.recipe.craft.ShapelessCraftingRecipeType;
import net.ptcrys.breakdown.api.recipe.craft.VanillaExportedRecipeAdapters;
import net.ptcrys.topo.api.api.builtin.RecipeDomainRegistration;
import net.ptcrys.topo.api.recipe.TopoRecipe;
import net.ptcrys.topo.api.recipe.TopoRecipeType;
import net.ptcrys.topo.api.recipe.VanillaFacingPaths;

import net.minecraft.world.item.crafting.RecipeType;

import com.lowdragmc.lowdraglib2.gui.ui.data.FillDirection;

import java.util.Objects;

/**
 * 瓦解核心内置配方类型(骨架):仅注册 Minecraft 原本就有的 facing 类型——
 * 原版熔炉(SMELTING/BLASTING/SMOKING/CAMPFIRE)与原版合成(CRAFTING_SHAPED/SHAPELESS),
 * 均带对原版配方表的导出桥接(exportRecipesTo)。机器配方类型由内容插件按需注册。
 */
public final class BuiltinBdRecipeTypes {

    private static final RecipeDomainRegistration RECIPE = BreakdownApiPlugin.INSTANCE.recipe();

    // ── 原版 facing 烹饪(桥接:Topo 配方 ↔ 原版熔炉 JSON)──────────────────────
    public static final CookingTopoRecipeType SMELTING = registerCooking(
            "topo_smelting",
            CookingRecipeBuilder.Kind.SMELTING,
            VanillaFacingPaths.SMELTING,
            "Smelting",
            "熔炉熔炼");
    public static final CookingTopoRecipeType BLASTING = registerCooking(
            "topo_blasting",
            CookingRecipeBuilder.Kind.BLASTING,
            VanillaFacingPaths.BLASTING,
            "Blasting Table",
            "高炉熔炼表");
    public static final CookingTopoRecipeType SMOKING = registerCooking(
            "topo_smoking",
            CookingRecipeBuilder.Kind.SMOKING,
            VanillaFacingPaths.SMOKING,
            "Smoking",
            "烟熏");
    public static final CookingTopoRecipeType CAMPFIRE = registerCooking(
            "topo_campfire",
            CookingRecipeBuilder.Kind.CAMPFIRE,
            VanillaFacingPaths.CAMPFIRE_COOKING,
            "Campfire Cooking",
            "营火烹饪");

    public static final ShapedCraftingRecipeType CRAFTING_SHAPED = registerShaped(
            "crafting_shaped", "Shaped Crafting", "有序合成");
    public static final ShapelessCraftingRecipeType CRAFTING_SHAPELESS = registerShapeless(
            "crafting_shapeless", "Shapeless Crafting", "无序合成");

    private BuiltinBdRecipeTypes() {}

    public static void init() {
        Objects.requireNonNull(SMELTING);
        Objects.requireNonNull(CRAFTING_SHAPED);
    }

    private static CookingTopoRecipeType registerCooking(
                                                         String path,
                                                         CookingRecipeBuilder.Kind kind,
                                                         String kindFolder,
                                                         String en,
                                                         String cn) {
        CookingTopoRecipeType type = register(
                path,
                new CookingTopoRecipeType(RECIPE.id(path), kind, kindFolder),
                en,
                cn,
                "smelt");
        type.exportRecipesTo(
                VanillaExportedRecipeAdapters.cooking(RECIPE, kind), type.foreignRecipeType());
        return type;
    }

    private static ShapedCraftingRecipeType registerShaped(String path, String en, String cn) {
        ShapedCraftingRecipeType type = register(
                path, new ShapedCraftingRecipeType(RECIPE.id(path)), en, cn, "arrow");
        type.exportRecipesTo(VanillaExportedRecipeAdapters.shaped(RECIPE), RecipeType.CRAFTING);
        return type;
    }

    private static ShapelessCraftingRecipeType registerShapeless(String path, String en, String cn) {
        ShapelessCraftingRecipeType type = register(
                path, new ShapelessCraftingRecipeType(RECIPE.id(path)), en, cn, "arrow");
        type.exportRecipesTo(VanillaExportedRecipeAdapters.shapeless(RECIPE), RecipeType.CRAFTING);
        return type;
    }

    private static <T extends TopoRecipeType<TopoRecipe>> T register(
                                                                     String path,
                                                                     T recipeType,
                                                                     String en,
                                                                     String cn,
                                                                     String progressBar) {
        T registered = RECIPE.recipeType(path, recipeType);
        registered
                .displayName(en, cn)
                .progressBar(
                        RECIPE.id("textures/gui/progress_bar/" + progressBar + ".png"),
                        FillDirection.LEFT_TO_RIGHT,
                        20,
                        40);
        return registered;
    }
}
