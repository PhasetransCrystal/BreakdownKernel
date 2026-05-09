package net.phasetranscrystal.breacore.api.damage.event;

import lombok.Getter;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.phasetranscrystal.breacore.api.damage.BreaDamageSource;
import net.phasetranscrystal.breacore.api.damage.DamageArmorContext;

import java.util.Objects;

/**
 * 在本次伤害使用 {@link DamageArmorContext} 前触发，允许监听器替换上下文实现。
 */
@Getter
public class BreaDamageArmorContextEvent extends LivingEvent {

    private final BreaDamageSource damageSource;
    private DamageArmorContext armorContext;

    public BreaDamageArmorContextEvent(
            LivingEntity victim,
            BreaDamageSource damageSource,
            DamageArmorContext armorContext
    ) {
        super(victim);
        this.damageSource = Objects.requireNonNull(damageSource, "damageSource");
        this.armorContext = Objects.requireNonNull(armorContext, "armorContext");
    }

    public void setArmorContext(DamageArmorContext armorContext) {
        this.armorContext = Objects.requireNonNull(armorContext, "armorContext");
    }
}
