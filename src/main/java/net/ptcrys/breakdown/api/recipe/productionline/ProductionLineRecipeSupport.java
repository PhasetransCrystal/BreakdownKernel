package net.ptcrys.breakdown.api.recipe.productionline;

import net.ptcrys.breakdown.api.recipe.BuiltinBdApiResourceIntegrations;
import net.ptcrys.breakdown.api.recipe.common.FluidRecipeCapability;
import net.ptcrys.breakdown.api.recipe.common.ItemRecipeCapability;
import net.ptcrys.breakdown.api.recipe.common.ScalarRecipeCapability;
import net.ptcrys.topo.api.material.Material;
import net.ptcrys.topo.api.material.form.MaterialForm;
import net.ptcrys.topo.helper.MaterialHelper;

import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

final class ProductionLineRecipeSupport {

    private ProductionLineRecipeSupport() {}

    static ItemRecipeCapability items() {
        return BuiltinBdApiResourceIntegrations.ITEM.recipeCapability();
    }

    static FluidRecipeCapability fluids() {
        return BuiltinBdApiResourceIntegrations.FLUID.recipeCapability();
    }

    static ScalarRecipeCapability energy() {
        return BuiltinBdApiResourceIntegrations.ENERGY.recipeCapability();
    }

    static Supplier<Fluid> fluid(Material material, MaterialForm form) {
        return MaterialHelper.materialFluidSupplier(material, form);
    }
}
