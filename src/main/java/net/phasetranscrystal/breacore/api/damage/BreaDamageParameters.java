package net.phasetranscrystal.breacore.api.damage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.phasetranscrystal.breacore.api.magic.Element;

/**
 * {@link BreaDamageSource} 的附加参数模型。
 */
public record BreaDamageParameters(
        Element element,
        int invulnerabilityTicks,
        double spellShieldHitRatio,
        double hardArmorPenetrationValue,
        double softArmorPenetrationValue,
        double hardArmorActionRatio,
        double softArmorActionRatio,
        double criticalChance,
        double criticalDamage
) {

    public static final BreaDamageParameters DEFAULT = new BreaDamageParameters(
            Element.NONE,
            0,
            1.0,
            Double.MAX_VALUE,
            Double.MAX_VALUE,
            1.0,
            1.0,
            0.0,
            0.0
    );

    public static final Codec<BreaDamageParameters> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Element.CODEC.optionalFieldOf("element", Element.NONE).forGetter(BreaDamageParameters::element),
            Codec.INT.optionalFieldOf("invulnerability_ticks", 0).forGetter(BreaDamageParameters::invulnerabilityTicks),
            Codec.DOUBLE.optionalFieldOf("spell_shield_hit_ratio", 1.0).forGetter(BreaDamageParameters::spellShieldHitRatio),
            Codec.DOUBLE.optionalFieldOf("hard_armor_penetration_value", Double.MAX_VALUE).forGetter(BreaDamageParameters::hardArmorPenetrationValue),
            Codec.DOUBLE.optionalFieldOf("soft_armor_penetration_value", Double.MAX_VALUE).forGetter(BreaDamageParameters::softArmorPenetrationValue),
            Codec.DOUBLE.optionalFieldOf("hard_armor_action_ratio", 1.0).forGetter(BreaDamageParameters::hardArmorActionRatio),
            Codec.DOUBLE.optionalFieldOf("soft_armor_action_ratio", 1.0).forGetter(BreaDamageParameters::softArmorActionRatio),
            Codec.DOUBLE.optionalFieldOf("critical_chance", 0.0).forGetter(BreaDamageParameters::criticalChance),
            Codec.DOUBLE.optionalFieldOf("critical_damage", 0.0).forGetter(BreaDamageParameters::criticalDamage)
    ).apply(instance, BreaDamageParameters::new));

    public BreaDamageParameters {
        element = element == null ? Element.NONE : element;
        invulnerabilityTicks = Math.max(0, invulnerabilityTicks);
        spellShieldHitRatio = clamp01(spellShieldHitRatio);
        hardArmorPenetrationValue = clampPenetrationValue(hardArmorPenetrationValue);
        softArmorPenetrationValue = clampPenetrationValue(softArmorPenetrationValue);
        hardArmorActionRatio = clamp01(hardArmorActionRatio);
        softArmorActionRatio = clamp01(softArmorActionRatio);
        criticalChance = clamp01(criticalChance);
        criticalDamage = Math.max(0.0, criticalDamage);
    }

    private static double clamp01(double value) {
        return Math.clamp(value, 0.0, 1.0);
    }

    private static double clampPenetrationValue(double value) {
        if (value == Double.MAX_VALUE) {
            return Double.MAX_VALUE;
        }
        return Math.max(0.0, value);
    }
}
