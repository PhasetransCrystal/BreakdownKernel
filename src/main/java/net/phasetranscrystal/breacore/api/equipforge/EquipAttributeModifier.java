package net.phasetranscrystal.breacore.api.equipforge;

import net.phasetranscrystal.breacore.api.attribute.DetailedAttributeModifier;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

public record EquipAttributeModifier(Identifier id, EquipmentSlotGroup group, DetailedAttributeModifier root) {

    public Holder<Attribute> getAttribute() {
        return root.attribute();
    }

    public AttributeModifier toModifier() {
        return root.toModifier(id);
    }

    public void put(ItemAttributeModifierEvent event) {
        event.addModifier(getAttribute(), toModifier(), group);
    }
}
