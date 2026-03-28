package net.phasetranscrystal.breacore.api.eventdispatch;

import net.phasetranscrystal.breacore.common.registry.AttachmentTypeRegistry;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.EntityEvent;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

/**
 * 事件分发器静态工具类。
 *
 * <p>
 * 封装对 {@link EventDistributor} 的常见操作，提供简洁的 API。
 *
 * @see EventDistributor 实体事件分发器
 * @see EventConsumer 事件消费器
 */
public final class EventDispatcher {

    public static final AttachmentType<EventDistributor> TYPE = AttachmentTypeRegistry.EVENT_DISTRIBUTOR.get();

    /**
     * 获取实体的 {@link EventDistributor}，若不存在则返回 null。
     *
     * @param entity 实体
     * @return EventDistributor 或 null
     */
    public static EventDistributor getExist(Entity entity) {
        if (entity == null) {
            return null;
        }
        return entity.getExistingDataOrNull(TYPE);
    }

    /**
     * 获取实体的 {@link EventDistributor}。
     *
     * <p>
     * 如果实体没有此附件，则创建并附加。
     *
     * @param entity 实体
     * @return EventDistributor
     * @throws NullPointerException 如果实体为 null 或 ATTACHMENT_TYPE 未初始化
     */
    public static EventDistributor get(Entity entity) {
        if (entity == null) {
            throw new NullPointerException("Entity cannot be null");
        }
        return entity.getData(TYPE);
    }

    /**
     * 附加消费器到实体。
     *
     * <p>
     * 便捷方法，自动获取或创建 EventDistributor。
     *
     * @param entity   实体
     * @param consumer 消费器
     */
    public static <T extends Event> void attach(Entity entity, EventConsumer<T> consumer) {
        get(entity).attachConsumer(consumer);
    }

    /**
     * 附加消费器到实体的便捷方法。
     *
     * <p>
     * 自动创建消费器实例并附加到实体。
     *
     * @param entity           实体
     * @param eventType        事件类型
     * @param runWhenCancelled 事件取消时是否执行
     * @param handler          处理逻辑
     * @param path             路径数组
     */
    public static <T extends Event> void attach(
                                                Entity entity,
                                                Class<T> eventType,
                                                boolean runWhenCancelled,
                                                BiConsumer<T, EventConsumer<T>> handler,
                                                Identifier... path) {
        get(entity).attachConsumer(eventType, runWhenCancelled, handler, path);
    }

    /**
     * 从实体身上移除指定消费器。
     *
     * @param entity   实体
     * @param consumer 消费器
     * @return 是否成功移除
     */
    public static <T extends Event> boolean detach(Entity entity, EventConsumer<T> consumer) {
        EventDistributor distributor = getExist(entity);
        return distributor != null && distributor.detachConsumer(consumer);
    }

    /**
     * 精确路径移除。
     *
     * @param entity 实体
     * @param path   路径数组
     * @return 被移除的消费器列表
     */
    public static List<EventConsumer<?>> detachPath(Entity entity, Identifier... path) {
        EventDistributor distributor = getExist(entity);
        return distributor != null ? distributor.detachPath(path) : List.of();
    }

    /**
     * 子树移除。
     *
     * @param entity 实体
     * @param path   路径数组
     * @return 被移除的消费器列表
     */
    public static List<EventConsumer<?>> detachSubtree(Entity entity, Identifier... path) {
        EventDistributor distributor = getExist(entity);
        return distributor != null ? distributor.detachSubtree(path) : List.of();
    }

    /**
     * 移除所有消费器。
     *
     * @param entity 实体
     */
    public static void detachAll(Entity entity) {
        EventDistributor distributor = getExist(entity);
        if (distributor != null) {
            distributor.detachAll();
        }
    }

    /**
     * 检查实体是否有指定类型的消费器。
     *
     * @param entity    实体
     * @param eventType 事件类型
     * @return 是否有消费器
     */
    public static boolean hasConsumers(Entity entity, Class<? extends Event> eventType) {
        EventDistributor distributor = getExist(entity);
        return distributor != null && distributor.hasConsumers(eventType);
    }

    /**
     * 获取实体指定类型的所有消费器。
     *
     * @param entity    实体
     * @param eventType 事件类型
     * @return 消费器列表
     */
    public static <T extends Event> List<EventConsumer<T>> getConsumers(Entity entity, Class<T> eventType) {
        EventDistributor distributor = getExist(entity);
        return distributor != null ? distributor.getConsumers(eventType) : List.of();
    }

    /**
     * 分发事件到实体。
     *
     * <p>
     * 直接调用实体的 EventDistributor.post()。
     *
     * @param entity 实体
     * @param event  事件
     * @return 是否有消费器被执行
     */
    public static <T extends Event> boolean dispatch(Entity entity, T event) {
        EventDistributor distributor = getExist(entity);
        return distributor != null && distributor.post(event);
    }

    /**
     * 从实体自身获取并分发事件。
     *
     * <p>
     * 适用于 {@link EntityEvent} 子类。
     *
     * @param event EntityEvent 实例
     * @return 是否有消费器被执行
     */
    public static <T extends EntityEvent> boolean dispatchSelf(T event) {
        return dispatch(event.getEntity(), event);
    }

    /**
     * 从实体自身获取并分发事件。
     *
     * <p>
     * 适用于 {@link IEntityAboutEvent} 的实现事件。
     *
     * @param event EntityEvent 实例
     * @return 是否有消费器被执行
     */
    public static <T extends Event & IEntityAboutEvent> boolean dispatchSelfCustom(T event) {
        AtomicBoolean b = new AtomicBoolean(false);
        event.getEntities().forEach(entity -> b.set(b.get() | dispatch(entity, event)));
        return b.get();
    }
}
