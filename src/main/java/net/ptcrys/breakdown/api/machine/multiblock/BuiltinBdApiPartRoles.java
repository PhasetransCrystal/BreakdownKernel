package net.ptcrys.breakdown.api.machine.multiblock;

import net.ptcrys.breakdown.api.BreakdownApiPlugin;
import net.ptcrys.breakdown.api.recipe.BuiltinBdApiResourceIntegrations;
import net.ptcrys.topo.api.api.builtin.MachineDomainRegistration;
import net.ptcrys.topo.api.machine.multiblock.ability.PartRole;
import net.ptcrys.topo.api.machine.resource.RecipeRole;

import java.util.Objects;

/**
 * Builtin {@link PartRole} declarations via {@link BreakdownApiPlugin#machine()}. Each call owns path,
 * en/cn display, resource family, and recipe role together (code-style §3.12).
 */
public final class BuiltinBdApiPartRoles {

    private static final MachineDomainRegistration MACHINE = BreakdownApiPlugin.INSTANCE.machine();

    public static final PartRole ITEM_INPUT = MACHINE.partRole(
            "item_input", "Item Input", "物品输入",
            BuiltinBdApiResourceIntegrations.ITEM.resourceType(), RecipeRole.INPUT);
    public static final PartRole ITEM_OUTPUT = MACHINE.partRole(
            "item_output", "Item Output", "物品输出",
            BuiltinBdApiResourceIntegrations.ITEM.resourceType(), RecipeRole.OUTPUT);
    public static final PartRole FLUID_INPUT = MACHINE.partRole(
            "fluid_input", "Fluid Input", "流体输入",
            BuiltinBdApiResourceIntegrations.FLUID.resourceType(), RecipeRole.INPUT);
    public static final PartRole FLUID_OUTPUT = MACHINE.partRole(
            "fluid_output", "Fluid Output", "流体输出",
            BuiltinBdApiResourceIntegrations.FLUID.resourceType(), RecipeRole.OUTPUT);
    public static final PartRole ENERGY_INPUT = MACHINE.partRole(
            "energy_input", "Energy Input", "能量输入",
            BuiltinBdApiResourceIntegrations.ENERGY.resourceType(), RecipeRole.INPUT);
    public static final PartRole ENERGY_OUTPUT = MACHINE.partRole(
            "energy_output", "Energy Output", "能量输出",
            BuiltinBdApiResourceIntegrations.ENERGY.resourceType(), RecipeRole.OUTPUT);

    private BuiltinBdApiPartRoles() {}

    public static void init() {
        Objects.requireNonNull(ENERGY_OUTPUT);
    }
}
