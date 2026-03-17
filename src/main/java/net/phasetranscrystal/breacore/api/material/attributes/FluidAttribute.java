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
    private boolean withBucket = true;
    @Getter
    private boolean withBlock = false;

    public FluidAttribute(boolean withBucket, boolean withBlock) {
        this.withBucket = withBucket;
        this.withBlock = withBlock;
    }

    public FluidAttribute() {}
}
