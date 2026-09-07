package net.ptcrys.breakdown.api.equipment.common;

import net.ptcrys.breakdown.api.recipe.BuiltinBdRecipeTypes;
import net.ptcrys.registrylib.util.entry.ItemEntry;
import net.ptcrys.topo.api.material.Material;

import net.minecraft.world.item.Item;

/**
 * Optional assembly recipe for an equipment kind when {@link EquipmentPattern} is not enough (e.g.
 * stick + ingot forge hammers). Emit via {@link BuiltinBdRecipeTypes} builders;
 * invoked once per registered (kind, material) item.
 */
@FunctionalInterface
public interface EquipmentExtraRecipe {

    void emit(Material material, ItemEntry<Item> entry);
}
