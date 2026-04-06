package net.phasetranscrystal.breacore.api.material.attributes;

import java.util.Set;

public class IngotAttribute implements MaterialAttribute {

    @Override
    public boolean canBeAddedTo(MaterialAttributeSet currentSet) {
        return !currentSet.hasAttribute(AttributeType.GEM);
    }

    @Override
    public Set<AttributeType<?>> getRequiredTypes() {
        return Set.of(AttributeType.GENERAL);
    }
}
