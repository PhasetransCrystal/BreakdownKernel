package net.phasetranscrystal.breacore.api.material.attributes;

public class FluidAttribute implements MaterialAttribute {

    @Override
    public boolean canBeAddedTo(MaterialAttributeSet currentSet) {
        return true;
    }
}
