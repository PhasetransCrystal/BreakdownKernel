package net.ptcrys.breakdown.api.machine.feature;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jetbrains.annotations.ApiStatus;

/**
 * A machine that can save its contents when dropped.
 */
public interface IDropSaveMachine extends IMachineFeature {

    /**
     * Whether save for breaking.
     */
    default boolean saveBreak() {
        return true;
    }

    /**
     * Whether save for cloning.
     */
    default boolean savePickClone() {
        return true;
    }

    /**
     * Saves the contents of the block entity to a compound tag.
     *
     * @param output The storage to save to.
     */
    @ApiStatus.ScheduledForRemoval(inVersion = "1.9.0")
    @Deprecated(forRemoval = true)
    default void saveToItem(ValueOutput output) {
        self().holder.saveManagedPersistentData(output, true);
    }

    /**
     * Loads the contents of the block entity from a compound tag.
     */
    default void loadFromItem(ValueInput input) {
        self().holder.loadManagedPersistentData(input);
    }
}
