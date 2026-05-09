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

    private final BreaDamageParameters parameters;
    private boolean hasCriticalDecision;
    private boolean criticalResolved;
    private double criticalBonusMultiplier;

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
            double softArmorActionRatio,
            double criticalChance,
            double criticalDamage
    ) {
        this(
                type,
                directEntity,
                causingEntity,
                damageSourcePosition,
                new BreaDamageParameters(
                        element,
                        invulnerabilityTicks,
                        spellShieldHitRatio,
                        hardArmorPenetrationValue,
                        softArmorPenetrationValue,
                        hardArmorActionRatio,
                        softArmorActionRatio,
                        criticalChance,
                        criticalDamage
                )
        );
    }

    public BreaDamageSource(
            Holder<DamageType> type,
            @Nullable Entity directEntity,
            @Nullable Entity causingEntity,
            @Nullable Vec3 damageSourcePosition,
            BreaDamageParameters parameters
    ) {
        super(type, directEntity, causingEntity, damageSourcePosition);
        this.parameters = parameters == null ? BreaDamageParameters.DEFAULT : parameters;
        this.hasCriticalDecision = false;
        this.criticalResolved = false;
        this.criticalBonusMultiplier = 0.0;
    }


    public Element getElement() {
        return parameters.element();
    }

    public int getInvulnerabilityTicks() {
        return parameters.invulnerabilityTicks();
    }

    public double getSpellShieldHitRatio() {
        return parameters.spellShieldHitRatio();
    }

    public double getHardArmorPenetrationValue() {
        return parameters.hardArmorPenetrationValue();
    }

    public double getSoftArmorPenetrationValue() {
        return parameters.softArmorPenetrationValue();
    }

    public double getHardArmorActionRatio() {
        return parameters.hardArmorActionRatio();
    }

    public double getSoftArmorActionRatio() {
        return parameters.softArmorActionRatio();
    }

    public double getCriticalChance() {
        return parameters.criticalChance();
    }

    public double getCriticalDamage() {
        return parameters.criticalDamage();
    }

    public boolean isCriticalResolved() {
        return criticalResolved;
    }

    public boolean hasCriticalDecision() {
        return hasCriticalDecision;
    }

    public double getCriticalBonusMultiplier() {
        return criticalBonusMultiplier;
    }

    public void setCriticalDecision(boolean criticalResolved, double criticalBonusMultiplier) {
        this.hasCriticalDecision = true;
        this.criticalResolved = criticalResolved;
        this.criticalBonusMultiplier = Math.max(0.0, criticalBonusMultiplier);
    }

    public BreaDamageParameters getParameters() {
        return parameters;
    }
}
