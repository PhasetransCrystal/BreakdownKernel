package net.phasetranscrystal.breacore.api.event;

import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

/**
 * 在实体对目标造成伤害时触发的适配事件。
 *
 * <p>
 * 此事件转发自伤害序列相关事件，用于将伤害事件适配到实体事件分发系统。
 *
 * <p>
 * <b>事件转发链：</b>
 * <ul>
 * <li>{@link Income} - 来自 {@link LivingIncomingDamageEvent}，可取消</li>
 * <li>{@link Pre} - 来自 {@link LivingDamageEvent.Pre}，伤害计算前</li>
 * <li>{@link Post} - 来自 {@link LivingDamageEvent.Post}，伤害计算后</li>
 * </ul>
 *
 * <p>
 * <b>中间实体标记：</b>
 * 当实体并非直接造成伤害的来源时（如弹射物），{@link EntityAttackEvent#isIntermediateEntity} 为 true。
 *
 * @see EntityKillEvent 实体击杀事件
 */
public abstract class EntityAttackEvent extends EntityEvent {

    /**
     * 标识主体实体是否为伤害链中的中间实体。
     *
     * <p>
     * 当实体并非直接造成伤害的来源时为 {@code true}。
     * 例如：弓箭射出后击中目标，弓箭实体为中间实体，射出弓箭的玩家为直接来源。
     */
    public final boolean isIntermediateEntity;

    protected EntityAttackEvent(Entity entity, boolean isIntermediateEntity) {
        super(entity);
        this.isIntermediateEntity = isIntermediateEntity;
    }

    /**
     * 伤害收入事件。
     *
     * <p>
     * 转发自 {@link LivingIncomingDamageEvent}，事件链中最早的可取消点。
     * 可用于拦截或修改即将造成的伤害。
     *
     * @see LivingIncomingDamageEvent 原始事件
     */
    public static class Income extends EntityAttackEvent implements net.neoforged.bus.api.ICancellableEvent {

        /**
         * 原始的 {@link LivingIncomingDamageEvent}。
         */
        public final LivingIncomingDamageEvent origin;

        public Income(Entity entity, LivingIncomingDamageEvent event, boolean isIntermediateEntity) {
            super(entity, isIntermediateEntity);
            this.origin = event;
        }
    }

    /**
     * 伤害计算前事件。
     *
     * <p>
     * 转发自 {@link LivingDamageEvent.Pre}，在伤害值计算之前触发。
     * 可用于在最终伤害计算前执行额外逻辑。
     *
     * @see LivingDamageEvent.Pre 原始事件
     */
    public static class Pre extends EntityAttackEvent {

        /**
         * 原始的 {@link LivingDamageEvent.Pre}。
         */
        public final LivingDamageEvent.Pre origin;

        public Pre(Entity entity, LivingDamageEvent.Pre event, boolean isIntermediateEntity) {
            super(entity, isIntermediateEntity);
            this.origin = event;
        }
    }

    /**
     * 伤害计算后事件。
     *
     * <p>
     * 转发自 {@link LivingDamageEvent.Post}，在伤害值计算之后触发。
     * 可用于在伤害应用后执行逻辑，如显示伤害数字、触发音效等。
     *
     * @see LivingDamageEvent.Post 原始事件
     */
    public static class Post extends EntityAttackEvent {

        /**
         * 原始的 {@link LivingDamageEvent.Post}。
         */
        public final LivingDamageEvent.Post origin;

        public Post(Entity entity, LivingDamageEvent.Post event, boolean isIntermediateEntity) {
            super(entity, isIntermediateEntity);
            this.origin = event;
        }
    }
}
