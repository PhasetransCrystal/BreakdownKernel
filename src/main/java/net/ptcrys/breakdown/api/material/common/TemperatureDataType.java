package net.ptcrys.breakdown.api.material.common;

import net.ptcrys.topo.api.material.data.MaterialDataType;
import net.ptcrys.topo.api.material.data.MaterialDataUse;

import net.minecraft.resources.Identifier;

/**
 * 温度物理量数据类型(开尔文 K):温度 / 熔点 / 沸点共用同一数据本体,仅以注册 id 区分语义。
 * 数据在声明行即校验(正值)。
 */
public final class TemperatureDataType extends MaterialDataType<TemperatureData> {

    public TemperatureDataType(Identifier id) {
        super(id);
    }

    public MaterialDataUse<TemperatureData> kelvin(int kelvin) {
        return use(TemperatureData.create(kelvin));
    }
}
