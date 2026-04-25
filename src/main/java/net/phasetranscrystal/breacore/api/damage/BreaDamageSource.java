package net.phasetranscrystal.breacore.api.damage;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.phasetranscrystal.breacore.api.magic.Element;
import org.jetbrains.annotations.Nullable;

/**
 * 带有 breacore 伤害计算附加参数的 {@link DamageSource}。
 */
public class BreaDamageSource extends DamageSource {

    private final Element element;
    private final int invulnerabilityTicks;

    private final double spellShieldHitRatio;
    private final double hardArmorPenetrationValue;
    private final double softArmorPenetrationValue;
    private final double hardArmorActionRatio;
    private final double softArmorActionRatio;

    public BreaDamageSource(
            Holder<DamageType> type,
            @Nullable Entity directEntity,
            @Nullable Entity causingEntity,
            @Nullable Vec3 damageSourcePosition,
            Element element,
            int invulnerabilityTicks,
            double spellShieldHitRatio,
            double hardArmorPenetrationValue,
            double softArmorPenetrationValue,
            double hardArmorActionRatio,
            double softArmorActionRatio
    ) {
        super(type, directEntity, causingEntity, damageSourcePosition);
        this.element = element;
        this.invulnerabilityTicks = Math.max(0, invulnerabilityTicks);
        this.spellShieldHitRatio = clamp01(spellShieldHitRatio);
        this.hardArmorPenetrationValue = clampPenetrationValue(hardArmorPenetrationValue);
        this.softArmorPenetrationValue = clampPenetrationValue(softArmorPenetrationValue);
        this.hardArmorActionRatio = clamp01(hardArmorActionRatio);
        this.softArmorActionRatio = clamp01(softArmorActionRatio);
    }

    public BreaDamageSource(
            Holder<DamageType> type,
            @Nullable Entity directEntity,
            @Nullable Entity causingEntity,
            Element element,
            int invulnerabilityTicks,
            double spellShieldHitRatio,
            double hardArmorPenetrationValue,
            double softArmorPenetrationValue,
            double hardArmorActionRatio,
            double softArmorActionRatio
    ) {
        this(
                type,
                directEntity,
                causingEntity,
                null,
                element,
                invulnerabilityTicks,
                spellShieldHitRatio,
                hardArmorPenetrationValue,
                softArmorPenetrationValue,
                hardArmorActionRatio,
                softArmorActionRatio
        );
    }

    public BreaDamageSource(
            Holder<DamageType> type,
            @Nullable Entity directEntity,
            @Nullable Entity causingEntity,
            @Nullable Vec3 damageSourcePosition,
            Element element,
            int invulnerabilityTicks,
            double spellShieldHitRatio
    ) {
        this(
                type,
                directEntity,
                causingEntity,
                damageSourcePosition,
                element,
                invulnerabilityTicks,
                spellShieldHitRatio,
                Double.MAX_VALUE,
                Double.MAX_VALUE,
                1.0,
                1.0
        );
    }

    public BreaDamageSource(
            Holder<DamageType> type,
            @Nullable Entity directEntity,
            @Nullable Entity causingEntity,
            Element element,
            int invulnerabilityTicks,
            double spellShieldHitRatio
    ) {
        this(
                type,
                directEntity,
                causingEntity,
                null,
                element,
                invulnerabilityTicks,
                spellShieldHitRatio
        );
    }

    public BreaDamageSource(
            Holder<DamageType> type,
            Element element,
            int invulnerabilityTicks,
            double spellShieldHitRatio,
            double hardArmorPenetrationValue,
            double softArmorPenetrationValue,
            double hardArmorActionRatio,
            double softArmorActionRatio
    ) {
        this(
                type,
                null,
                null,
                null,
                element,
                invulnerabilityTicks,
                spellShieldHitRatio,
                hardArmorPenetrationValue,
                softArmorPenetrationValue,
                hardArmorActionRatio,
                softArmorActionRatio
        );
    }

    public BreaDamageSource(
            Holder<DamageType> type,
            Element element,
            int invulnerabilityTicks,
            double spellShieldHitRatio
    ) {
        this(
                type,
                null,
                null,
                null,
                element,
                invulnerabilityTicks,
                spellShieldHitRatio,
                Double.MAX_VALUE,
                Double.MAX_VALUE,
                1.0,
                1.0
        );
    }

    public Element getElement() {
        return element;
    }

    public int getInvulnerabilityTicks() {
        return invulnerabilityTicks;
    }

    public double getSpellShieldHitRatio() {
        return spellShieldHitRatio;
    }

    public double getHardArmorPenetrationValue() {
        return hardArmorPenetrationValue;
    }

    public double getSoftArmorPenetrationValue() {
        return softArmorPenetrationValue;
    }

    public double getHardArmorActionRatio() {
        return hardArmorActionRatio;
    }

    public double getSoftArmorActionRatio() {
        return softArmorActionRatio;
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
