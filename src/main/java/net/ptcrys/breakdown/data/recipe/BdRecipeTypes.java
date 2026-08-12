package net.ptcrys.breakdown.data.recipe;

import net.ptcrys.breakdown.data.BreakdownPlugin;
import net.ptcrys.topo.api.api.builtin.RecipeDomainRegistration;
import net.ptcrys.topo.api.recipe.TopoRecipe;
import net.ptcrys.topo.api.recipe.TopoRecipeType;

import com.lowdragmc.lowdraglib2.gui.ui.data.FillDirection;

import static net.ptcrys.topo.data.recipe.BuiltinTopoRecipeTypes.*;

public final class BdRecipeTypes {

    private static final RecipeDomainRegistration RECIPE = BreakdownPlugin.INSTANCE.recipe();

    public static final TopoRecipeType<TopoRecipe> ELECTRIC_FURNACE = register(
            "electric_furnace", "Electric Smelting", "电力熔炼", "smelt")
            .importRecipesFrom(SMELTING::poweredImport, SMELTING.foreignRecipeType());
    public static final TopoRecipeType<TopoRecipe> BLAST_FURNACE = register(
            "blast_furnace", "Blasting", "高炉熔炼", "smelt")
            .importRecipesFrom(BLASTING::poweredImport, BLASTING.foreignRecipeType());

    private BdRecipeTypes() {}

    public static void init() {}

    private static TopoRecipeType<TopoRecipe> register(String path, String en, String cn, String progressBar) {
        return register(path, en, cn, progressBar, FillDirection.LEFT_TO_RIGHT, 20, 40);
    }

    private static TopoRecipeType<TopoRecipe> register(
                                                       String path,
                                                       String en,
                                                       String cn,
                                                       String progressBar,
                                                       FillDirection direction,
                                                       int textureWidth,
                                                       int textureHeight) {
        return RECIPE.recipeType(path)
                .displayName(en, cn)
                .progressBar(
                        RECIPE.id("textures/gui/progress_bar/" + progressBar + ".png"),
                        direction,
                        textureWidth,
                        textureHeight);
    }

    private static <T extends TopoRecipeType<TopoRecipe>> T register(
                                                                     String path,
                                                                     T recipeType,
                                                                     String en,
                                                                     String cn,
                                                                     String progressBar) {
        return register(path, recipeType, en, cn, progressBar, FillDirection.LEFT_TO_RIGHT, 20, 40);
    }

    private static <T extends TopoRecipeType<TopoRecipe>> T register(
                                                                     String path,
                                                                     T recipeType,
                                                                     String en,
                                                                     String cn,
                                                                     String progressBar,
                                                                     FillDirection direction,
                                                                     int textureWidth,
                                                                     int textureHeight) {
        T registered = RECIPE.recipeType(path, recipeType);
        registered
                .displayName(en, cn)
                .progressBar(
                        RECIPE.id("textures/gui/progress_bar/" + progressBar + ".png"),
                        direction,
                        textureWidth,
                        textureHeight);
        return registered;
    }
}
