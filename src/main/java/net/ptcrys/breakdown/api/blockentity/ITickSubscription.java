package net.ptcrys.breakdown.api.blockentity;

import net.ptcrys.breakdown.utils.TickableSubscription;

import net.minecraft.world.level.block.entity.BlockEntity;

import org.jetbrains.annotations.Nullable;

public interface ITickSubscription {

    /**
     * For initialization. To get level and property fields after auto sync, you can subscribe it in
     * {@link BlockEntity#clearRemoved()} event.
     */
    @Nullable
    TickableSubscription subscribeServerTick(Runnable runnable);

    void unsubscribe(@Nullable TickableSubscription current);

    @Nullable
    default TickableSubscription subscribeServerTick(@Nullable TickableSubscription last, Runnable runnable) {
        if (last == null || !last.isStillSubscribed()) {
            return subscribeServerTick(runnable);
        }
        return last;
    }
}
