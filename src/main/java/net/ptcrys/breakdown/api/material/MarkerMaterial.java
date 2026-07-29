package net.ptcrys.breakdown.api.material;

import net.ptcrys.breakdown.BreaLib;

import net.minecraft.resources.Identifier;

public final class MarkerMaterial extends Material {

    public static MarkerMaterial NULL = new MarkerMaterial(BreaLib.id("null"));

    public MarkerMaterial(Identifier identifier) {
        super(identifier);
    }

    @Override
    public void verifyMaterial() {}

    @Override
    public String toString() {
        return getIdentifier().toString();
    }
}
