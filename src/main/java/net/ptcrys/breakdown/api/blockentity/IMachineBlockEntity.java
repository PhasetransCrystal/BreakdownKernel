package net.ptcrys.breakdown.api.blockentity;

import net.ptcrys.breakdown.api.BreaApi;
import net.ptcrys.breakdown.api.block.IMachineBlock;
import net.ptcrys.breakdown.api.machine.MachineDefinition;
import net.ptcrys.breakdown.api.machine.MetaMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import com.lowdragmc.lowdraglib2.syncdata.holder.blockentity.IBlockEntityManaged;
import com.lowdragmc.lowdraglib2.syncdata.holder.blockentity.IRPCBlockEntity;
import com.lowdragmc.lowdraglib2.syncdata.holder.blockentity.ISyncPersistRPCBlockEntity;
import com.lowdragmc.lowdraglib2.syncdata.storage.MultiManagedStorage;

public interface IMachineBlockEntity extends ISyncPersistRPCBlockEntity, IRPCBlockEntity, IBlockEntityManaged {

    default BlockEntity self() {
        return (BlockEntity) this;
    }

    default Level level() {
        return self().getLevel();
    }

    default BlockPos pos() {
        return self().getBlockPos();
    }

    default void notifyBlockUpdate() {
        if (level() != null) {
            level().updateNeighborsAt(pos(), level().getBlockState(pos()).getBlock());
        }
    }

    default void scheduleRenderUpdate() {
        var pos = pos();
        if (level() != null) {
            var state = level().getBlockState(pos);
            if (level().isClientSide()) {
                level().sendBlockUpdated(pos, state, state, 1 << 3);
            } else {
                level().blockEvent(pos, state.getBlock(), 1, 0);
            }
        }
    }

    default long getOffsetTimer() {
        if (level() == null) return getOffset();
        else if (level().isClientSide()) return BreaApi.CLIENT_TIME + getOffset();

        var server = level().getServer();
        if (server != null) return server.getTickCount() + getOffset();
        return getOffset();
    }

    default MachineDefinition getDefinition() {
        if (self().getBlockState().getBlock() instanceof IMachineBlock machineBlock) {
            return machineBlock.getDefinition();
        } else {
            throw new IllegalStateException("MetaMachineBlockEntity is created for an un available block: " +
                    self().getBlockState().getBlock());
        }
    }

    MetaMachine getMetaMachine();

    long getOffset();

    @Override
    MultiManagedStorage getSyncStorage();

    @Override
    default MultiManagedStorage getRootStorage() {
        return getSyncStorage();
    }

    @Override
    default void saveCustomPersistedData(ValueOutput output, boolean forDrop) {
        ISyncPersistRPCBlockEntity.super.saveCustomPersistedData(output, forDrop);
        getMetaMachine().saveCustomPersistedData(output, forDrop);
    }

    @Override
    default void loadCustomPersistedData(ValueInput input) {
        ISyncPersistRPCBlockEntity.super.loadCustomPersistedData(input);
        getMetaMachine().loadCustomPersistedData(input);
    }
}
