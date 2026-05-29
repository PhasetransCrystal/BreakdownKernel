package net.phasetranscrystal.breacore.api.material.register;

import net.phasetranscrystal.breacore.api.block.MaterialBlock;
import net.phasetranscrystal.breacore.api.item.MaterialBlockItem;
import net.phasetranscrystal.breacore.api.item.MaterialItem;
import net.phasetranscrystal.breacore.api.material.attributes.AttributeType;

public class RegisterActions {

    public static RegisterAction GeneralItem = (registrate, variant, material) -> {
        var attr = material.getAttribute(AttributeType.GENERAL);
        registrate.object(variant.idPattern().formatted(material.getName()));
        var builder = registrate.item(prop -> new MaterialItem(variant, material, prop));
        if (attr.getBurnTime() > 0) builder.burnTime(attr.getBurnTime());
        builder.properties(prop -> prop.enchantable(attr.getHarvestLevel()));
        builder.register();
    };
    public static RegisterAction GeneralBlock = (registrate, variant, material) -> {
        var attr = material.getAttribute(AttributeType.GENERAL);
        registrate.object(variant.idPattern().formatted(material.getName()));
        var builder = registrate.block(prop -> new MaterialBlock(variant, material, prop));
        var itemBuilder = builder.item((block, prop) -> new MaterialBlockItem(variant, material, block, prop));
        if (attr.getBurnTime() > 0) itemBuilder.burnTime(attr.getBurnTime());
        itemBuilder.properties(prop -> prop.enchantable(attr.getHarvestLevel()));
        itemBuilder.build();
        builder.register();
    };
    public static RegisterAction GeneralFluid = (registrate, variant, material) -> {
        var attr = material.getAttribute(AttributeType.FLUID);
        registrate.object(variant.idPattern().formatted(material.getName()));
        var builder = registrate.fluid();
        if (!attr.isWithFlowing()) builder.noBlock();
        if (!attr.isWithBucket()) builder.noBucket();
        builder.register();
    };
}
