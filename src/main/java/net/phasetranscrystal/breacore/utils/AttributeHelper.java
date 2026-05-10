package net.phasetranscrystal.breacore.utils;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public final class AttributeHelper {

    private AttributeHelper() {
    }

    public static double getValueOrDefault(Entity entity, Holder<Attribute> attributeHolder) {
        if (attributeHolder == null) {
            return 0.0;
        }
        if (!(entity instanceof LivingEntity livingEntity)) {
            return attributeHolder.value().getDefaultValue();
        }

        AttributeInstance instance = livingEntity.getAttribute(attributeHolder);
        return instance == null ? attributeHolder.value().getDefaultValue() : instance.getValue();
    }
}
