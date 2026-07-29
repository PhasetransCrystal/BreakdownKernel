package net.ptcrys.breakdown.api.item;

import net.ptcrys.breakdown.api.material.Material;
import net.ptcrys.breakdown.api.material.register.MaterialVariant;
import net.ptcrys.registrylib.composite.ComponentItem;

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
