package net.phasetranscrystal.breacore.api.material.attributes;

import lombok.Getter;

import java.util.Set;

public class FluidAttribute implements MaterialAttribute {

    @Override
    public boolean canBeAddedTo(MaterialAttributeSet currentSet) {
        return true;
    }

    @Override
    public Set<AttributeType<?>> getRequiredTypes() {
        return Set.of(AttributeType.GENERAL);
    }

    @Getter
    private final boolean withBucket;
    @Getter
    private final boolean withFlowing;

    public FluidAttribute(boolean withBucket, boolean withFlowing) {
        this.withBucket = withBucket;
        this.withFlowing = withFlowing;
    }

    public FluidAttribute() {
        this(true, false);
    }
}
