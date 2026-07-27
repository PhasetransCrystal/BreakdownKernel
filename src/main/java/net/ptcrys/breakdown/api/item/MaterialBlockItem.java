package net.ptcrys.breakdown.api.item;

import net.ptcrys.breakdown.api.material.Material;
import net.ptcrys.breakdown.api.material.register.MaterialVariant;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

import lombok.Getter;

public class MaterialBlockItem extends BlockItem {

    @Getter
    private final MaterialVariant variant;
    @Getter
    private final Material material;

    public MaterialBlockItem(MaterialVariant variant, Material mat, Block block, Properties properties) {
        super(block, properties);
        this.variant = variant;
        this.material = mat;
    }

    public void onRegister() {}
}
