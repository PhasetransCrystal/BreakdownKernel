package net.phasetranscrystal.breacore.api.eventdispatch;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.Event;

import java.util.function.BiConsumer;

/**
 * 实体事件消费器。
 *
 * <p>
 * 用于处理附加在实体上的事件分发逻辑。
 * 提供从 lambda 函数快速创建实例的工厂方法。
 *
 * @param eventType        处理的事件类型
 * @param path             路径标识，用于树结构批量操作
 * @param runWhenCancelled 事件被取消时是否仍执行
 * @param handler          事件处理逻辑，参数为事件实例和自身引用
 */
public record EventConsumer<T extends Event>(
                                             Class<T> eventType,
                                             Identifier[] path,
                                             boolean runWhenCancelled,
                                             BiConsumer<T, EventConsumer<T>> handler) {

    /**
     * 从 lambda 函数快速创建消费器实例（无路径）。
     *
     * <p>
     * 创建的消费器将附加到根路径。
     *
     * @param eventType        事件类型
     * @param runWhenCancelled 是否在事件取消时执行
     * @param handler          事件处理逻辑：{@code (event, consumer) -> {...}}
     * @return 新的消费器实例
     */
    public static <T extends Event> EventConsumer<T> of(
                                                        Class<T> eventType,
                                                        boolean runWhenCancelled,
                                                        BiConsumer<T, EventConsumer<T>> handler) {
        return new EventConsumer<>(eventType, new Identifier[0], runWhenCancelled, handler);
    }

    /**
     * 从 lambda 函数快速创建消费器实例（带路径）。
     *
     * @param eventType        事件类型
     * @param path             路径数组
     * @param runWhenCancelled 是否在事件取消时执行
     * @param handler          事件处理逻辑：{@code (event, consumer) -> {...}}
     * @return 新的消费器实例
     */
    public static <T extends Event> EventConsumer<T> of(
                                                        Class<T> eventType,
                                                        Identifier[] path,
                                                        boolean runWhenCancelled,
                                                        BiConsumer<T, EventConsumer<T>> handler) {
        return new EventConsumer<>(eventType, path, runWhenCancelled, handler);
    }

    /**
     * 从实体身上移除此消费器。
     *
     * <p>
     * 会同时从树结构和按类型索引中移除。
     *
     * @param entity 目标实体
     */
    public void removeFrom(Entity entity) {
        EventDispatcher.detach(entity, this);
    }

    /**
     * 执行此消费器。
     *
     * <p>
     * 调用存储的 handler 并传入事件实例和自身引用。
     *
     * @param event 事件实例
     */
    public void accept(T event) {
        handler.accept(event, this);
    }

    /**
     * 创建新的消费器，使用指定路径。
     *
     * @param newPath 新的路径数组
     * @return 新的消费器实例
     */
    public EventConsumer<T> withPath(Identifier[] newPath) {
        return new EventConsumer<>(eventType, newPath, runWhenCancelled, handler);
    }
}
