package net.phasetranscrystal.breacore.api.perk;

import net.phasetranscrystal.brealib.BreaLib;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
public class Perk {

    private final Identifier id;
    private final PerkStackingType stackingType;

    public Perk(Identifier id, PerkStackingType stackingType) {
        this.id = id;
        this.stackingType = stackingType;
    }

    public void onAttached(LivingEntity entity, PerkInfo info) {}

    public void onDetached(LivingEntity entity, PerkInfo info) {}

    public void onLevelChanged(LivingEntity entity, float oldLevel, float newLevel, PerkInfo info) {}

    public List<PerkConsumer<?>> getEventConsumers(PerkInfo info) {
        return List.of();
    }

    public Collection<PerkAttributeModifier> getAttributeModifiers(LivingEntity entity, float level) {
        return List.of();
    }

    public Identifier getAttributeModifierId(AttributeModifier.Operation operation) {
        String suffix = switch (operation) {
            case ADD_VALUE -> "/stage1";
            case ADD_MULTIPLIED_BASE -> "/stage2";
            case ADD_MULTIPLIED_TOTAL -> "/stage3";
        };
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
}
