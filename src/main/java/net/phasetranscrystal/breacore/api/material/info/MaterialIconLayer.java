package net.phasetranscrystal.breacore.api.material.info;

import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;

import lombok.Getter;

public enum MaterialIconLayer {

    BaseLayer("_base"),
    SecondaryLayer("_secondary"),
    OverlayLayer("_overlay"),
    MaskLayer("_mask"),
    DetailLayer("_detail"),
    FluidLayer("_fluid"),
    ;

    @Getter
    private final String suffix;

    MaterialIconLayer(String suffix) {
        this.suffix = suffix;
    }

    public Material getLayer(Identifier identifier) {
        return new Material(identifier.withSuffix(suffix));
    }
}
