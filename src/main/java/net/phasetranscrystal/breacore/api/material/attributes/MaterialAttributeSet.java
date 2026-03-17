package net.phasetranscrystal.breacore.api.material.attributes;

import net.phasetranscrystal.breacore.api.material.Material;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class MaterialAttributeSet {

    private final Map<AttributeType<?>, MaterialAttribute> attributeMap;
    @Getter
    @Setter
    private Material material;

    public MaterialAttributeSet() {
        this.attributeMap = new HashMap<>();
        this.attributeMap.put(AttributeType.EMPTY, AttributeType.EMPTY.constructDefault().orElseThrow());
    }

    public boolean isEmpty() {
        return attributeMap.isEmpty();
    }

    public <T extends MaterialAttribute> T getAttribute(AttributeType<T> key) {
        return key.cast(attributeMap.get(key));
    }

    public <T extends MaterialAttribute> boolean hasAttribute(AttributeType<T> key) {
        return attributeMap.get(key) != null;
    }

    public <T extends MaterialAttribute> void setAttribute(AttributeType<T> key, T value) {
        if (value == null)
            throw new IllegalArgumentException("Material Attribute cannot be null");
        if (attributeMap.containsKey(key))
            throw new IllegalArgumentException("Material Attribute " + key.toString() + " already registered!");
        if (!value.canBeAddedTo(this))
            throw new IllegalArgumentException("Material Attribute " + key.toString() + " cannot be added to!");
        for (var type : value.getRequiredTypes()) {
            if (attributeMap.containsKey(type)) continue;
            var def = type.constructDefault();
            if (def.isEmpty())
                def = value.createDependency(type);
            var dep = def.orElseThrow(() -> new IllegalArgumentException("Material Attribute " + key.toString() + " cannot be constructed!"));
            attributeMap.put(key, dep);
            attributeMap.remove(AttributeType.EMPTY);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        attributeMap.forEach((k, v) -> sb.append(k.toString()).append("\n"));
        return sb.toString();
    }
}
