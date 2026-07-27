package net.ptcrys.breakdown.api.event;

import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.entity.EntityEvent;

/**
 * 在实体加入世界后触发的初始化事件。
 *
 * <p>
 * 此事件转发自 {@link net.neoforged.neoforge.event.entity.EntityJoinLevelEvent}，
 * 在实体成功加入世界后以最低优先级发布。
 *
 * <p>
 * <b>使用场景：</b>
 * <ul>
 * <li>初始化实体的自定义事件监听器</li>
 * <li>从持久化存储恢复实体的附加数据</li>
 * <li>为实体注册首次加入世界时需要执行的逻辑</li>
 * </ul>
 *
 * <p>
 * <b>注意：</b>
 * <ul>
 * <li>此事件仅在服务端触发</li>
 * <li>应在此时初始化所有需要的事件消费器</li>
 * </ul>
 *
 */
public class GatherEntityDistributeEvent extends EntityEvent {

    public GatherEntityDistributeEvent(Entity entity) {
        super(entity);
    }
}
