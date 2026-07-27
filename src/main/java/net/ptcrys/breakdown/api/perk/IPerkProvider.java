package net.ptcrys.breakdown.api.perk;

import net.ptcrys.registrylib.composite.IComponentItem;

import net.minecraft.world.entity.EquipmentSlotGroup;

import java.util.List;
import java.util.Map;

public interface IPerkProvider<T extends IComponentItem<T>> extends IComponentItem<T> {

    Map<EquipmentSlotGroup, List<PerkStack>> getPerkStacks();
}
