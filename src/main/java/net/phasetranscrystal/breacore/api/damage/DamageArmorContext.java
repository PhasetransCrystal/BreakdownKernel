package net.phasetranscrystal.breacore.api.damage;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.phasetranscrystal.breacore.api.event.SpellShieldHurtEvent;
import net.phasetranscrystal.breacore.api.magic.Element;
import net.phasetranscrystal.breacore.mixins.LivingEntityAccessor;

/**
 * 伤害结算所需的受击方护甲上下文。
 */
public interface DamageArmorContext {

    Entity getRootAttacker();

    Entity getDirectAttacker();

    LivingEntity getVictim();

    ItemStack getWeapon();

    double getSpellShieldHealth();

    void setSpellShieldHealth(double health);

    double getSpellShieldDurability();

    void setSpellShieldDurability(double durability);

    double getSpellShieldSturdiness();

    double getHardArmorValue();

    double getSoftArmorValue();

    /**
     * 返回暴击伤害减免值。
     */
    default double getCriticalDamageReduction() {
        // TODO 对接防御实体 attribute：暴击伤害减免。
        return 0.0;
    }

    /**
     * 返回元素抗性值，范围建议为 [0,1]。
     */
    double getElementResistance(Element element);

    default void applySpellShieldLoss(BreaDamageSource damageSource, double shieldHealthLoss, double shieldDurabilityLoss) {
        double next = Math.max(0.0, getSpellShieldHealth() - Math.max(0.0, shieldHealthLoss));
        setSpellShieldHealth(next);

        SpellShieldHurtEvent event = NeoForge.EVENT_BUS.post(new SpellShieldHurtEvent(getVictim(), damageSource, shieldDurabilityLoss));
        if (!event.isCanceled()) {
            double nextDurability = Math.max(0.0, getSpellShieldDurability() - Math.max(0.0, event.getNewDurabilityLoss()));
            setSpellShieldDurability(nextDurability);
        }
    }

    /**
     * 将总护甲耐久损耗应用到实体装备。
     */
    double applyArmorDurabilityLoss(BreaDamageSource damageSource, double armorDurabilityLoss);

    /**
     * 将最终伤害直接应用到实体，不重复进入 hurtServer 计算流程。
     */
    default boolean applyFinalDamage(BreaDamageSource damageSource, double finalDamage) {
        if (finalDamage <= 0.0) {
            return false;
        }

        LivingEntity victim = getVictim();
        if (!(victim.level() instanceof ServerLevel serverLevel)) {
            return false;
        }

        float before = victim.getHealth();
        ((LivingEntityAccessor) victim).breacore$invokeActuallyHurt(serverLevel, damageSource, (float) finalDamage);
        boolean hurtApplied = victim.getHealth() < before || victim.isDeadOrDying();
        if (hurtApplied) {
            victim.invulnerableTime = damageSource.getInvulnerabilityTicks();
        }
        return hurtApplied;
    }
}
