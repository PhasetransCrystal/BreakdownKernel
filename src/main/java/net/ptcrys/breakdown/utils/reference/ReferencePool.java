package net.ptcrys.breakdown.utils.reference;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 引用对象池全局工具类，线程安全。
 */
public final class ReferencePool {

    private static final Map<Class<?>, ReferenceCollection<?>> POOLS = new ConcurrentHashMap<>();

    @Getter
    @Setter
    private static volatile boolean enableStrictCheck = false;

    /* 禁止实例化 */
    private ReferencePool() {}

    /**
     * 当前池种类数量
     */
    public static int getCount() {
        return POOLS.size();
    }

    /* ===================== 清除 ===================== */

    /**
     * 获取所有池的信息快照
     */
    public static List<ReferencePoolInfo> getAllReferencePoolInfos() {
        List<ReferencePoolInfo> list = new ArrayList<>(POOLS.size());
        for (Map.Entry<Class<?>, ReferenceCollection<?>> e : POOLS.entrySet()) {
            Class<?> type = e.getKey();
            ReferenceCollection<?> col = e.getValue();
            list.add(new ReferencePoolInfo(
                    type,
                    col.getUnusedReferenceCount(),
                    col.getUsingReferenceCount(),
                    col.getAcquireReferenceCount(),
                    col.getReleaseReferenceCount(),
                    col.getAddReferenceCount(),
                    col.getRemoveReferenceCount()));
        }
        return list;
    }

    /* ===================== Acquire ===================== */

    /**
     * 清空所有池并移除
     */
    public static void clearAll() {
        for (ReferenceCollection<?> col : POOLS.values()) {
            col.removeAll();
        }
        POOLS.clear();
    }

    /**
     * 泛型获取
     */
    public static <T extends IReference> T acquire(Class<T> clazz) {
        return acquire(clazz, FactoryCache.get(clazz));
    }

    /* ===================== Release ===================== */

    /**
     * 带工厂获取（内部使用）
     */
    private static <T extends IReference> T acquire(Class<T> clazz, ReferenceFactory<T> factory) {
        Objects.requireNonNull(clazz, "clazz is null");
        return getOrCreateCollection(clazz, factory).acquire();
    }

    /* ===================== Add ===================== */

    public static void release(IReference reference) {
        if (reference == null) throw new RuntimeException("Reference is invalid.");
        Class<? extends IReference> clazz = reference.getClass();
        internalCheckReferenceType(clazz);
        @SuppressWarnings("unchecked")
        ReferenceCollection<IReference> col = (ReferenceCollection<IReference>) poolOf(clazz);
        if (col == null) return;   // 该类型从未创建过池，忽略或抛异常均可
        col.release(reference);
    }

    public static <T extends IReference> void add(Class<T> clazz, int count) {
        add(clazz, count, FactoryCache.get(clazz));
    }

    /* ===================== Remove ===================== */

    public static <T extends IReference> void add(Class<T> clazz, int count, ReferenceFactory<T> factory) {
        Objects.requireNonNull(clazz, "clazz is null");
        if (count <= 0) return;
        getOrCreateCollection(clazz, factory).add(count);
    }

    public static <T extends IReference> void remove(Class<T> clazz, int count) {
        Objects.requireNonNull(clazz, "clazz is null");
        ReferenceCollection<T> col = poolOf(clazz);
        if (col != null) col.remove(count);
    }

    /* ===================== 内部工具 ===================== */

    public static <T extends IReference> void removeAll(Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz is null");
        ReferenceCollection<T> col = poolOf(clazz);
        if (col != null) col.removeAll();
    }

    @SuppressWarnings("unchecked")
    private static <T extends IReference> ReferenceCollection<T> poolOf(Class<T> clazz) {
        return (ReferenceCollection<T>) POOLS.get(clazz);
    }

    @SuppressWarnings("unchecked")
    private static <T extends IReference> ReferenceCollection<T> getOrCreateCollection(
                                                                                       Class<T> clazz, ReferenceFactory<T> factory) {
        return (ReferenceCollection<T>) POOLS.computeIfAbsent(clazz, k -> new ReferenceCollection<>((Class<T>) k, factory));
    }

    private static void internalCheckReferenceType(Class<?> clazz) {
        if (!enableStrictCheck) return;
        if (clazz == null) throw new RuntimeException("Reference type is invalid.");
        if (!IReference.class.isAssignableFrom(clazz))
            throw new RuntimeException("Reference type '" + clazz.getName() + "' is invalid.");
        if (clazz.isInterface() || (clazz.getModifiers() & java.lang.reflect.Modifier.ABSTRACT) != 0)
            throw new RuntimeException("Reference type must be a non-abstract class.");
    }
}
