package net.ptcrys.breakdown.api.material.common;

import java.util.List;
import java.util.Objects;

/**
 * 元素组成数据本体:复合材料(合金/化合物)的元素构成清单。
 * 构造由 {@link ElementCompositionDataType} 铸造,调用方不能直接 new。
 */
public final class ElementCompositionData {

    private final List<ElementEntry> entries;

    private ElementCompositionData(List<ElementEntry> entries) {
        this.entries = List.copyOf(entries);
    }

    static ElementCompositionData of(List<ElementEntry> entries) {
        if (entries.isEmpty()) {
            throw new IllegalArgumentException("element composition must not be empty");
        }
        return new ElementCompositionData(entries);
    }

    public List<ElementEntry> entries() {
        return entries;
    }

    /** 一个元素的组成条目:元素 + 数量(原子/摩尔比)。 */
    public record ElementEntry(Element element, int count) {

        public ElementEntry {
            Objects.requireNonNull(element, "element");
            if (count <= 0) {
                throw new IllegalArgumentException("element count must be positive: " + count);
            }
        }

        public static ElementEntry entry(Element element, int count) {
            return new ElementEntry(element, count);
        }
    }
}
