package net.phasetranscrystal.breacore.api.material.attributes;

import java.util.Set;

public interface MaterialAttribute {

    boolean canBeAddedTo(MaterialAttributeSet currentSet);

    default Set<AttributeType<?>> getRequiredTypes() {
        return Set.of();
    }
}
