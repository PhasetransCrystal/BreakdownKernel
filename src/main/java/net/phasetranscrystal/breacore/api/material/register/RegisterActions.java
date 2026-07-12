package net.phasetranscrystal.breacore.api.material.register;

import net.phasetranscrystal.brealib.api.registry.registrate.BreaFluidTypeExtensions;

import net.phasetranscrystal.breacore.api.block.MaterialBlock;
import net.phasetranscrystal.breacore.api.item.MaterialBlockItem;
import net.phasetranscrystal.breacore.api.item.MaterialItem;
import net.phasetranscrystal.breacore.api.material.attributes.AttributeType;

public class RegisterActions {

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
        var builder = registrate.fluid(variant.idPattern().formatted(material.getName()), BreaFluidTypeExtensions.FLUID_LIQUID_STILL, BreaFluidTypeExtensions.FLUID_LIQUID_FLOWING);
        if (!attr.isWithFlowing()) builder.noBlock();
        if (!attr.isWithBucket()) builder.noBucket();
        builder.register();
    };
}
