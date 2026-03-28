package net.phasetranscrystal.breacore.api.eventdispatch;

import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * 实体事件分发器附件。
 *
 * <p>存储在实体上，管理该实体的所有事件消费器。
 *
 * <p><b>数据结构：</b>
 * <ul>
 *   <li>树结构 ({@link EventTree})：用于路径批量操作</li>
 *   <li>类型索引 ({@link Map})：用于按事件类型快速查找</li>
 *   <li>去重缓存：防止同一事件实例被重复处理</li>
 * </ul>
 *
 * @see EventConsumer 事件消费器
 * @see EventTree 树结构
 */
public class EventDistributor {

    private final EventTree<EventConsumer<?>> tree = new EventTree<>();
    private final Map<Class<? extends Event>, List<EventConsumer<?>>> byType = new HashMap<>();
    private final Map<Class<? extends Event>, Integer> recentHashcodes = new HashMap<>();

    /**
     * 分发事件给所有已注册的消费器。
     *
     * <p>按附加顺序执行匹配事件类型的消费器。
     * 每个消费器执行后检查事件是否被取消，决定是否继续执行。
     *
     * <p>同一事件实例（相同 hashcode）不会被重复处理。
     *
     * @param event 事件实例
     * @return 是否有消费器被执行
     */
    @SuppressWarnings("unchecked")
    public <T extends Event> boolean post(T event) {
        Class<? extends Event> eventType = event.getClass();

        List<EventConsumer<?>> consumers = byType.get(eventType);

        if (consumers == null || consumers.isEmpty()) {
            return false;
        }

        int hashCode = System.identityHashCode(event);
        Integer recent = recentHashcodes.get(eventType);
        if (recent != null && recent == hashCode) {
            return false;
        }
        recentHashcodes.put(eventType, hashCode);

        consumers = List.copyOf(consumers);

        boolean hasExecuted = false;
        for (EventConsumer consumer : consumers) {
            boolean cancelled = event instanceof ICancellableEvent e && e.isCanceled();

            if (cancelled && !consumer.runWhenCancelled()) {
                continue;
            }

            try {
                consumer.accept(event);
            } catch (Exception e) {
                throw new RuntimeException("Exception in event consumer for " + eventType.getName(), e);
            }
            hasExecuted = true;
        }

        return hasExecuted;
    }

    /**
     * 附加消费器到实体。
     *
     * <p>同步更新树结构和类型索引。
     *
     * @param consumer 消费器
     */
    public <T extends Event> void attachConsumer(EventConsumer<T> consumer) {
        tree.insert(consumer.path(), consumer);
        byType.computeIfAbsent(consumer.eventType(), k -> new ArrayList<>()).add(consumer);
    }

    /**
     * 附加消费器到实体的便捷方法。
     *
     * <p>自动创建消费器实例并附加到实体。
     *
     * @param eventType  事件类型
     * @param runWhenCancelled 事件取消时是否执行
     * @param handler   处理逻辑
     * @param path      路径数组
     */
    public <T extends Event> void attachConsumer(
            Class<T> eventType,
            boolean runWhenCancelled,
            BiConsumer<T, EventConsumer<T>> handler,
            Identifier... path
    ) {
        attachConsumer(EventConsumer.of(eventType, path, runWhenCancelled, handler));
    }

    /**
     * 从实体身上移除指定消费器。
     *
     * <p>同时从树结构和类型索引中移除。
     *
     * @param consumer 要移除的消费器
     * @return 是否成功移除
     */
    public <T extends Event> boolean detachConsumer(EventConsumer<T> consumer) {
        boolean removedFromTree = tree.removeInstance(consumer).contains(consumer);

        List<EventConsumer<?>> typeList = byType.get(consumer.eventType());
        if (typeList != null) {
            typeList.remove(consumer);
            if (typeList.isEmpty()) {
                byType.remove(consumer.eventType());
            }
        }

        return removedFromTree;
    }

    /**
     * 精确路径移除：移除路径叶子节点上的所有消费器。
     *
     * @param path 路径数组
     * @return 被移除的消费器列表
     */
    public List<EventConsumer<?>> detachPath(Identifier... path) {
        List<EventConsumer<?>> removed = tree.removeExact(path);
        removed.forEach(c -> {
            List<EventConsumer<?>> typeList = byType.get(c.eventType());
            if (typeList != null) {
                typeList.remove(c);
                if (typeList.isEmpty()) {
                    byType.remove(c.eventType());
                }
            }
        });
        return removed;
    }

    /**
     * 子树移除：移除路径下的所有消费器。
     *
     * @param path 路径数组
     * @return 被移除的消费器列表
     */
    public List<EventConsumer<?>> detachSubtree(Identifier... path) {
        Collection<EventConsumer<?>> removed = tree.removeSubtree(path);
        removed.forEach(c -> {
            List<EventConsumer<?>> typeList = byType.get(c.eventType());
            if (typeList != null) {
                typeList.remove(c);
                if (typeList.isEmpty()) {
                    byType.remove(c.eventType());
                }
            }
        });
        return new ArrayList<>(removed);
    }

    /**
     * 移除所有消费器。
     */
    public void detachAll() {
        tree.removeAll();
        byType.clear();
    }

    /**
     * 获取指定类型的所有消费器。
     *
     * @param eventType 事件类型
     * @return 消费器列表，不存在则返回空列表
     */
    public <T extends Event> List<EventConsumer<T>> getConsumers(Class<T> eventType) {
        List<EventConsumer<?>> list = byType.get(eventType);
        return list != null ? (List<EventConsumer<T>>) (List) List.copyOf(list) : Collections.emptyList();
    }

    /**
     * 获取所有已注册的事件类型。
     *
     * @return 事件类型集合
     */
    public Set<Class<? extends Event>> getRegisteredEventTypes() {
        return Collections.unmodifiableSet(byType.keySet());
    }

    /**
     * 检查是否有指定类型的消费器。
     *
     * @param eventType 事件类型
     * @return 是否有消费器
     */
    public <T extends Event> boolean hasConsumers(Class<T> eventType) {
        List<EventConsumer<?>> list = byType.get(eventType);
        return list != null && !list.isEmpty();
    }

    /**
     * 获取消费器总数。
     *
     * @return 消费器数量
     */
    public int getConsumerCount() {
        return byType.values().stream().mapToInt(List::size).sum();
    }
}
