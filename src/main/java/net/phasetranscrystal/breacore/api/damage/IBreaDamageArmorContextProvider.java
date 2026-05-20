package net.phasetranscrystal.breacore.api.damage;

import net.minecraft.world.entity.LivingEntity;

/**
 * 可选接口：由实体实现以提供自定义的 {@link DamageArmorContext}。
 */
public interface IBreaDamageArmorContextProvider {

    /**
     * @param damageSource 已转换后的 Brea 伤害源
     * @param victim       受击实体（通常为 this）
     * @return 自定义上下文；返回 {@code null} 时将回退到默认构建流程
     */
    DamageArmorContext provideDamageArmorContext(BreaDamageSource damageSource, LivingEntity victim);
}
