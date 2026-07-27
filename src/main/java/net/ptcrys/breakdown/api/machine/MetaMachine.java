package net.ptcrys.breakdown.api.machine;

import net.ptcrys.breakdown.BreaLib;
import net.ptcrys.breakdown.api.block.BlockProperties;
import net.ptcrys.breakdown.api.block.IAppearance;
import net.ptcrys.breakdown.api.block.IMachineBlock;
import net.ptcrys.breakdown.api.block.MetaMachineBlock;
import net.ptcrys.breakdown.api.blockentity.IMachineBlockEntity;
import net.ptcrys.breakdown.api.blockentity.IPaintable;
import net.ptcrys.breakdown.api.blockentity.ITickSubscription;
import net.ptcrys.breakdown.api.blockentity.MetaMachineBlockEntity;
import net.ptcrys.breakdown.api.capability.IToolable;
import net.ptcrys.breakdown.api.item.tool.IToolGridHighLight;
import net.ptcrys.breakdown.api.machine.feature.IRedstoneSignalMachine;
import net.ptcrys.breakdown.api.machine.trait.MachineTrait;
import net.ptcrys.breakdown.common.machine.owner.MachineOwner;
import net.ptcrys.breakdown.common.machine.owner.PlayerOwner;
import net.ptcrys.breakdown.utils.RelativeDirection;
import net.ptcrys.breakdown.utils.RotationState;
import net.ptcrys.breakdown.utils.TickableSubscription;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib2.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib2.syncdata.holder.blockentity.IBlockEntityManaged;
import com.lowdragmc.lowdraglib2.syncdata.storage.FieldManagedStorage;
import com.lowdragmc.lowdraglib2.utils.virtuallevel.DummyWorld;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MetaMachine implements IBlockEntityManaged, IToolable, ITickSubscription, IAppearance, IToolGridHighLight, IPaintable, IRedstoneSignalMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MetaMachine.class);
    @Getter
    private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);
    @Setter
    @Getter
    @Persisted
    @DescSynced
    @Nullable
    private UUID ownerUUID;
    @Getter
    public final IMachineBlockEntity holder;
    @Getter
    @Setter
    @Persisted
    @DescSynced
    @RequireRerender
    private int paintingColor = -1;
    @Getter
    protected final List<MachineTrait> traits;
    private final List<TickableSubscription> serverTicks;
    private final List<TickableSubscription> waitingToAdd;

    public MetaMachine(IMachineBlockEntity holder) {
        this.holder = holder;
        this.traits = new ArrayList<>();
        this.serverTicks = new ArrayList<>();
        this.waitingToAdd = new ArrayList<>();
        // bind sync storage
        this.holder.getRootStorage().attach(getSyncStorage());
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void notifyPersistence() {
        var level = getLevel();
        if (level != null && !level.isClientSide() && level.getServer() != null) {
            level.getServer().execute(this::markDirty);
        }
    }

    public @Nullable Level getLevel() {
        return holder.level();
    }

    public BlockPos getPos() {
        return holder.pos();
    }

    public BlockState getBlockState() {
        return holder.getSelf().getBlockState();
    }

    public boolean isRemote() {
        return getLevel() == null ? BreaLib.isClientThread() : getLevel().isClientSide();
    }

    public void notifyBlockUpdate() {
        holder.notifyBlockUpdate();
    }

    public void scheduleRenderUpdate() {
        holder.scheduleRenderUpdate();
    }

    public void scheduleNeighborShapeUpdate() {
        Level level = getLevel();
        BlockPos pos = getPos();

        if (level == null || pos == null)
            return;

        level.getBlockState(pos).updateNeighbourShapes(level, pos, Block.UPDATE_ALL);
    }

    public long getOffsetTimer() {
        return holder.getOffsetTimer();
    }

    public void markDirty() {
        holder.getSelf().setChanged();
    }

    public boolean isInValid() {
        return holder.getSelf().isRemoved();
    }

    public void onUnload() {
        traits.forEach(MachineTrait::onMachineUnLoad);
        for (TickableSubscription serverTick : serverTicks) {
            serverTick.unsubscribe();
        }
        serverTicks.clear();
    }

    public void onLoad() {
        traits.forEach(MachineTrait::onMachineLoad);
    }

    /**
     * Use for data not able to be saved with the SyncData system, like optional mod compatiblity in internal machines.
     *
     * @param output  the CompoundTag to load data from
     * @param forDrop if the save is done for dropping the machine as an item.
     */
    public void saveCustomPersistedData(@NotNull ValueOutput output, boolean forDrop) {
        for (MachineTrait trait : this.getTraits()) {
            trait.saveCustomPersistedData(output, forDrop);
        }
    }

    public void loadCustomPersistedData(@NotNull ValueInput input) {
        for (MachineTrait trait : this.getTraits()) {
            trait.loadCustomPersistedData(input);
        }
    }

    public void applyImplicitComponents(MetaMachineBlockEntity.ExDataComponentInput componentInput) {}

    public void collectImplicitComponents(DataComponentMap.Builder components) {}

    /**
     * For initialization. To get level and property fields after auto sync, you can subscribe it in {@link #onLoad()}
     * event.
     */
    @Nullable
    public TickableSubscription subscribeServerTick(Runnable runnable) {
        if (!isRemote()) {
            var subscription = new TickableSubscription(runnable);
            waitingToAdd.add(subscription);
            var blockState = getBlockState();
            if (!blockState.getValue(BlockProperties.SERVER_TICK)) {
                if (getLevel() instanceof ServerLevel serverLevel) {
                    blockState = blockState.setValue(BlockProperties.SERVER_TICK, true);
                    holder.getSelf().setBlockState(blockState);
                    serverLevel.getServer().schedule(new TickTask(0, () -> {
                        if (!isInValid()) {
                            serverLevel.setBlockAndUpdate(getPos(),
                                    getBlockState().setValue(BlockProperties.SERVER_TICK, true));
                        }
                    }));
                }
            }
            return subscription;
        } else if (getLevel() instanceof DummyWorld) {
            var subscription = new TickableSubscription(runnable);
            waitingToAdd.add(subscription);
            return subscription;
        }
        return null;
    }

    public void unsubscribe(@Nullable TickableSubscription current) {
        if (current != null) {
            current.unsubscribe();
        }
    }

    public final void serverTick() {
        executeTick();
        if (serverTicks.isEmpty() && waitingToAdd.isEmpty() && !isInValid()) {
            getLevel().setBlockAndUpdate(getPos(), getBlockState().setValue(BlockProperties.SERVER_TICK, false));
        }
    }

    public boolean isFirstDummyWorldTick = true;

    public void clientTick() {
        if (getLevel() instanceof DummyWorld) {
            if (isFirstDummyWorldTick) {
                isFirstDummyWorldTick = false;
                onLoad();
            }
            executeTick();
        }
    }

    private void executeTick() {
        if (!waitingToAdd.isEmpty()) {
            serverTicks.addAll(waitingToAdd);
            waitingToAdd.clear();
        }
        var iter = serverTicks.iterator();
        while (iter.hasNext()) {
            var tickable = iter.next();
            if (tickable.isStillSubscribed()) {
                tickable.run();
            }
            if (isInValid()) break;
            if (!tickable.isStillSubscribed()) {
                iter.remove();
            }
        }
    }

    @Nullable
    public static MetaMachine getMachine(BlockGetter level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof IMachineBlockEntity machineBlockEntity) {
            return machineBlockEntity.getMetaMachine();
        }
        return null;
    }

    /**
     * All traits should be initialized while MetaMachine is creating. you cannot add them on the fly.
     */
    public void attachTraits(MachineTrait trait) {
        traits.add(trait);
    }

    public void clearInventory(ResourceHandler<ItemResource> inventory) {
        try (var ctx = Transaction.openRoot()) {
            for (int i = 0; i < inventory.size(); i++) {
                var resource = inventory.getResource(i);
                var amount = inventory.getAmountAsInt(i);
                if (!resource.isEmpty() && amount > 0) {
                    ItemStack stack = resource.toStack(amount);
                    inventory.extract(i, resource, amount, ctx);
                    Block.popResource(getLevel(), getPos(), stack);
                }
            }
            ctx.commit();
        }
    }

    public MachineDefinition getDefinition() {
        return holder.getDefinition();
    }

    /**
     * Called to obtain list of AxisAlignedBB used for collision testing, highlight rendering
     * and ray tracing this meta tile entity's block in world
     */
    public void addCollisionBoundingBox(List<VoxelShape> collisionList) {
        collisionList.add(Shapes.block());
    }

    public boolean canSetIoOnSide(@Nullable Direction direction) {
        return !hasFrontFacing() || getFrontFacing() != direction;
    }

    public static @NotNull Direction getFrontFacing(@Nullable MetaMachine machine) {
        return machine == null ? Direction.NORTH : machine.getFrontFacing();
    }

    public Direction getFrontFacing() {
        var blockState = getBlockState();
        if (blockState.getBlock() instanceof MetaMachineBlock machineBlock) {
            return machineBlock.getFrontFacing(blockState);
        }
        return Direction.NORTH;
    }

    public final boolean hasFrontFacing() {
        var blockState = getBlockState();
        if (blockState.getBlock() instanceof MetaMachineBlock machineBlock) {
            return machineBlock.getRotationState() != RotationState.NONE;
        }
        return false;
    }

    public boolean isFacingValid(Direction facing) {
        if (allowExtendedFacing()) {
            return true;
        }
        if (hasFrontFacing() && facing == getFrontFacing()) return false;
        var blockState = getBlockState();
        if (blockState.getBlock() instanceof MetaMachineBlock metaMachineBlock) {
            return metaMachineBlock.rotationState.test(facing);
        }
        return false;
    }

    public void setFrontFacing(Direction facing) {
        var oldFacing = getFrontFacing();

        if (allowExtendedFacing()) {
            var newUpwardsFacing = RelativeDirection.simulateAxisRotation(facing, oldFacing, getUpwardsFacing());
            setUpwardsFacing(newUpwardsFacing);
        }

        var blockState = getBlockState();
        if (blockState.getBlock() instanceof MetaMachineBlock metaMachineBlock && isFacingValid(facing)) {
            getLevel().setBlockAndUpdate(getPos(),
                    blockState.setValue(metaMachineBlock.rotationState.property, facing));
        }

        if (getLevel() != null && !getLevel().isClientSide()) {
            notifyBlockUpdate();
            markDirty();
        }
    }

    public static @NotNull Direction getUpwardFacing(@Nullable MetaMachine machine) {
        return machine == null || !machine.allowExtendedFacing() ? Direction.NORTH :
                machine.getBlockState().getValue(IMachineBlock.UPWARDS_FACING_PROPERTY);
    }

    public Direction getUpwardsFacing() {
        return this.allowExtendedFacing() ? this.getBlockState().getValue(IMachineBlock.UPWARDS_FACING_PROPERTY) :
                Direction.NORTH;
    }

    public void setUpwardsFacing(@NotNull Direction upwardsFacing) {
        if (!getDefinition().isAllowExtendedFacing()) {
            return;
        }
        if (upwardsFacing.getAxis() == Direction.Axis.Y) {
            BreaLib.LOGGER.error("Tried to set upwards facing to invalid facing {}! Skipping", upwardsFacing);
            return;
        }
        var blockState = getBlockState();
        if (blockState.getBlock() instanceof MetaMachineBlock &&
                blockState.getValue(IMachineBlock.UPWARDS_FACING_PROPERTY) != upwardsFacing) {
            getLevel().setBlockAndUpdate(getPos(),
                    blockState.setValue(IMachineBlock.UPWARDS_FACING_PROPERTY, upwardsFacing));
            if (getLevel() != null && !getLevel().isClientSide()) {
                notifyBlockUpdate();
                markDirty();
            }
        }
    }

    public void onRotated(Direction oldFacing, Direction newFacing) {}

    public boolean allowExtendedFacing() {
        return getDefinition().isAllowExtendedFacing();
    }

    public void onNeighborChanged(Block block, BlockPos fromPos, boolean isMoving) {}

    public void animateTick(RandomSource random) {}

    @Override
    public @Nullable BlockState getBlockAppearance(BlockState state, BlockAndLightGetter level, BlockPos pos, Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
        return getDefinition().getAppearance().get();
    }

    @Override
    public int getOutputSignal(@Nullable Direction side) {
        if (side == null) return 0;
        return 0;
    }

    @Override
    public boolean canConnectRedstone(@Nullable Direction side) {
        if (side == null) return false;
        return false;
    }

    public @Nullable MachineOwner getOwner() {
        return MachineOwner.getOwner(ownerUUID);
    }

    public @Nullable PlayerOwner getPlayerOwner() {
        return MachineOwner.getPlayerOwner(ownerUUID);
    }

    @Override
    public int getDefaultPaintingColor() {
        return getDefinition().getDefaultPaintingColor();
    }
}
