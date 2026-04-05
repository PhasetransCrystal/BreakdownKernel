package net.phasetranscrystal.breacore.api.perk;

import net.phasetranscrystal.brealib.BreaLib;

import net.phasetranscrystal.breacore.api.attribute.DetailedAttributeModifier;
import net.phasetranscrystal.breacore.api.registry.BreaRegistries;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Perk {

    private static final String KEY_PREFIX = "breacore.quench.perk.";

    @Getter
    private final PerkStackingType stackingType;

    public Perk(PerkStackingType stackingType) {
        this.stackingType = stackingType;
    }

    public Identifier getId() {
        return BreaRegistries.PERKS.getKey(this);
    }

    public String getNameKey() {
        Identifier id = getId();
        return KEY_PREFIX + id.getNamespace() + "." + id.getPath() + ".name";
    }

    public String getExplainKey() {
        Identifier id = getId();
        return KEY_PREFIX + id.getNamespace() + "." + id.getPath() + ".explain";
    }

    public void onAttached(LivingEntity entity, PerkInfo info) {}

    public void onDetached(LivingEntity entity, PerkInfo info) {}

    public void onLevelChanged(LivingEntity entity, float oldLevel, float newLevel, PerkInfo info) {}

    public List<PerkConsumer<?>> getEventConsumers(PerkInfo info) {
        return List.of();
    }

    public Collection<DetailedAttributeModifier> getAttributeModifiers(LivingEntity entity, float level) {
        return getAttributeModifiers(entity, level, false);
    }

    public Collection<DetailedAttributeModifier> getAttributeModifiers(LivingEntity entity, float level, boolean isDisplayMode) {
        return List.of();
    }

    public Identifier getAttributeModifierId(AttributeModifier.Operation operation) {
        String suffix = switch (operation) {
            case ADD_VALUE -> "/stage1";
            case ADD_MULTIPLIED_BASE -> "/stage2";
            case ADD_MULTIPLIED_TOTAL -> "/stage3";
        };
        Identifier id = getId();
        return BreaLib.id("perk_system/" + id.getNamespace() + "/" + id.getPath() + suffix);
    }

    public float calculateLevel(List<Float> levels) {
        if (levels == null || levels.isEmpty()) {
            return 0f;
        }

        return switch (stackingType) {
            case MAX -> levels.stream().max(Float::compare).orElse(0f);
            case SUM -> levels.stream().reduce(0f, Float::sum);
            case MIN -> levels.stream().min(Float::compare).orElse(0f);
            case AVERAGE -> (float) levels.stream().mapToDouble(d -> d).average().orElse(0f);
        };
    }

    // TODO DOING TEST
    public PerkDisplayInfo getDisplayInfo() {
        Collection<DetailedAttributeModifier> modifiers = getAttributeModifiers(null, 0f, true);
        List<PerkDisplayInfo.PerkAttributeModifierDisplay> modifierDisplays = groupModifiersByAttribute(modifiers);

        List<PerkDisplayInfo.EventDisplayInfo> eventDisplays = getEventDisplayInfo();

        return new PerkDisplayInfo(
                getId(),
                getNameKey(),
                getExplainKey(),
                modifierDisplays,
                eventDisplays);
    }

    private List<PerkDisplayInfo.PerkAttributeModifierDisplay> groupModifiersByAttribute(Collection<DetailedAttributeModifier> modifiers) {
        return modifiers.stream()
                .collect(Collectors.groupingBy(DetailedAttributeModifier::attribute))
                .entrySet().stream()
                .map(entry -> toModifierDisplay(entry.getKey(), entry.getValue()))
                .toList();
    }

    protected PerkDisplayInfo.PerkAttributeModifierDisplay toModifierDisplay(Holder<Attribute> attribute, List<DetailedAttributeModifier> modifiers) {
        List<PerkDisplayInfo.ModifierEntry> entries = modifiers.stream()
                .sorted((a, b) -> Integer.compare(getOperationOrder(a.operation()), getOperationOrder(b.operation())))
                .map(m -> new PerkDisplayInfo.ModifierEntry(
                        m.getDisplayFormula(),
                        m.extraInfoKey()))
                .toList();

        return new PerkDisplayInfo.PerkAttributeModifierDisplay(
                attribute,
                entries);
    }

    private int getOperationOrder(AttributeModifier.Operation operation) {
        return switch (operation) {
            case ADD_VALUE -> 0;
            case ADD_MULTIPLIED_BASE -> 1;
            case ADD_MULTIPLIED_TOTAL -> 2;
        };
    }

    protected List<PerkDisplayInfo.EventDisplayInfo> getEventDisplayInfo() {
        return List.of();
    }
}
