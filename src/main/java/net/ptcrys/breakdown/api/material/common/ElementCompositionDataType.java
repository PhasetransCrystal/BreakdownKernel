package net.ptcrys.breakdown.api.material.common;

import net.ptcrys.topo.api.material.data.MaterialDataType;
import net.ptcrys.topo.api.material.data.MaterialDataUse;

import net.minecraft.resources.Identifier;

import java.util.List;

/**
 * 元素组成数据类型:材料声明自身由哪些元素、各多少构成(合金/化合物)。
 * 数据在声明行即校验(非空、数量为正)。
 */
public final class ElementCompositionDataType extends MaterialDataType<ElementCompositionData> {

    public ElementCompositionDataType(Identifier id) {
        super(id);
    }

    public MaterialDataUse<ElementCompositionData> of(ElementCompositionData.ElementEntry... entries) {
        return use(ElementCompositionData.of(List.of(entries)));
    }

    /** 单元素组成,如纯元素材料 {@code of(Element.FE, 1)}。 */
    public MaterialDataUse<ElementCompositionData> of(Element element, int count) {
        return of(ElementCompositionData.ElementEntry.entry(element, count));
    }
}
