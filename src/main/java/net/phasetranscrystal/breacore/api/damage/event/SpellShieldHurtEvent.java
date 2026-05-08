package net.phasetranscrystal.breacore.api.damage.event;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.phasetranscrystal.breacore.api.damage.BreaDamageSource;

/**
 * 护盾耐久受损事件，可取消或修改损耗值。
 */
public class SpellShieldHurtEvent extends LivingEvent implements ICancellableEvent {

    private final BreaDamageSource damageSource;
    private final double originalDurabilityLoss;
    private double newDurabilityLoss;

    public SpellShieldHurtEvent(LivingEntity entity, BreaDamageSource damageSource, double durabilityLoss) {
        super(entity);
        this.damageSource = damageSource;
        this.originalDurabilityLoss = Math.max(0.0, durabilityLoss);
        this.newDurabilityLoss = this.originalDurabilityLoss;
    }

    public BreaDamageSource getDamageSource() {
        return damageSource;
    }

    public double getOriginalDurabilityLoss() {
        return originalDurabilityLoss;
    }

    public double getNewDurabilityLoss() {
        return newDurabilityLoss;
    }

    public void setNewDurabilityLoss(double newDurabilityLoss) {
        this.newDurabilityLoss = Math.max(0.0, newDurabilityLoss);
    }
}
