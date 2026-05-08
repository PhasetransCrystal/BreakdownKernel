package net.phasetranscrystal.breacore.api.damage;

import net.neoforged.neoforge.common.NeoForge;
import net.minecraft.world.entity.LivingEntity;
import net.phasetranscrystal.breacore.api.damage.event.DamageCalculationEvent;
import net.phasetranscrystal.breacore.api.magic.Element;

/**
 * 分层伤害计算器。
 *
 * <p>执行顺序：法术护盾层 -> 硬甲层 -> 软甲层 -> Pre 事件 -> 应用结果 -> Post 事件。</p>
 */
public final class DamageCalculator {

    private static final double RICOCHET_RATIO_THRESHOLD = 0.5;
    private static final double PENETRATION_RATIO_THRESHOLD = 1.0;

    /**
     * 执行完整伤害结算并应用到受击实体。
     *
     * @param damageSource 伤害源扩展参数
     * @param armorContext 护甲/护盾上下文
     * @param rawDamage 原始伤害值
     * @return 最终不可变结果事件
     */
    public static DamageCalculationEvent.Post calculateAndApply(
            BreaDamageSource damageSource,
            DamageArmorContext armorContext,
            double rawDamage
    ) {
        DamageCalculationEvent.Pre preEvent = prepareForVanillaApply(damageSource, armorContext, rawDamage);
        try {
            boolean damageApplied = armorContext.applyFinalDamage(damageSource, preEvent.getFinalDamage());
            return finalizeAfterHurt(damageSource, armorContext, preEvent, damageApplied);
        } finally {
            DamageRuntimeContext.clearCalculation(armorContext.getVictim(), damageSource);
        }
    }

    public static DamageCalculationEvent.Pre calculatePre(
            BreaDamageSource damageSource,
            DamageArmorContext armorContext,
            double rawDamage
    ) {
        double sourceDamage = Math.max(0.0, rawDamage);
        double spellRawDamage = sourceDamage * damageSource.getSpellShieldHitRatio();
        double physicalRawDamage = sourceDamage - spellRawDamage;

        double resistance = 0.0;
        if (damageSource.getElement() != Element.NONE) {
            resistance = armorContext.getElementResistance(damageSource.getElement());
        }

        double spellWeightedDamage = spellRawDamage * (1.0 - clamp01(resistance));
        boolean critical = physicalRawDamage > 0.0
                && armorContext.getVictim().getRandom().nextDouble() < clamp01(damageSource.getCriticalChance());
        double criticalMultiplier = critical ?  Math.max(
                1.0,
                1.0 + damageSource.getCriticalDamage() - armorContext.getCriticalDamageReduction()
        ) : 1.0;
        double physicalWeightedDamage = physicalRawDamage * criticalMultiplier;
        double weightedDamage = spellWeightedDamage + physicalWeightedDamage;

        double shieldHealth = Math.max(0.0, armorContext.getSpellShieldHealth());
        double shieldHealthLoss = Math.min(shieldHealth, spellWeightedDamage);
        double spellFinalDamage = Math.max(0.0, spellWeightedDamage - shieldHealth);
        double shieldDurabilityLoss = shieldHealthLoss * (1.0 - clamp01(armorContext.getSpellShieldSturdiness()));

        ArmorPenetrationOutcome hardOutcome = evaluatePenetration(
                damageSource.getHardArmorPenetrationValue(),
                armorContext.getHardArmorValue()
        );
        double hardPenetrationComparisonRatio = penetrationComparisonRatio(
                damageSource.getHardArmorPenetrationValue(),
                armorContext.getHardArmorValue()
        );
        double hardArmorActionRatio = adjustedActionRatio(damageSource.getHardArmorActionRatio(), hardOutcome);
        double hardArmorDurabilityLoss = hardArmorActionRatio <= 0.0 ? 0.0 : physicalWeightedDamage * hardArmorActionRatio;
        double hardPenetrationRatio = penetrationRatioFor(hardOutcome, hardPenetrationComparisonRatio);
        double remainingAfterHard = physicalWeightedDamage * hardPenetrationRatio;
        double hardArmorAbsorbedDamage = Math.max(0.0, physicalWeightedDamage - remainingAfterHard);

        ArmorPenetrationOutcome softOutcome = evaluatePenetration(
                damageSource.getSoftArmorPenetrationValue(),
                armorContext.getSoftArmorValue()
        );
        double softPenetrationComparisonRatio = penetrationComparisonRatio(
                damageSource.getSoftArmorPenetrationValue(),
                armorContext.getSoftArmorValue()
        );
        double softArmorActionRatio = adjustedActionRatio(damageSource.getSoftArmorActionRatio(), softOutcome);
        double softArmorDurabilityLoss = softArmorActionRatio <= 0.0 ? 0.0 : remainingAfterHard * softArmorActionRatio;
        double softPenetrationRatio = penetrationRatioFor(softOutcome, softPenetrationComparisonRatio);
        double physicalLayerFinalDamage = remainingAfterHard * softPenetrationRatio;
        double softArmorAbsorbedDamage = Math.max(0.0, remainingAfterHard - physicalLayerFinalDamage);

        double finalDamage = spellFinalDamage + physicalLayerFinalDamage;

        double totalArmorAbsorbedDamage = hardArmorAbsorbedDamage + softArmorAbsorbedDamage;
        double totalAbsorbedDamage = shieldHealthLoss + totalArmorAbsorbedDamage;

        DamageCalculationEvent.Pre preEvent = new DamageCalculationEvent.Pre(
                armorContext.getVictim(),
                damageSource,
                sourceDamage,
                weightedDamage,
                spellRawDamage,
                spellWeightedDamage,
                shieldHealthLoss,
                spellFinalDamage,
                physicalRawDamage,
                physicalWeightedDamage,
                hardArmorAbsorbedDamage,
                softArmorAbsorbedDamage,
                physicalLayerFinalDamage,
                totalAbsorbedDamage,
                finalDamage,
                shieldHealthLoss,
                shieldDurabilityLoss,
                hardArmorDurabilityLoss,
                softArmorDurabilityLoss,
                criticalMultiplier,
                critical
        );

        NeoForge.EVENT_BUS.post(preEvent);
        return preEvent;
    }

    public static DamageCalculationEvent.Post finalizeAfterHurt(
            BreaDamageSource damageSource,
            DamageArmorContext armorContext,
            DamageCalculationEvent.Pre preEvent,
            boolean damageApplied
    ) {
        double appliedArmorDurabilityLoss = DamageRuntimeContext.pullAppliedArmorDurability(armorContext.getVictim(), damageSource);
        if (!damageApplied) {
            preEvent.setShieldHealthLoss(0.0);
            preEvent.setShieldDurabilityLoss(0.0);
            preEvent.setArmorDurabilityLoss(0.0);
            preEvent.setCritical(false);
            preEvent.setFinalDamage(0.0);
            appliedArmorDurabilityLoss = 0.0;
        }

        DamageCalculationEvent.Post postEvent = new DamageCalculationEvent.Post(preEvent, appliedArmorDurabilityLoss, damageApplied);
        NeoForge.EVENT_BUS.post(postEvent);
        return postEvent;
    }

    public static DamageCalculationEvent.Pre prepareForVanillaApply(
            BreaDamageSource damageSource,
            DamageArmorContext armorContext,
            double rawDamage
    ) {
        DamageCalculationEvent.Pre preEvent = calculatePre(damageSource, armorContext, rawDamage);
        DamageRuntimeContext.pushCalculation(
                armorContext.getVictim(),
                damageSource,
                armorContext,
                preEvent,
                preEvent.getArmorDurabilityLoss(),
                preEvent.getHardArmorAbsorbedDamage() + preEvent.getSoftArmorAbsorbedDamage(),
                preEvent.getSpellAbsorbedByShield()
        );
        return preEvent;
    }

    public static DamageCalculationEvent.Post finalizePendingForVanillaApply(
            LivingEntity victim,
            BreaDamageSource source,
            boolean damageApplied
    ) {
        DamageRuntimeContext.RuntimeEntry pending = DamageRuntimeContext.peekCalculation(victim, source);
        if (pending == null) {
            return null;
        }
        return finalizeAfterHurt(source, pending.getArmorContext(), pending.getPreEvent(), damageApplied);
    }

    /**
     * {@link #calculateAndApply(BreaDamageSource, DamageArmorContext, double)} 的 float 便捷重载。
     */
    public static DamageCalculationEvent.Post calculateAndApply(
            BreaDamageSource damageSource,
            DamageArmorContext armorContext,
            float rawDamage
    ) {
        return calculateAndApply(damageSource, armorContext, (double) rawDamage);
    }

    /**
     * 根据穿甲值与装甲值判定穿透结果。
     */
    public static ArmorPenetrationOutcome evaluatePenetration(double penetrationValue, double armorValue) {
        if (penetrationValue == Double.MAX_VALUE) {
            return ArmorPenetrationOutcome.PENETRATED;
        }
        if (penetrationValue <= 0.0) {
            return ArmorPenetrationOutcome.RICOCHET;
        }
        if (armorValue <= 0.0) {
            return ArmorPenetrationOutcome.PENETRATED;
        }

        double ratio = penetrationComparisonRatio(penetrationValue, armorValue);
        if (ratio < RICOCHET_RATIO_THRESHOLD) {
            return ArmorPenetrationOutcome.RICOCHET;
        }
        if (ratio < PENETRATION_RATIO_THRESHOLD) {
            return ArmorPenetrationOutcome.NOT_PENETRATED;
        }
        return ArmorPenetrationOutcome.PENETRATED;
    }

    /**
     * 将穿透结果映射为伤害通过比。
     */
    private static double penetrationRatioFor(ArmorPenetrationOutcome outcome, double penetrationComparisonRatio) {
        return switch (outcome) {
            case RICOCHET -> 0.0;
            case NOT_PENETRATED -> notPenetrationDamageFactor(penetrationComparisonRatio);
            case PENETRATED -> 1.0;
        };
    }

    /**
     * 计算有效护甲作用比。
     *
     * <p>输入值会被限制在 [0,1]；若结果为跳弹，则再乘 0.1。</p>
     */
    private static double adjustedActionRatio(double baseActionRatio, ArmorPenetrationOutcome outcome) {
        double ratio = clamp01(baseActionRatio);
        if (ratio <= 0.0) {
            return 0.0;
        }
        if (outcome == ArmorPenetrationOutcome.RICOCHET) {
            ratio *= 0.1;
        }
        return ratio;
    }

    /**
     * 未穿透时的伤害通过比（占位实现）。
     *
     * <p>TODO: 用正式公式替换当前占位实现。</p>
     */
    private static double notPenetrationDamageFactor(double penetrationComparisonRatio) {
        return Math.clamp(penetrationComparisonRatio, 0.0, 1.0);
    }

    /**
     * 计算穿甲/装甲比较比值。
     */
    private static double penetrationComparisonRatio(double penetrationValue, double armorValue) {
        if (penetrationValue == Double.MAX_VALUE || armorValue <= 0.0) {
            return Double.POSITIVE_INFINITY;
        }
        if (penetrationValue <= 0.0) {
            return 0.0;
        }
        return penetrationValue / armorValue;
    }

    /**
     * 将数值限制在 [0,1]。
     */
    private static double clamp01(double value) {
        return Math.clamp(value, 0.0, 1.0);
    }
}
