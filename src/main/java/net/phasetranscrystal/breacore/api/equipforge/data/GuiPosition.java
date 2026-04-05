package net.phasetranscrystal.breacore.api.equipforge.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Objects;

public class GuiPosition {

    public static final Codec<GuiPosition> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("x").forGetter(GuiPosition::x),
            Codec.INT.fieldOf("y").forGetter(GuiPosition::y)).apply(i, GuiPosition::new));

    private final int x;
    private final int y;

    public GuiPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuiPosition that = (GuiPosition) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
