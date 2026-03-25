package net.phasetranscrystal.breacore.api.material.register;

import net.phasetranscrystal.breacore.api.block.MaterialBlock;
import net.phasetranscrystal.breacore.api.item.MaterialItem;

public class RegisterActions {

    public static RegisterAction GeneralItem = (registrate, variant, material) -> {
        registrate.item(variant.idPattern().formatted(material.getName()), prop -> new MaterialItem(variant, material, prop))
                .register();
    };
    public static RegisterAction GeneralBlock = (registrate, variant, material) -> {
        registrate.block(variant.idPattern().formatted(material.getName()), prop -> new MaterialBlock(variant, material, prop))
                .simpleItem()
                .register();
    };
}
