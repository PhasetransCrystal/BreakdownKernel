package net.phasetranscrystal.breacore.api.machine.trait;

import net.phasetranscrystal.breacore.api.machine.MetaMachine;

import net.minecraft.core.Direction;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import com.lowdragmc.lowdraglib2.syncdata.holder.blockentity.IBlockEntityManaged;
import com.lowdragmc.lowdraglib2.syncdata.storage.FieldManagedStorage;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public abstract class MachineTrait implements IBlockEntityManaged {

    @Getter
    private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);

    @Getter
    protected final MetaMachine machine;
    @Setter
    protected Predicate<@Nullable Direction> capabilityValidator;

    public MachineTrait(MetaMachine machine) {
        this.machine = machine;
        this.capabilityValidator = side -> true;
        machine.attachTraits(this);
    }

    public final boolean hasCapability(@Nullable Direction side) {
        return capabilityValidator.test(side);
    }

    @Override
    public void notifyPersistence() {
        machine.notifyPersistence();
    }

    public void onMachineLoad() {}

    public void onMachineUnLoad() {}

    /**
     * Use for data not able to be saved with the SyncData system, like optional mod compatiblity in internal machines.
     *
     * @param output  the Storage to load data from
     * @param forDrop if the save is done for dropping the machine as an item.
     */
    public void saveCustomPersistedData(@NotNull ValueOutput output, boolean forDrop) {}

    public void loadCustomPersistedData(@NotNull ValueInput input) {}

    @Override
    public void scheduleRenderUpdate() {
        machine.scheduleRenderUpdate();
    }
}
