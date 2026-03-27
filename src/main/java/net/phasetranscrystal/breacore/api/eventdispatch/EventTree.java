package net.phasetranscrystal.breacore.api.eventdispatch;

import com.google.common.collect.Lists;
import net.minecraft.resources.Identifier;

import java.util.*;

/**
 * 泛用树结构，用于按路径存储和检索元素。
 *
 * <p>使用非递归实现避免栈溢出问题。
 *
 * @param <T> 存储的元素类型
 */
public class EventTree<T> {
    private final Map<Identifier, EventTree<T>> children = new LinkedHashMap<>();
    private final List<T> values = new ArrayList<>();

    /**
     * 在指定路径插入值。
     *
     * <p>路径为空时直接添加到根节点。
     *
     * @param path  路径数组
     * @param value 要插入的值
     */
    public void insert(Identifier[] path, T value) {
        if (path == null || path.length == 0) {
            values.add(value);
            return;
        }

        EventTree<T> current = this;
        for (Identifier segment : path) {
            current.children.computeIfAbsent(segment, k -> new EventTree<>());
            current = current.children.get(segment);
        }
        current.values.add(value);
    }

    /**
     * 精确路径移除：移除指定路径叶子节点上的所有值。
     *
     * <p>移除该路径下的全部元素，并清理空节点。
     *
     * @param path 路径数组
     * @return 被删除的值列表
     */
    public List<T> removeExact(Identifier[] path) {
        if (path == null || path.length == 0) {
            List<T> removed = new ArrayList<>(values);
            values.clear();
            return removed;
        }

        List<EventTree<T>> pathNodes = Lists.newArrayList();
        pathNodes.add(this);

        EventTree<T> current = this;
        for (Identifier segment : path) {
            EventTree<T> next = current.children.get(segment);
            if (next == null) {
                return Collections.emptyList();
            }
            pathNodes.add(next);
            current = next;
        }

        List<T> removed = new ArrayList<>(current.values);
        current.values.clear();
        pruneEmptyNodes(pathNodes);
        return removed;
    }

    /**
     * 子树移除：移除指定路径下的所有元素。
     *
     * @param path 路径数组，为空时移除所有根节点元素
     * @return 被删除的所有值
     */
    public Collection<T> removeSubtree(Identifier[] path) {
        if (path == null || path.length == 0) {
            List<T> allValues = new ArrayList<>();
            collectAllValues(this, allValues);
            clear();
            return allValues;
        }

        List<EventTree<T>> pathNodes = Lists.newArrayList();
        pathNodes.add(this);

        EventTree<T> current = this;
        for (Identifier segment : path) {
            EventTree<T> next = current.children.get(segment);
            if (next == null) {
                return Collections.emptyList();
            }
            pathNodes.add(next);
            current = next;
        }

        List<T> removed = new ArrayList<>();
        collectAllValues(current, removed);
        current.clear();
        pruneEmptyNodes(pathNodes.subList(0, pathNodes.size() - 1));
        return removed;
    }

    /**
     * 实例移除：移除树中所有等于给定实例的元素。
     *
     * @param value 要移除的实例
     * @return 被删除的所有值
     */
    public List<T> removeInstance(T value) {
        List<T> removed = new ArrayList<>();
        Deque<EventTree<T>> stack = new ArrayDeque<>();
        stack.push(this);

        while (!stack.isEmpty()) {
            EventTree<T> node = stack.pop();
            if (node.values.remove(value)) {
                removed.add(value);
            }
            node.children.values().forEach(stack::push);
        }
        return removed;
    }

    /**
     * 移除所有内容。
     */
    public void removeAll() {
        clear();
    }

    /**
     * 精确查找：获取指定路径叶子节点上的所有值。
     *
     * @param path 路径数组
     * @return 叶子节点的值列表，若路径不存在返回空列表
     */
    public List<T> findExact(Identifier[] path) {
        if (path == null || path.length == 0) {
            return new ArrayList<>(values);
        }

        EventTree<T> current = this;
        for (Identifier segment : path) {
            EventTree<T> next = current.children.get(segment);
            if (next == null) {
                return Collections.emptyList();
            }
            current = next;
        }
        return new ArrayList<>(current.values);
    }

    /**
     * 获取所有值。
     *
     * @return 树中所有元素
     */
    public Collection<T> getAllValues() {
        List<T> allValues = new ArrayList<>();
        collectAllValues(this, allValues);
        return allValues;
    }

    private void clear() {
        values.clear();
        children.clear();
    }

    private void collectAllValues(EventTree<T> node, List<T> result) {
        Deque<EventTree<T>> stack = new ArrayDeque<>();
        stack.push(node);

        while (!stack.isEmpty()) {
            EventTree<T> current = stack.pop();
            result.addAll(current.values);
            current.children.values().forEach(stack::push);
        }
    }

    private void pruneEmptyNodes(List<EventTree<T>> pathNodes) {
        for (int i = pathNodes.size() - 1; i > 0; i--) {
            EventTree<T> parent = pathNodes.get(i - 1);
            EventTree<T> child = pathNodes.get(i);

            if (child.isEmpty()) {
                parent.children.values().removeIf(v -> v == child);
            } else {
                break;
            }
        }
    }

    private boolean isEmpty() {
        return values.isEmpty() && children.isEmpty();
    }
}
