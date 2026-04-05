package net.phasetranscrystal.breacore.api.equipforge;

import net.phasetranscrystal.breacore.api.registry.BreaRegistries;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Map;

public abstract class ModificationType {

    public Identifier getId() {
        return BreaRegistries.MODIFICATION_TYPES.getKey(this);
    }

    public abstract PartType getApplicablePartType();

    public Map<Identifier, Double> getValueModifiers() {
        return Map.of();
    }

    public Map<Identifier, Integer> getMachineRequirements() {
        return Map.of();
    }

    public List<Ingredient> getRequiredItems() {
        return List.of();
    }
}
