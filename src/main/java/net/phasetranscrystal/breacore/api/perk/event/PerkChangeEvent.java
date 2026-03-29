package net.phasetranscrystal.breacore.api.perk.event;

import net.phasetranscrystal.breacore.api.perk.Perk;
import net.phasetranscrystal.breacore.api.perk.PerkChangeType;
import net.phasetranscrystal.breacore.api.perk.PerkInfo;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

import lombok.Getter;

@Getter
public class PerkChangeEvent extends LivingEvent {

    private final Perk perk;
    private final PerkChangeType changeType;
    private final float oldLevel;
    private final float newLevel;
    private final PerkInfo perkInfo;

    public PerkChangeEvent(LivingEntity entity, Perk perk, PerkChangeType changeType, float oldLevel, float newLevel, PerkInfo perkInfo) {
        super(entity);
        this.perk = perk;
        this.changeType = changeType;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
        this.perkInfo = perkInfo;
    }
}
