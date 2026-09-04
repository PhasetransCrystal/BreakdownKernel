package net.ptcrys.breakdown.api.recipe;

import net.ptcrys.breakdown.api.BreakdownApiPlugin;
import net.ptcrys.breakdown.api.machine.common.component.resource.ScalarDestroyedBehavior;
import net.ptcrys.breakdown.api.machine.common.component.resource.ScalarDestroyedBehaviors;
import net.ptcrys.breakdown.api.machine.common.component.resource.ScalarResource;
import net.ptcrys.breakdown.api.recipe.common.FluidRecipeCapability;
import net.ptcrys.breakdown.api.recipe.common.ItemRecipeCapability;
import net.ptcrys.breakdown.api.recipe.common.ScalarRecipeCapability;
import net.ptcrys.topo.api.api.builtin.MachineDomainRegistration;
import net.ptcrys.topo.api.api.builtin.RecipeDomainRegistration;
import net.ptcrys.topo.api.api.lang.LangKey;
import net.ptcrys.topo.api.machine.data.DataScope;
import net.ptcrys.topo.api.machine.resource.MachineResourceType;
import net.ptcrys.topo.api.machine.resource.MachineResourceTypes;
import net.ptcrys.topo.api.machine.resource.ResourceDataFieldFactory;
import net.ptcrys.topo.api.machine.ui.MachineUiIcons;
import net.ptcrys.topo.api.recipe.capability.RecipeCapability;

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
import java.util.function.IntFunction;
import java.util.function.Supplier;

public final class BuiltinBdResourceIntegrations {

    private static final MachineDomainRegistration MACHINE = BreakdownApiPlugin.INSTANCE.machine();
    private static final RecipeDomainRegistration RECIPE = BreakdownApiPlugin.INSTANCE.recipe();

    /** 物品:SLOTTED 槽位形态,NeoForge 物品方块能力。 */
    public static final BdResourceIntegration<ItemResource, ItemRecipeCapability> ITEM = item(
            "item",
            () -> BuiltinBdMachineUiIcons.chest(0xFFC9A26B),
            "Item",
            "物品");

    /** 流体:SLOTTED 槽位形态,NeoForge 流体方块能力。 */
    public static final BdResourceIntegration<FluidResource, FluidRecipeCapability> FLUID = fluid(
            "fluid",
            () -> BuiltinBdMachineUiIcons.drop(0xFF4FA3E8),
            "Fluid",
            "流体");

    /** 能量:IMPLICIT 资源条形态;燃烧类发电机的产物,电力消耗机器的口粮。被破坏走电容放电(密度 1)。 */
    public static final BdResourceIntegration<ScalarResource, ScalarRecipeCapability> ENERGY = scalar(
            "energy", "Energy", "能量",
            0xFFE8C547, BuiltinBdMachineUiIcons::bolt, ScalarDestroyedBehaviors.energyDischarge(1));

    private BuiltinBdResourceIntegrations() {}

    public static void init() {}

    private static BdResourceIntegration<ItemResource, ItemRecipeCapability> item(
                                                                                  String path,
                                                                                  Supplier<IGuiTexture> icon,
                                                                                  String en,
                                                                                  String cn) {
        MachineResourceType<ItemResource> resourceType = MACHINE.resourceTypeWithDataField(
                path, ItemResource.class, Capabilities.Item.BLOCK, itemDataFieldFactory());
        ItemRecipeCapability recipeCapability = RECIPE.capability(new ItemRecipeCapability(resourceType));
        // Resource type + capability + name + icon declared together (resource family ownership).
        MACHINE.bindResourceName(resourceType, en, cn);
        MachineUiIcons.register(resourceType, Objects.requireNonNull(icon, "resource icon"));
        return new BdResourceIntegration<>(resourceType, recipeCapability);
    }

    private static BdResourceIntegration<FluidResource, FluidRecipeCapability> fluid(
                                                                                     String path,
                                                                                     Supplier<IGuiTexture> icon,
                                                                                     String en,
                                                                                     String cn) {
        MachineResourceType<FluidResource> resourceType = MACHINE.resourceTypeWithDataField(
                path, FluidResource.class, Capabilities.Fluid.BLOCK, fluidDataFieldFactory());
        FluidRecipeCapability recipeCapability = RECIPE.capability(new FluidRecipeCapability(resourceType));
        MACHINE.bindResourceName(resourceType, en, cn);
        MachineUiIcons.register(resourceType, Objects.requireNonNull(icon, "resource icon"));
        return new BdResourceIntegration<>(resourceType, recipeCapability);
    }

    private static ResourceDataFieldFactory<ItemResource> itemDataFieldFactory() {
        return DataScope::itemResourceHandler;
    }

    private static ResourceDataFieldFactory<FluidResource> fluidDataFieldFactory() {
        return DataScope::fluidResourceHandler;
    }

    private static <R extends Resource, C extends RecipeCapability<?, ?>> BdResourceIntegration<R, C> integrate(
                                                                                                                String path,
                                                                                                                Class<R> resourceClass,
                                                                                                                @Nullable BlockCapability<ResourceHandler<R>, @Nullable Direction> blockCapability,
                                                                                                                Function<MachineResourceType<R>, C> recipeCapabilityFactory,
                                                                                                                Supplier<IGuiTexture> icon,
                                                                                                                String en,
                                                                                                                String cn) {
        return integrate(
                path,
                resourceClass,
                blockCapability,
                MachineResourceTypes.valueIoDataFieldFactory(),
                recipeCapabilityFactory,
                icon,
                en,
                cn);
    }

    private static <R extends Resource, C extends RecipeCapability<?, ?>> BdResourceIntegration<R, C> integrate(
                                                                                                                String path,
                                                                                                                Class<R> resourceClass,
                                                                                                                @Nullable BlockCapability<ResourceHandler<R>, @Nullable Direction> blockCapability,
                                                                                                                ResourceDataFieldFactory<R> dataFieldFactory,
                                                                                                                Function<MachineResourceType<R>, C> recipeCapabilityFactory,
                                                                                                                Supplier<IGuiTexture> icon,
                                                                                                                String en,
                                                                                                                String cn) {
        MachineResourceType<R> resourceType = MACHINE.resourceTypeWithDataField(path, resourceClass, blockCapability, dataFieldFactory);
        C recipeCapability = RECIPE.capability(recipeCapabilityFactory.apply(resourceType));
        MACHINE.bindResourceName(resourceType, en, cn);
        MachineUiIcons.register(resourceType, Objects.requireNonNull(icon, "resource icon"));
        return new BdResourceIntegration<>(resourceType, recipeCapability);
    }

    private static BdResourceIntegration<ScalarResource, ScalarRecipeCapability> scalar(
                                                                                        String path,
                                                                                        String en,
                                                                                        String cn,
                                                                                        int color,
                                                                                        IntFunction<IGuiTexture> iconRecipe,
                                                                                        ScalarDestroyedBehavior destroyedBehavior) {
        Identifier id = MACHINE.id(path);
        MachineResourceType<ScalarResource> resourceType = MACHINE.resourceTypeWithDataField(
                path,
                ScalarResource.class,
                BlockCapability.createSided(id, ResourceHandler.asClass()),
                MachineResourceTypes.valueIoDataFieldFactory());
        // Domain owns lang mint; same LangKey shared by type + scalar resource handle.
        LangKey nameLang = MACHINE.bindResourceName(resourceType, en, cn);
        ScalarResource resource = ScalarResource.register(id, nameLang, color, destroyedBehavior);
        ScalarRecipeCapability recipeCapability = RECIPE.capability(new ScalarRecipeCapability(id, resourceType, resource));
        MachineUiIcons.register(resourceType, () -> iconRecipe.apply(color));
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
