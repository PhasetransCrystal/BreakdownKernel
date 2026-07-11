package net.phasetranscrystal.breacore.api.item;

import net.phasetranscrystal.registrylib.composite.ComponentItem;

import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.material.register.MaterialVariant;

import lombok.Getter;

public class MaterialItem extends ComponentItem {

    @Getter
    private final MaterialVariant variant;
    @Getter
    private final Material material;

    public MaterialItem(MaterialVariant variant, Material mat, Properties properties) {
        super(properties);
        this.variant = variant;
        this.material = mat;
    }

    public void onRegister() {}
}
