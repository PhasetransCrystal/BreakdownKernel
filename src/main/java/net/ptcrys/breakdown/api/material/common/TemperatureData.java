package net.ptcrys.breakdown.api.material.common;

/** 温度数据本体(开尔文 K);构造由 {@link TemperatureDataType} 铸造,调用方不能直接 new。 */
public final class TemperatureData {

    private final int kelvin;

    private TemperatureData(int kelvin) {
        this.kelvin = kelvin;
    }

    static TemperatureData create(int kelvin) {
        if (kelvin <= 0) {
            throw new IllegalArgumentException("temperature must be positive (K): " + kelvin);
        }
        return new TemperatureData(kelvin);
    }

    public int kelvin() {
        return kelvin;
    }
}
