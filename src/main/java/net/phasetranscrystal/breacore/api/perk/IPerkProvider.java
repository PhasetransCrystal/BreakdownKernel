package net.phasetranscrystal.breacore.api.perk;

import net.phasetranscrystal.breacore.api.item.component.IItemComponent;

import net.minecraft.world.entity.EquipmentSlotGroup;

import java.util.List;
import java.util.Map;

public interface IPerkProvider extends IItemComponent {

    Map<EquipmentSlotGroup, List<PerkStack>> getPerkStacks();
}
