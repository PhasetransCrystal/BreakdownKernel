package net.phasetranscrystal.breacore.api.perk;

import net.phasetranscrystal.registrylib.composite.IComponentItem;

import net.minecraft.world.entity.EquipmentSlotGroup;

import java.util.List;
import java.util.Map;

public interface IPerkProvider<T extends IComponentItem<T>> extends IComponentItem<T> {

    Map<EquipmentSlotGroup, List<PerkStack>> getPerkStacks();
}
