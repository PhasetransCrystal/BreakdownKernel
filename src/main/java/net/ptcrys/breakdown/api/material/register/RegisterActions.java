package net.ptcrys.breakdown.api.material.register;

import net.ptcrys.breakdown.api.block.MaterialBlock;
import net.ptcrys.breakdown.api.item.MaterialBlockItem;
import net.ptcrys.breakdown.api.item.MaterialItem;
import net.ptcrys.breakdown.api.material.attributes.AttributeType;

import net.minecraft.resources.Identifier;

public class RegisterActions {

    private static final Identifier FLUID_STILL = Identifier.fromNamespaceAndPath("registrylib", "block/fluid/liquid_still");
    private static final Identifier FLUID_FLOW = Identifier.fromNamespaceAndPath("registrylib", "block/fluid/liquid_flow");

    public static RegisterAction GeneralItem = (registrate, variant, material) -> {
        var attr = material.getAttribute(AttributeType.GENERAL);
        registrate.getRegistryLib().defaultCreativeTab(variant.itemCreativeTab().get().getKey());
        var builder = registrate.item(variant.idPattern().formatted(material.getName()), prop -> new MaterialItem(variant, material, prop), true);
        builder.properties(prop -> prop.enchantable(attr.getHarvestLevel()));
        builder.register();
    };
    public static RegisterAction GeneralBlock = (registrate, variant, material) -> {
        var attr = material.getAttribute(AttributeType.GENERAL);
        registrate.getRegistryLib().defaultCreativeTab(variant.itemCreativeTab().get().getKey());
        var builder = registrate.block(variant.idPattern().formatted(material.getName()), prop -> new MaterialBlock(variant, material, prop));
        builder.item((block, prop) -> new MaterialBlockItem(variant, material, block, prop), itemBuilder -> itemBuilder
                .properties(prop -> prop.enchantable(attr.getHarvestLevel())));
        builder.register();
    };
    public static RegisterAction GeneralFluid = (registrate, variant, material) -> {
        var attr = material.getAttribute(AttributeType.FLUID);
        registrate.getRegistryLib().defaultCreativeTab(variant.itemCreativeTab().get().getKey());
        var builder = registrate.fluid(variant.idPattern().formatted(material.getName()), FLUID_STILL, FLUID_FLOW);
        if (!attr.isWithFlowing()) builder.noBlock();
        if (!attr.isWithBucket()) builder.noBucket();
        builder.register();
    };
}
