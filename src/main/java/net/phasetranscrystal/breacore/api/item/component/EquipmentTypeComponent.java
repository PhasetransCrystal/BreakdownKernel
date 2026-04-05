package net.phasetranscrystal.breacore.api.item.component;

import net.phasetranscrystal.breacore.api.equipforge.EquipmentType;
import net.phasetranscrystal.breacore.api.equipforge.data.EquipForgeData;

public class EquipmentTypeComponent implements IItemComponent {

    private final EquipmentType type;
    private final EquipForgeData data;

    public EquipmentTypeComponent(EquipmentType type, EquipForgeData data) {
        this.type = type;
        this.data = data;
    }

    public EquipmentType getType() {
        return type;
    }

    public EquipForgeData getData() {
        return data;
    }

    @Override
    public void onAttached(net.minecraft.world.item.Item item) {}
}
