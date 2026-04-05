package net.phasetranscrystal.breacore.api.equipforge.data;

import net.minecraft.resources.Identifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.HashMap;
import java.util.Map;

public record EquipForgeGuiData(int gridWidth, int gridHeight, Map<Identifier, GuiPosition> slotPositions) {

    public static final Codec<EquipForgeGuiData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("gridWidth").forGetter(EquipForgeGuiData::gridWidth),
            Codec.INT.fieldOf("gridHeight").forGetter(EquipForgeGuiData::gridHeight),
            Codec.unboundedMap(Identifier.CODEC, GuiPosition.CODEC).fieldOf("slotPos").forGetter(EquipForgeGuiData::slotPositions))
            .apply(i, EquipForgeGuiData::new));

    public boolean validatePositions() {
        if (gridWidth < 1 || gridHeight < 1) {
            return false;
        }

        Map<Identifier, GuiPosition> positions = new HashMap<>(slotPositions);
        for (Map.Entry<Identifier, GuiPosition> entry : positions.entrySet()) {
            GuiPosition pos = entry.getValue();
            if (pos.x() < 0 || pos.x() >= gridWidth || pos.y() < 0 || pos.y() >= gridHeight) {
                return false;
            }
        }

        for (Map.Entry<Identifier, GuiPosition> entry1 : positions.entrySet()) {
            for (Map.Entry<Identifier, GuiPosition> entry2 : positions.entrySet()) {
                if (entry1.getKey().equals(entry2.getKey())) continue;
                if (entry1.getValue().x() == entry2.getValue().x() && entry1.getValue().y() == entry2.getValue().y()) {
                    return false;
                }
            }
        }

        return true;
    }
}
