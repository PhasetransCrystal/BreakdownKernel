package net.ptcrys.breakdown.api.material.common;

/** 材料质量数据本体(相对原子质量,取整);构造由 {@link MassDataType#mass} 铸造,调用方不能直接 new。 */
public final class MassData {

    private final int value;

    private MassData(int value) {
        this.value = value;
    }

    static MassData create(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("mass must be positive: " + value);
        }
        return new MassData(value);
    }

    public int value() {
        return value;
    }
}
