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

import java.util.List;
import java.util.Optional;

public record PerkDisplayInfo(
                              Identifier perkId,
                              String perkNameKey,
                              String perkExplainKey,
                              List<PerkAttributeModifierDisplay> attributeModifiers,
                              List<EventDisplayInfo> eventDisplays) {

    public static final Codec<PerkDisplayInfo> CODEC = RecordCodecBuilder.create(consumer -> consumer.group(
            Identifier.CODEC.fieldOf("perkId").forGetter(PerkDisplayInfo::perkId),
            Codec.STRING.fieldOf("perkNameKey").forGetter(PerkDisplayInfo::perkNameKey),
            Codec.STRING.fieldOf("perkExplainKey").forGetter(PerkDisplayInfo::perkExplainKey),
            PerkAttributeModifierDisplay.CODEC.listOf().fieldOf("attributeModifiers").forGetter(PerkDisplayInfo::attributeModifiers),
            EventDisplayInfo.CODEC.listOf().fieldOf("eventDisplays").forGetter(PerkDisplayInfo::eventDisplays)).apply(consumer, PerkDisplayInfo::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PerkDisplayInfo> STREAM_CODEC = StreamCodec.of(
            (buf, info) -> {
                Identifier.STREAM_CODEC.encode(buf, info.perkId);
                ByteBufCodecs.STRING_UTF8.encode(buf, info.perkNameKey);
                ByteBufCodecs.STRING_UTF8.encode(buf, info.perkExplainKey());
                ByteBufCodecs.fromCodec(PerkAttributeModifierDisplay.CODEC.listOf()).encode(buf, info.attributeModifiers);
                ByteBufCodecs.fromCodec(EventDisplayInfo.CODEC.listOf()).encode(buf, info.eventDisplays);
            },
            buf -> new PerkDisplayInfo(
                    Identifier.STREAM_CODEC.decode(buf),
                    ByteBufCodecs.STRING_UTF8.decode(buf),
                    ByteBufCodecs.STRING_UTF8.decode(buf),
                    ByteBufCodecs.fromCodec(PerkAttributeModifierDisplay.CODEC.listOf()).decode(buf),
                    ByteBufCodecs.fromCodec(EventDisplayInfo.CODEC.listOf()).decode(buf)));

    public record PerkAttributeModifierDisplay(
                                               Holder<Attribute> attribute,
                                               List<ModifierEntry> modifiers) {

        public static final Codec<PerkAttributeModifierDisplay> CODEC = RecordCodecBuilder.create(consumer -> consumer.group(
                Attribute.CODEC.fieldOf("attribute").forGetter(PerkAttributeModifierDisplay::attribute),
                ModifierEntry.CODEC.listOf().fieldOf("modifiers").forGetter(PerkAttributeModifierDisplay::modifiers)).apply(consumer, PerkAttributeModifierDisplay::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, PerkAttributeModifierDisplay> STREAM_CODEC = StreamCodec.of(
                (buf, display) -> {
                    ByteBufCodecs.holderRegistry(Registries.ATTRIBUTE).encode(buf, display.attribute);
                    ByteBufCodecs.fromCodec(ModifierEntry.CODEC.listOf()).encode(buf, display.modifiers);
                },
                buf -> new PerkAttributeModifierDisplay(
                        ByteBufCodecs.holderRegistry(Registries.ATTRIBUTE).decode(buf),
                        ByteBufCodecs.fromCodec(ModifierEntry.CODEC.listOf()).decode(buf)));
    }

    public record ModifierEntry(
                                String valueFormat,
                                Optional<String> extraInfoKey) {

        private static int getOperationOrder(AttributeModifier.Operation operation) {
            return switch (operation) {
                case ADD_VALUE -> 0;
                case ADD_MULTIPLIED_BASE -> 1;
                case ADD_MULTIPLIED_TOTAL -> 2;
            };
        }

        public static final Codec<ModifierEntry> CODEC = RecordCodecBuilder.create(consumer -> consumer.group(
                Codec.STRING.fieldOf("valueFormat").forGetter(ModifierEntry::valueFormat),
                Codec.STRING.optionalFieldOf("extraInfoKey").forGetter(ModifierEntry::extraInfoKey)).apply(consumer, ModifierEntry::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ModifierEntry> STREAM_CODEC = StreamCodec.of(
                (buf, entry) -> {
                    ByteBufCodecs.STRING_UTF8.encode(buf, entry.valueFormat);
                    ByteBufCodecs.STRING_UTF8.encode(buf, entry.extraInfoKey.orElse(""));
                },
                buf -> new ModifierEntry(
                        ByteBufCodecs.STRING_UTF8.decode(buf),
                        Optional.of(ByteBufCodecs.STRING_UTF8.decode(buf))));
    }

    public record EventDisplayInfo(
                                   String eventNameKey,
                                   String valueFormat,
                                   String unit,
                                   Optional<String> extraInfoKey) {

        public static final Codec<EventDisplayInfo> CODEC = RecordCodecBuilder.create(consumer -> consumer.group(
                Codec.STRING.fieldOf("eventNameKey").forGetter(EventDisplayInfo::eventNameKey),
                Codec.STRING.fieldOf("valueFormat").forGetter(EventDisplayInfo::valueFormat),
                Codec.STRING.fieldOf("unit").forGetter(EventDisplayInfo::unit),
                Codec.STRING.optionalFieldOf("extraInfoKey").forGetter(EventDisplayInfo::extraInfoKey)).apply(consumer, EventDisplayInfo::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, EventDisplayInfo> STREAM_CODEC = StreamCodec.of(
                (buf, display) -> {
                    ByteBufCodecs.STRING_UTF8.encode(buf, display.eventNameKey);
                    ByteBufCodecs.STRING_UTF8.encode(buf, display.valueFormat);
                    ByteBufCodecs.STRING_UTF8.encode(buf, display.unit);
                    ByteBufCodecs.STRING_UTF8.encode(buf, display.extraInfoKey.orElse(""));
                },
                buf -> new EventDisplayInfo(
                        ByteBufCodecs.STRING_UTF8.decode(buf),
                        ByteBufCodecs.STRING_UTF8.decode(buf),
                        ByteBufCodecs.STRING_UTF8.decode(buf),
                        Optional.of(ByteBufCodecs.STRING_UTF8.decode(buf))));
    }
}
