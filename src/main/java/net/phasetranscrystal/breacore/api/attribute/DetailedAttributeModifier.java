package net.phasetranscrystal.breacore.api.attribute;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

import javax.annotation.Nonnull;

public record DetailedAttributeModifier(
                                        Holder<Attribute> attribute,
                                        AttributeModifier.Operation operation,
                                        double value,
                                        @Nonnull String valueFormat,
                                        Optional<String> extraInfoKey) {

    public static final Codec<DetailedAttributeModifier> CODEC = RecordCodecBuilder.create(consumer -> consumer.group(
            Attribute.CODEC.fieldOf("attribute").forGetter(DetailedAttributeModifier::attribute),
            AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(DetailedAttributeModifier::operation),
            Codec.DOUBLE.fieldOf("value").forGetter(DetailedAttributeModifier::value),
            Codec.STRING.fieldOf("valueFormat").forGetter(DetailedAttributeModifier::valueFormat),
            Codec.STRING.optionalFieldOf("extraInfoKey").forGetter(DetailedAttributeModifier::extraInfoKey)).apply(consumer, DetailedAttributeModifier::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DetailedAttributeModifier> STREAM_CODEC = StreamCodec.of(
            (buf, modifier) -> {
                ByteBufCodecs.holderRegistry(Registries.ATTRIBUTE).encode(buf, modifier.attribute);
                ByteBufCodecs.fromCodec(AttributeModifier.Operation.CODEC).encode(buf, modifier.operation);
                ByteBufCodecs.DOUBLE.encode(buf, modifier.value);
                ByteBufCodecs.STRING_UTF8.encode(buf, modifier.valueFormat);
                ByteBufCodecs.STRING_UTF8.encode(buf, modifier.extraInfoKey.orElse(""));
            },
            buf -> new DetailedAttributeModifier(
                    ByteBufCodecs.holderRegistry(Registries.ATTRIBUTE).decode(buf),
                    ByteBufCodecs.fromCodec(AttributeModifier.Operation.CODEC).decode(buf),
                    ByteBufCodecs.DOUBLE.decode(buf),
                    ByteBufCodecs.STRING_UTF8.decode(buf),
                    Optional.of(ByteBufCodecs.STRING_UTF8.decode(buf))));

    @Deprecated
    public DetailedAttributeModifier(Holder<Attribute> attribute, AttributeModifier.Operation operation, double value) {
        this(attribute, operation, value, "", Optional.empty());
    }

    public DetailedAttributeModifier(Holder<Attribute> attribute, AttributeModifier.Operation operation, double value, String valueFormat) {
        this(attribute, operation, value, valueFormat, Optional.empty());
    }

    public AttributeModifier toModifier(Identifier id) {
        return new AttributeModifier(id, value, operation);
    }

    public String getDisplayFormula() {
        return valueFormat;
    }

    public void applyToEntity(LivingEntity entity, Identifier attrId, boolean isTransient) {
        AttributeInstance instance = entity.getAttribute(attribute());
        if (instance != null) {
            if (isTransient) {
                instance.addOrUpdateTransientModifier(toModifier(attrId));
            } else {
                instance.addOrReplacePermanentModifier(toModifier(attrId));
            }
        }
    }

    public ItemAttributeModifiers.Entry toItemModifier(EquipmentSlotGroup slotGroup, Identifier id) {
        return toItemModifier(slotGroup, id, null);
    }

    public ItemAttributeModifiers.Entry toItemModifier(EquipmentSlotGroup slotGroup, Identifier id, ItemAttributeModifiers.Display display) {
        return new ItemAttributeModifiers.Entry(attribute, toModifier(id), slotGroup, display);
    }
}
