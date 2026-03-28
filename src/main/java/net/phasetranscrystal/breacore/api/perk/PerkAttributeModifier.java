package net.phasetranscrystal.breacore.api.perk;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record PerkAttributeModifier(
                                    Holder<Attribute> attribute,
                                    AttributeModifier.Operation operation,
                                    double value) {

    public AttributeModifier toModifier(net.minecraft.resources.Identifier id) {
        return new AttributeModifier(id, value, operation);
    }
}
