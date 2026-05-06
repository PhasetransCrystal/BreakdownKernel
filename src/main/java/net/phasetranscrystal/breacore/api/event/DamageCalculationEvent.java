package net.phasetranscrystal.breacore.api.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.phasetranscrystal.breacore.api.damage.BreaDamageSource;

/**
 * 伤害分层计算事件。
 */
public abstract class DamageCalculationEvent extends EntityEvent {

    private final BreaDamageSource damageSource;

    private final double spellLayerFinalDamage;
    private final double physicalLayerFinalDamage;
    private final double hardArmorDurabilityLoss;
    private final double softArmorDurabilityLoss;
    private final double preCriticalFinalDamage;
    private final double criticalMultiplier;

    protected DamageCalculationEvent(
            LivingEntity victim,
            BreaDamageSource damageSource,
            double spellLayerFinalDamage,
            double physicalLayerFinalDamage,
            double hardArmorDurabilityLoss,
            double softArmorDurabilityLoss,
            double preCriticalFinalDamage,
            double criticalMultiplier
    ) {
        super(victim);
        this.damageSource = damageSource;
        this.spellLayerFinalDamage = Math.max(0.0, spellLayerFinalDamage);
        this.physicalLayerFinalDamage = Math.max(0.0, physicalLayerFinalDamage);
        this.hardArmorDurabilityLoss = Math.max(0.0, hardArmorDurabilityLoss);
        this.softArmorDurabilityLoss = Math.max(0.0, softArmorDurabilityLoss);
        this.preCriticalFinalDamage = Math.max(0.0, preCriticalFinalDamage);
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

    public double getSpellLayerFinalDamage() {
        return spellLayerFinalDamage;
    }

    public double getPhysicalLayerFinalDamage() {
        return physicalLayerFinalDamage;
    }

    public double getHardArmorDurabilityLoss() {
        return hardArmorDurabilityLoss;
    }

    public double getSoftArmorDurabilityLoss() {
        return softArmorDurabilityLoss;
    }

    public double getPreCriticalFinalDamage() {
        return preCriticalFinalDamage;
    }

    public double getCriticalMultiplier() {
        return criticalMultiplier;
    }

    /**
     * 初次计算后、应用前的可修改事件。
     */
    public static final class Pre extends DamageCalculationEvent {

        private double shieldHealthLoss;
        private double shieldDurabilityLoss;
        private double armorDurabilityLoss;
        private double finalDamage;
        private boolean critical;

        public Pre(
                LivingEntity victim,
                BreaDamageSource damageSource,
                double shieldHealthLoss,
                double shieldDurabilityLoss,
                double spellLayerFinalDamage,
                double physicalLayerFinalDamage,
                double hardArmorDurabilityLoss,
                double softArmorDurabilityLoss,
                double preCriticalFinalDamage,
                double criticalMultiplier,
                boolean critical,
                double finalDamage
        ) {
            super(
                    victim,
                    damageSource,
                    spellLayerFinalDamage,
                    physicalLayerFinalDamage,
                    hardArmorDurabilityLoss,
                    softArmorDurabilityLoss,
                    preCriticalFinalDamage,
                    criticalMultiplier
            );
            this.shieldHealthLoss = Math.max(0.0, shieldHealthLoss);
            this.shieldDurabilityLoss = Math.max(0.0, shieldDurabilityLoss);
            this.armorDurabilityLoss = Math.max(0.0, hardArmorDurabilityLoss + softArmorDurabilityLoss);
            this.critical = critical;
            this.finalDamage = Math.max(0.0, finalDamage);
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

        public void setCritical(boolean critical) {
            this.critical = critical;
        }
    }

    /**
     * 应用后的最终不可修改结果事件。
     */
    public static final class Post extends DamageCalculationEvent {

        private final double shieldHealthLoss;
        private final double shieldDurabilityLoss;
        private final double armorDurabilityLoss;
        private final double finalDamage;
        private final double appliedArmorDurabilityLoss;
        private final boolean critical;

        public Post(Pre pre, double appliedArmorDurabilityLoss) {
            super(
                    pre.getEntity(),
                    pre.getDamageSource(),
                    pre.getSpellLayerFinalDamage(),
                    pre.getPhysicalLayerFinalDamage(),
                    pre.getHardArmorDurabilityLoss(),
                    pre.getSoftArmorDurabilityLoss(),
                    pre.getPreCriticalFinalDamage(),
                    pre.getCriticalMultiplier()
            );
            this.shieldHealthLoss = Math.max(0.0, pre.getShieldHealthLoss());
            this.shieldDurabilityLoss = Math.max(0.0, pre.getShieldDurabilityLoss());
            this.armorDurabilityLoss = Math.max(0.0, pre.getArmorDurabilityLoss());
            this.finalDamage = Math.max(0.0, pre.getFinalDamage());
            this.appliedArmorDurabilityLoss = Math.max(0.0, appliedArmorDurabilityLoss);
            this.critical = pre.isCritical();
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
    }
}
