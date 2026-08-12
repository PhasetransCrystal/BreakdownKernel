package net.ptcrys.breakdown.data.recipe;

import net.ptcrys.breakdown.data.BreakdownPlugin;
import net.ptcrys.topo.api.api.builtin.MachineDomainRegistration;
import net.ptcrys.topo.api.api.builtin.RecipeDomainRegistration;
import net.ptcrys.topo.api.machine.data.DataScope;
import net.ptcrys.topo.api.machine.resource.MachineResourceType;
import net.ptcrys.topo.api.machine.resource.MachineResourceTypes;
import net.ptcrys.topo.api.machine.resource.ResourceDataFieldFactory;
import net.ptcrys.topo.api.machine.ui.MachineUiIcons;
import net.ptcrys.topo.api.recipe.capability.RecipeCapability;
import net.ptcrys.topo.data.recipe.BuiltinTopoMachineUiIcons;
import net.ptcrys.topo.data.recipe.common.FluidRecipeCapability;
import net.ptcrys.topo.data.recipe.common.ItemRecipeCapability;

import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.resource.Resource;

import com.lowdragmc.lowdraglib2.gui.texture.IGuiTexture;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public final class BdResourceIntegrations {

    private static final MachineDomainRegistration MACHINE = BreakdownPlugin.INSTANCE.machine();
    private static final RecipeDomainRegistration RECIPE = BreakdownPlugin.INSTANCE.recipe();
    /**
     * 物品:SLOTTED 槽位形态,NeoForge 物品方块能力。
     */
    public static final BdResourceIntegration<ItemResource, ItemRecipeCapability> ITEM = item(
            "item",
            () -> BuiltinTopoMachineUiIcons.chest(0xFFC9A26B),
            "Item",
            "物品");

    /**
     * 流体:SLOTTED 槽位形态,NeoForge 流体方块能力。
     */
    public static final BdResourceIntegration<FluidResource, FluidRecipeCapability> FLUID = fluid(
            "fluid",
            () -> BuiltinTopoMachineUiIcons.drop(0xFF4FA3E8),
            "Fluid",
            "流体");

    private BdResourceIntegrations() {}

    public static void init() {}

    private static BdResourceIntegration<ItemResource, ItemRecipeCapability> item(String path, Supplier<IGuiTexture> icon, String en, String cn) {
        return integrate(path, ItemResource.class, Capabilities.Item.BLOCK, DataScope::itemResourceHandler, ItemRecipeCapability::new, icon, en, cn);
    }

    private static BdResourceIntegration<FluidResource, FluidRecipeCapability> fluid(String path, Supplier<IGuiTexture> icon, String en, String cn) {
        return integrate(path, FluidResource.class, Capabilities.Fluid.BLOCK, DataScope::fluidResourceHandler, FluidRecipeCapability::new, icon, en, cn);
    }

    private static <R extends Resource, C extends RecipeCapability<?, ?>> BdResourceIntegration<R, C> integrate(String path, Class<R> resourceClass, @Nullable BlockCapability<ResourceHandler<R>, @Nullable Direction> blockCapability, Function<MachineResourceType<R>, C> recipeCapabilityFactory, Supplier<IGuiTexture> icon, String en, String cn) {
        return integrate(path, resourceClass, blockCapability, MachineResourceTypes.valueIoDataFieldFactory(), recipeCapabilityFactory, icon, en, cn);
    }

    private static <R extends Resource, C extends RecipeCapability<?, ?>> BdResourceIntegration<R, C> integrate(String path, Class<R> resourceClass, @Nullable BlockCapability<ResourceHandler<R>, @Nullable Direction> blockCapability, ResourceDataFieldFactory<R> dataFieldFactory, Function<MachineResourceType<R>, C> recipeCapabilityFactory, Supplier<IGuiTexture> icon, String en, String cn) {
        MachineResourceType<R> resourceType = MACHINE.resourceTypeWithDataField(path, resourceClass, blockCapability, dataFieldFactory);
        C recipeCapability = RECIPE.capability(recipeCapabilityFactory.apply(resourceType));
        MACHINE.bindResourceName(resourceType, en, cn);
        MachineUiIcons.register(resourceType, Objects.requireNonNull(icon, "resource icon"));
        return new BdResourceIntegration<>(resourceType, recipeCapability);
    }

    public record BdResourceIntegration<R extends Resource, C extends RecipeCapability<?, ?>>(
                                                                                              MachineResourceType<R> resourceType,
                                                                                              C recipeCapability) {

        public BdResourceIntegration {
            Objects.requireNonNull(resourceType, "resource type");
            Objects.requireNonNull(recipeCapability, "recipe capability");
        }

        public Identifier id() {
            return resourceType.id();
        }
    }
}
