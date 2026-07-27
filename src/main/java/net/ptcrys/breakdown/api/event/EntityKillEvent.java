package net.ptcrys.breakdown.api.event;

import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

/**
 * 在实体杀死目标时触发的适配事件。
 *
 * <p>
 * 此事件转发自 {@link LivingDeathEvent}，分别在最高和最低优先级触发。
 *
 * <p>
 * <b>事件转发链：</b>
 * <ul>
 * <li>{@link Pre} - 最高优先级，可取消，用于在死亡处理前执行逻辑</li>
 * <li>{@link Post} - 最低优先级，不可取消，用于在死亡处理后执行逻辑</li>
 * </ul>
 *
 * <p>
 * <b>使用场景：</b>
 * <ul>
 * <li>{@link Pre} - 掉落物品修改、成就检查、击杀统计</li>
 * <li>{@link Post} - 清理关联数据、发送通知</li>
 * </ul>
 *
 * @see EntityAttackEvent 实体攻击事件
 */
public abstract class EntityKillEvent extends EntityEvent {

    /**
     * 原始的 {@link LivingDeathEvent}。
     */
    public final LivingDeathEvent origin;

    /**
     * 标识主体实体是否为击杀链中的中间实体。
     *
     * <p>
     * 当实体并非直接造成击杀的来源时为 {@code true}。
     * 例如：召唤生物击杀目标，召唤者可能被视为中间实体。
     */
    public final boolean isIntermediateEntity;

    protected EntityKillEvent(Entity entity, LivingDeathEvent origin, boolean isIntermediateEntity) {
        super(entity);
        this.origin = origin;
        this.isIntermediateEntity = isIntermediateEntity;
    }

    /**
     * 击杀前事件。
     *
     * <p>
     * 在死亡处理之前触发，可取消。
     * 取消此事件不会阻止死亡，仅阻止后续逻辑执行。
     *
     * @see LivingDeathEvent 原始事件
     */
    public static class Pre extends EntityKillEvent implements net.neoforged.bus.api.ICancellableEvent {

        public Pre(Entity entity, LivingDeathEvent origin, boolean isIntermediateEntity) {
            super(entity, origin, isIntermediateEntity);
        }
    }

    /**
     * 击杀后事件。
     *
     * <p>
     * 在死亡处理之后触发，不可取消。
     * 用于在目标已经死亡后执行清理或通知逻辑。
     *
     * @see LivingDeathEvent 原始事件
     */
    public static class Post extends EntityKillEvent {

        public Post(Entity entity, LivingDeathEvent origin, boolean isIntermediateEntity) {
            super(entity, origin, isIntermediateEntity);
        }
    }
}
