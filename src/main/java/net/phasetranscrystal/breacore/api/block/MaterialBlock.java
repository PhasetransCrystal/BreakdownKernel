package net.phasetranscrystal.breacore.api.block;

import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.material.register.MaterialVariant;

import net.minecraft.world.level.block.Block;

import lombok.Getter;

public class MaterialBlock extends Block {

    @Getter
    private final MaterialVariant variant;
    @Getter
    private final Material material;

    public MaterialBlock(MaterialVariant variant, Material material, Properties properties) {
        super(properties);
        this.variant = variant;
        this.material = material;
    }
}
