package net.phasetranscrystal.breacore.api.damage.event;

import lombok.Setter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.phasetranscrystal.breacore.api.damage.BreaDamageSource;

/**
 * 伤害分层计算事件。
 */
public abstract class DamageCalculationEvent extends EntityEvent {

    /** 本次结算使用的扩展伤害源。 */
    private final BreaDamageSource damageSource;

    /** 原始输入伤害（未拆分、未加权）。 */
    private final double rawDamage;
    /** 总加权伤害（法术加权 + 物理加权）。 */
    private final double weightedDamage;
    /** 法术部分原始伤害（按法术命中率拆分后）。 */
    private final double spellRawDamage;
    /** 法术部分加权伤害（元素抗性处理后，护盾处理前）。 */
    private final double spellWeightedDamage;
    /** 法术伤害被护盾吸收的量。 */
    private final double spellAbsorbedByShield;
    /** 法术最终伤害（法术加权后扣除护盾吸收）。 */
    private final double spellFinalDamage;
    /** 物理部分原始伤害（按法术命中率拆分后）。 */
    private final double physicalRawDamage;
    /** 物理部分加权伤害（当前语义：已应用暴击倍率，护甲处理前）。 */
    private final double physicalWeightedDamage;
    /** 硬甲吸收的物理伤害。 */
    private final double hardArmorAbsorbedDamage;
    /** 软甲吸收的物理伤害。 */
    private final double softArmorAbsorbedDamage;
    /** 物理最终伤害（经过软/硬甲处理后）。 */
    private final double physicalFinalDamage;
    /** 总吸收伤害（护盾吸收 + 软硬甲吸收）。 */
    private final double totalAbsorbedDamage;
    /** 总最终伤害（法术穿透后 + 物理最终）。 */
    private final double totalFinalDamage;

    /** 硬甲耐久损耗。 */
    private final double hardArmorDurabilityLoss;
    /** 软甲耐久损耗。 */
    private final double softArmorDurabilityLoss;
    /** 暴击倍率（最小为 1）。 */
    private final double criticalMultiplier;

    protected DamageCalculationEvent(
            LivingEntity victim,
            BreaDamageSource damageSource,
            double rawDamage,
            double weightedDamage,
            double spellRawDamage,
            double spellWeightedDamage,
            double spellAbsorbedByShield,
            double spellFinalDamage,
            double physicalRawDamage,
            double physicalWeightedDamage,
            double hardArmorAbsorbedDamage,
            double softArmorAbsorbedDamage,
            double physicalFinalDamage,
            double totalAbsorbedDamage,
            double totalFinalDamage,
            double hardArmorDurabilityLoss,
            double softArmorDurabilityLoss,
            double criticalMultiplier
    ) {
        super(victim);
        this.damageSource = damageSource;
        this.rawDamage = Math.max(0.0, rawDamage);
        this.weightedDamage = Math.max(0.0, weightedDamage);
        this.spellRawDamage = Math.max(0.0, spellRawDamage);
        this.spellWeightedDamage = Math.max(0.0, spellWeightedDamage);
        this.spellAbsorbedByShield = Math.max(0.0, spellAbsorbedByShield);
        this.spellFinalDamage = Math.max(0.0, spellFinalDamage);
        this.physicalRawDamage = Math.max(0.0, physicalRawDamage);
        this.physicalWeightedDamage = Math.max(0.0, physicalWeightedDamage);
        this.hardArmorAbsorbedDamage = Math.max(0.0, hardArmorAbsorbedDamage);
        this.softArmorAbsorbedDamage = Math.max(0.0, softArmorAbsorbedDamage);
        this.physicalFinalDamage = Math.max(0.0, physicalFinalDamage);
        this.totalAbsorbedDamage = Math.max(0.0, totalAbsorbedDamage);
        this.totalFinalDamage = Math.max(0.0, totalFinalDamage);
        this.hardArmorDurabilityLoss = Math.max(0.0, hardArmorDurabilityLoss);
        this.softArmorDurabilityLoss = Math.max(0.0, softArmorDurabilityLoss);
        this.criticalMultiplier = Math.max(1.0, criticalMultiplier);
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) super.getEntity();
    }

    public Entity getRootAttacker() {
        return damageSource.getEntity();
    }

    public Entity getDirectAttacker() {
        return damageSource.getDirectEntity();
    }

    public ItemStack getWeapon() {
        Entity rootAttacker = getRootAttacker();
        if (rootAttacker instanceof LivingEntity livingAttacker) {
            return livingAttacker.getMainHandItem();
        }
        return ItemStack.EMPTY;
    }

    public BreaDamageSource getDamageSource() {
        return damageSource;
    }

    /**
     * 是否为真实伤害（命中原版 BYPASSES_ARMOR 标签）。
     */
    public boolean isTrueDamage() {
        return damageSource != null && damageSource.is(DamageTypeTags.BYPASSES_ARMOR);
    }

    public double getRawDamage() {
        return rawDamage;
    }

    public double getWeightedDamage() {
        return weightedDamage;
    }

    public double getSpellRawDamage() {
        return spellRawDamage;
    }

    public double getSpellWeightedDamage() {
        return spellWeightedDamage;
    }

    public double getSpellAbsorbedByShield() {
        return spellAbsorbedByShield;
    }

    public double getSpellFinalDamage() {
        return spellFinalDamage;
    }

    public double getPhysicalRawDamage() {
        return physicalRawDamage;
    }

    public double getPhysicalWeightedDamage() {
        return physicalWeightedDamage;
    }

    public double getHardArmorAbsorbedDamage() {
        return hardArmorAbsorbedDamage;
    }

    public double getSoftArmorAbsorbedDamage() {
        return softArmorAbsorbedDamage;
    }

    public double getPhysicalFinalDamage() {
        return physicalFinalDamage;
    }

    public double getTotalAbsorbedDamage() {
        return totalAbsorbedDamage;
    }

    public double getTotalFinalDamage() {
        return totalFinalDamage;
    }

    public double getHardArmorDurabilityLoss() {
        return hardArmorDurabilityLoss;
    }

    public double getSoftArmorDurabilityLoss() {
        return softArmorDurabilityLoss;
    }

    public double getCriticalMultiplier() {
        return criticalMultiplier;
    }

    /**
     * 初次计算后、应用前的可修改事件。
     */
    public static final class Pre extends DamageCalculationEvent {

        /** 本次命中实际扣除的护盾生命。 */
        private double shieldHealthLoss;
        /** 本次命中实际扣除的护盾耐久。 */
        private double shieldDurabilityLoss;
        /** 本次命中计划应用的总护甲耐久损耗（硬甲 + 软甲）。 */
        private double armorDurabilityLoss;
        /** 可在 Pre 阶段被监听器调整的最终伤害。 */
        private double finalDamage;
        @Setter
        /** 是否触发暴击（可在 Pre 阶段被监听器改写）。 */
        private boolean critical;

        public Pre(
                LivingEntity victim,
                BreaDamageSource damageSource,
                double rawDamage,
                double weightedDamage,
                double spellRawDamage,
                double spellWeightedDamage,
                double spellAbsorbedByShield,
                double spellFinalDamage,
                double physicalRawDamage,
                double physicalWeightedDamage,
                double hardArmorAbsorbedDamage,
                double softArmorAbsorbedDamage,
                double physicalFinalDamage,
                double totalAbsorbedDamage,
                double totalFinalDamage,
                double shieldHealthLoss,
                double shieldDurabilityLoss,
                double hardArmorDurabilityLoss,
                double softArmorDurabilityLoss,
                double criticalMultiplier,
                boolean critical
        ) {
            super(
                    victim,
                    damageSource,
                    rawDamage,
                    weightedDamage,
                    spellRawDamage,
                    spellWeightedDamage,
                    spellAbsorbedByShield,
                    spellFinalDamage,
                    physicalRawDamage,
                    physicalWeightedDamage,
                    hardArmorAbsorbedDamage,
                    softArmorAbsorbedDamage,
                    physicalFinalDamage,
                    totalAbsorbedDamage,
                    totalFinalDamage,
                    hardArmorDurabilityLoss,
                    softArmorDurabilityLoss,
                    criticalMultiplier
            );
            this.shieldHealthLoss = Math.max(0.0, shieldHealthLoss);
            this.shieldDurabilityLoss = Math.max(0.0, shieldDurabilityLoss);
            this.armorDurabilityLoss = Math.max(0.0, hardArmorDurabilityLoss + softArmorDurabilityLoss);
            this.critical = critical;
            this.finalDamage = Math.max(0.0, totalFinalDamage);
        }

        public double getShieldHealthLoss() {
            return shieldHealthLoss;
        }

        public void setShieldHealthLoss(double shieldHealthLoss) {
            this.shieldHealthLoss = Math.max(0.0, shieldHealthLoss);
        }

        public double getShieldDurabilityLoss() {
            return shieldDurabilityLoss;
        }

        public void setShieldDurabilityLoss(double shieldDurabilityLoss) {
            this.shieldDurabilityLoss = Math.max(0.0, shieldDurabilityLoss);
        }

        public double getArmorDurabilityLoss() {
            return armorDurabilityLoss;
        }

        public void setArmorDurabilityLoss(double armorDurabilityLoss) {
            this.armorDurabilityLoss = Math.max(0.0, armorDurabilityLoss);
        }

        public double getFinalDamage() {
            return finalDamage;
        }

        public void setFinalDamage(double finalDamage) {
            this.finalDamage = Math.max(0.0, finalDamage);
        }

        public boolean isCritical() {
            return critical;
        }

    }

    /**
     * 应用后的最终不可修改结果事件。
     */
    public static final class Post extends DamageCalculationEvent {

        /** 应用后最终确认的护盾生命损耗。 */
        private final double shieldHealthLoss;
        /** 应用后最终确认的护盾耐久损耗。 */
        private final double shieldDurabilityLoss;
        /** 应用后最终确认的护甲耐久损耗（计划值）。 */
        private final double armorDurabilityLoss;
        /** 应用后最终确认的伤害值。 */
        private final double finalDamage;
        /** 实际执行到原版 hurtArmor 的耐久损耗值。 */
        private final double appliedArmorDurabilityLoss;
        /** 应用后最终确认的暴击标记。 */
        private final boolean critical;
        /** 本次伤害是否实际生效（是否掉血/死亡）。 */
        private final boolean damageApplied;

        public Post(Pre pre, double appliedArmorDurabilityLoss, boolean damageApplied) {
            super(
                    pre.getEntity(),
                    pre.getDamageSource(),
                    pre.getRawDamage(),
                    pre.getWeightedDamage(),
                    pre.getSpellRawDamage(),
                    pre.getSpellWeightedDamage(),
                    pre.getSpellAbsorbedByShield(),
                    pre.getSpellFinalDamage(),
                    pre.getPhysicalRawDamage(),
                    pre.getPhysicalWeightedDamage(),
                    pre.getHardArmorAbsorbedDamage(),
                    pre.getSoftArmorAbsorbedDamage(),
                    pre.getPhysicalFinalDamage(),
                    pre.getTotalAbsorbedDamage(),
                    pre.getTotalFinalDamage(),
                    pre.getHardArmorDurabilityLoss(),
                    pre.getSoftArmorDurabilityLoss(),
                    pre.getCriticalMultiplier()
            );
            this.shieldHealthLoss = Math.max(0.0, pre.getShieldHealthLoss());
            this.shieldDurabilityLoss = Math.max(0.0, pre.getShieldDurabilityLoss());
            this.armorDurabilityLoss = Math.max(0.0, pre.getArmorDurabilityLoss());
            this.finalDamage = Math.max(0.0, pre.getFinalDamage());
            this.appliedArmorDurabilityLoss = Math.max(0.0, appliedArmorDurabilityLoss);
            this.critical = pre.isCritical();
            this.damageApplied = damageApplied;
        }

        public double getShieldHealthLoss() {
            return shieldHealthLoss;
        }

        public double getShieldDurabilityLoss() {
            return shieldDurabilityLoss;
        }

        public double getArmorDurabilityLoss() {
            return armorDurabilityLoss;
        }

        public double getFinalDamage() {
            return finalDamage;
        }

        public double getAppliedArmorDurabilityLoss() {
            return appliedArmorDurabilityLoss;
        }

        public boolean isCritical() {
            return critical;
        }

        public boolean isDamageApplied() {
            return damageApplied;
        }
    }
}
