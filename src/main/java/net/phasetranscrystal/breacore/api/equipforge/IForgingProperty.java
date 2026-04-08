package net.phasetranscrystal.breacore.api.equipforge;

import net.phasetranscrystal.breacore.api.material.attributes.AttributeType;
import net.phasetranscrystal.breacore.api.material.attributes.MaterialAttribute;
import net.phasetranscrystal.breacore.api.perk.PerkStack;

import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface IForgingProperty extends MaterialAttribute {

    Map<Identifier, Double> getBaseValues();

    Map<Identifier, List<PerkStack>> getPerks();

    @Override
    default Set<AttributeType<?>> getRequiredTypes() {
        return Set.of();
    }

    @Override
    default <T extends MaterialAttribute> Optional<T> createDependency(AttributeType<T> type) {
        return Optional.empty();
    }
}
