package net.phasetranscrystal.breacore.api.perk;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

import javax.annotation.Nonnull;

public record PerkAttributeModifier(
                                    Holder<Attribute> attribute,
                                    AttributeModifier.Operation operation,
                                    double value,
                                    @Nonnull String valueFormat, // shouldn't be empty
                                    Optional<String> extraInfoKey) {

    public static final Codec<PerkAttributeModifier> CODEC = RecordCodecBuilder.create(consumer -> consumer.group(
            Attribute.CODEC.fieldOf("attribute").forGetter(PerkAttributeModifier::attribute),
            AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(PerkAttributeModifier::operation),
            Codec.DOUBLE.fieldOf("value").forGetter(PerkAttributeModifier::value),
            Codec.STRING.fieldOf("valueFormat").forGetter(PerkAttributeModifier::valueFormat),
            Codec.STRING.optionalFieldOf("extraInfoKey").forGetter(PerkAttributeModifier::extraInfoKey)).apply(consumer, PerkAttributeModifier::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PerkAttributeModifier> STREAM_CODEC = StreamCodec.of(
            (buf, modifier) -> {
                ByteBufCodecs.holderRegistry(Registries.ATTRIBUTE).encode(buf, modifier.attribute);
                ByteBufCodecs.fromCodec(AttributeModifier.Operation.CODEC).encode(buf, modifier.operation);
                ByteBufCodecs.DOUBLE.encode(buf, modifier.value);
                ByteBufCodecs.STRING_UTF8.encode(buf, modifier.valueFormat);
                ByteBufCodecs.STRING_UTF8.encode(buf, modifier.extraInfoKey.orElse(""));
            },
            buf -> new PerkAttributeModifier(
                    ByteBufCodecs.holderRegistry(Registries.ATTRIBUTE).decode(buf),
                    ByteBufCodecs.fromCodec(AttributeModifier.Operation.CODEC).decode(buf),
                    ByteBufCodecs.DOUBLE.decode(buf),
                    ByteBufCodecs.STRING_UTF8.decode(buf),
                    Optional.of(ByteBufCodecs.STRING_UTF8.decode(buf))));

    @Deprecated
    // test only. use the method below.
    public PerkAttributeModifier(Holder<Attribute> attribute, AttributeModifier.Operation operation, double value) {
        this(attribute, operation, value, "", Optional.empty());
    }

    public PerkAttributeModifier(Holder<Attribute> attribute, AttributeModifier.Operation operation, double value, String valueFormat) {
        this(attribute, operation, value, valueFormat, Optional.empty());
    }

    public AttributeModifier toModifier(Identifier id) {
        return new AttributeModifier(id, value, operation);
    }

    public String getDisplayFormula() {
        return valueFormat;
    }
}
