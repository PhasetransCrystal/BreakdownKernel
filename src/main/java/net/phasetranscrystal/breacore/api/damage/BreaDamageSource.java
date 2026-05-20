package net.phasetranscrystal.breacore.api.damage;

import net.phasetranscrystal.breacore.api.magic.Element;
import net.phasetranscrystal.breacore.common.registry.AttributeRegistry;
import net.phasetranscrystal.breacore.utils.AttributeHelper;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.phys.Vec3;

import com.tterrag.registrate.util.entry.RegistryEntry;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

/**
 * 带有 breacore 伤害计算附加参数的 {@link DamageSource}。
 */
public class BreaDamageSource extends DamageSource {

    @Getter
    private final BreaDamageParameters parameters;
    private boolean hasCriticalDecision;
    @Getter
    private boolean criticalResolved;
    @Getter
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
                            double criticalDamage) {
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
                        criticalDamage));
    }

    public BreaDamageSource(
                            Holder<DamageType> type,
                            @Nullable Entity directEntity,
                            @Nullable Entity causingEntity,
                            @Nullable Vec3 damageSourcePosition,
                            BreaDamageParameters parameters) {
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

    public double getSpellDamageAmplification() {
        Element element = getElement();
        if (element == null || element == Element.NONE) {
            return 0.0;
        }

        RegistryEntry<Attribute, Attribute> attribute = AttributeRegistry.getSpellDamageAmplificationAttribute(element);
        LivingEntity attacker = resolveAttacker();
        return AttributeHelper.getValueOrDefault(attacker, attribute);
    }

    public boolean hasCriticalDecision() {
        return hasCriticalDecision;
    }

    public void setCriticalDecision(boolean criticalResolved, double criticalBonusMultiplier) {
        this.hasCriticalDecision = true;
        this.criticalResolved = criticalResolved;
        this.criticalBonusMultiplier = Math.max(0.0, criticalBonusMultiplier);
    }

    private LivingEntity resolveAttacker() {
        if (getEntity() instanceof LivingEntity causing) {
            return causing;
        }
        if (getDirectEntity() instanceof LivingEntity direct) {
            return direct;
        }
        return null;
    }
}
