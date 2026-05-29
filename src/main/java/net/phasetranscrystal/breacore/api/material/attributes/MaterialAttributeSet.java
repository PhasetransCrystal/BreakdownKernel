package net.phasetranscrystal.breacore.api.material.attributes;

import net.phasetranscrystal.brealib.BreaLib;

import net.phasetranscrystal.breacore.BreakdownCore;
import net.phasetranscrystal.breacore.api.material.Material;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        return attributeMap.containsKey(AttributeType.EMPTY);
    }

    public <T extends MaterialAttribute> T getAttribute(AttributeType<T> key) {
        if (attributeMap.containsKey(key)) {
            return key.cast(attributeMap.get(key));
        }
        throw new IllegalArgumentException("Attribute " + key + " not found");
    }

    public <T extends MaterialAttribute> boolean hasAttribute(AttributeType<T> key) {
        return attributeMap.containsKey(key);
    }

    public <T extends MaterialAttribute> void setAttribute(AttributeType<T> key, T value) {
        setAttributeInternal(key, value, new HashSet<>());
    }

    private <T extends MaterialAttribute> void setAttributeInternal(AttributeType<T> key, T value, Set<AttributeType<?>> stack) {
        if (attributeMap.containsKey(key)) {
            throw new IllegalArgumentException("Already registered: " + key.getKey());
        }
        if (!value.canBeAddedTo(this)) {
            throw new IllegalArgumentException("Conflicts: " + key.getKey());
        }
        if (stack.contains(key)) {
            throw new IllegalStateException("Circular dependency: " + stack + " -> " + key);
        }
        stack.add(key);
        for (AttributeType<?> depType : value.getRequiredTypes()) {
            if (attributeMap.containsKey(depType)) continue;
            var dep = depType.constructDefault();
            if (dep.isEmpty()) {
                if (BreaLib.isProd()) {
                    BreakdownCore.LOGGER.warn("Empty dependency found for {}", key.getKey());
                    return;
                }
                throw new IllegalStateException("Empty dependency found for " + key.getKey());
            }
            // 递归添加依赖（注意：递归调用时类型是安全的，因为 depType 与 dep 匹配）
            setAttributeInternal((AttributeType) depType, dep.get(), stack);
        }
        attributeMap.put(key, value);
        attributeMap.remove(AttributeType.EMPTY);
        stack.remove(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        attributeMap.forEach((k, v) -> sb.append(k.toString()).append("\n"));
        return sb.toString();
    }
}
