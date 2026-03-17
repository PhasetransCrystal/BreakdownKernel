package net.phasetranscrystal.breacore.api.item;

import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.material.variants.MaterialVariant;

import net.minecraft.world.item.Item;

import lombok.Getter;

public class MaterialItem extends Item {

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
