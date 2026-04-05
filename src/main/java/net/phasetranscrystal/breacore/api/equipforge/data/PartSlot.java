package net.phasetranscrystal.breacore.api.equipforge.data;

import net.phasetranscrystal.breacore.api.equipforge.PartType;

import net.minecraft.resources.Identifier;

public record PartSlot(Identifier slotId, int guiX, int guiY, boolean required, PartType partType) {

}
