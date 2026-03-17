package net.phasetranscrystal.breacore.api.material.info;

import net.minecraft.client.renderer.block.model.Material;
import net.minecraft.resources.Identifier;

import lombok.Getter;

public enum MaterialIconLayer {

    BaseLayer("_base"),
    OverlayLayer("_overlay"),
    SecondaryLayer("_secondary"),
    MaskLayer("_mask"),
    DetailLayer("_detail"),
    FluidStillLayer("_fluid_still"),
    FluidFlowingLayer("_fluid_flowing");

    @Getter
    private final String suffix;

    MaterialIconLayer(String suffix) {
        this.suffix = suffix;
    }

    public Material getLayer(Identifier identifier) {
        return new Material(identifier.withSuffix(suffix));
    }
}
