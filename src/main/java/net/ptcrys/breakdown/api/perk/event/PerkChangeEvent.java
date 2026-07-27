package net.ptcrys.breakdown.api.perk.event;

import net.ptcrys.breakdown.api.perk.Perk;
import net.ptcrys.breakdown.api.perk.PerkChangeType;
import net.ptcrys.breakdown.api.perk.PerkInfo;

import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.Event;

import lombok.Getter;

@Getter
public class PerkChangeEvent extends Event {

    private final Entity entity;
    private final Perk perk;
    private final PerkChangeType changeType;
    private final float oldLevel;
    private final float newLevel;
    private final PerkInfo perkInfo;

    public PerkChangeEvent(Entity entity, Perk perk, PerkChangeType changeType, float oldLevel, float newLevel, PerkInfo perkInfo) {
        this.entity = entity;
        this.perk = perk;
        this.changeType = changeType;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
        this.perkInfo = perkInfo;
    }
}
