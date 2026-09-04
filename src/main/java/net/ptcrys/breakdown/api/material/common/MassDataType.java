package net.ptcrys.breakdown.api.material.common;

import net.ptcrys.topo.api.material.data.MaterialDataType;
import net.ptcrys.topo.api.material.data.MaterialDataUse;

import net.minecraft.resources.Identifier;

/** 材料质量数据类型。 */
public final class MassDataType extends MaterialDataType<MassData> {

    public MassDataType(Identifier id) {
        super(id);
    }

    public MaterialDataUse<MassData> mass(int mass) {
        return use(MassData.create(mass));
    }
}
