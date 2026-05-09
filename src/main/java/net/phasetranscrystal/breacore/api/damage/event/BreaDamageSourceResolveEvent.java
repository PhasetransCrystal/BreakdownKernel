package net.phasetranscrystal.breacore.api.damage.event;

import lombok.Getter;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.phasetranscrystal.breacore.api.damage.BreaDamageSource;
import org.jetbrains.annotations.Nullable;

/**
 * 在本次伤害使用 {@link BreaDamageSource} 前触发，允许监听器替换最终伤害源。
 * <p>
 * 允许原始伤害源或当前解析结果为 {@code null}，用于外部系统在事件监听阶段自行构建。
 */
@Getter
public class BreaDamageSourceResolveEvent extends LivingEvent {

    private final @Nullable DamageSource originalSource;
    private @Nullable BreaDamageSource damageSource;

    public BreaDamageSourceResolveEvent(
            LivingEntity victim,
            @Nullable DamageSource originalSource,
            @Nullable BreaDamageSource damageSource
    ) {
        super(victim);
        this.originalSource = originalSource;
        this.damageSource = damageSource;
    }

    public void setDamageSource(@Nullable BreaDamageSource damageSource) {
        this.damageSource = damageSource;
    }
}
