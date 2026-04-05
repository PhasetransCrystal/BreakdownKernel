package net.phasetranscrystal.breacore.api.equipforge;

import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.perk.PerkStack;
import net.phasetranscrystal.breacore.api.registry.BreaRegistries;

import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class PartType {

    public Identifier getId() {
        return BreaRegistries.PART_TYPES.getKey(this);
    }

    public abstract boolean isMaterialValid(Material material);

    public abstract Map<Identifier, Double> getBaseValues(Map<Identifier, Double> materialValues);

    public List<PerkStack> getPerks(Map<Identifier, List<PerkStack>> materialPerks) {
        return materialPerks.getOrDefault(getPerkSourceId(), Collections.emptyList());
    }

    public abstract Identifier getPerkSourceId();
}
