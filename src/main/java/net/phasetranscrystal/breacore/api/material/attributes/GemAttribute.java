package net.phasetranscrystal.breacore.api.material.attributes;

import java.util.Set;

public class GemAttribute implements MaterialAttribute {

    @Override
    public boolean canBeAddedTo(MaterialAttributeSet currentSet) {
        return !currentSet.hasAttribute(AttributeType.INGOT);
    }

    @Override
    public Set<AttributeType<?>> getRequiredTypes() {
        return Set.of(AttributeType.GENERAL);
    }
}
