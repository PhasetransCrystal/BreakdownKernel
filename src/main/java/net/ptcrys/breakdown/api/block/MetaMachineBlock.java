package net.ptcrys.breakdown.api.block;

import net.ptcrys.breakdown.api.blockentity.IMachineBlockEntity;
import net.ptcrys.breakdown.api.machine.MachineDefinition;
import net.ptcrys.breakdown.api.machine.MetaMachine;
import net.ptcrys.breakdown.api.machine.feature.IDropSaveMachine;
import net.ptcrys.breakdown.api.machine.feature.IMachineLife;
import net.ptcrys.breakdown.api.machine.feature.IMachineModifyDrops;
import net.ptcrys.breakdown.utils.RotationState;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.lowdragmc.lowdraglib2.client.renderer.IRenderer;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MetaMachineBlock extends AppearanceBlock implements IMachineBlock {

    @Getter
    public final MachineDefinition definition;
    @Getter
    public final RotationState rotationState;

    public MetaMachineBlock(Properties properties, MachineDefinition definition) {
        super(properties);
        this.definition = definition;
        this.rotationState = RotationState.get();
        if (rotationState != RotationState.NONE) {
            BlockState defaultState = this.defaultBlockState().setValue(rotationState.property,
                    rotationState.defaultDirection);
            if (definition.isAllowExtendedFacing()) {
                defaultState = defaultState.setValue(IMachineBlock.UPWARDS_FACING_PROPERTY, Direction.NORTH);
            }
            registerDefaultState(defaultState);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockProperties.SERVER_TICK);
        RotationState rotationState = RotationState.get();
        if (rotationState != RotationState.NONE) {
            pBuilder.add(rotationState.property);
            if (MachineDefinition.getBuilt().isAllowExtendedFacing()) {
                pBuilder.add(IMachineBlock.UPWARDS_FACING_PROPERTY);
            }
        }
    }

    @Nullable
    public MetaMachine getMachine(BlockGetter level, BlockPos pos) {
        return MetaMachine.getMachine(level, pos);
    }

    @Nullable
    @Override
    public IRenderer getRenderer(BlockState state) {
        return definition.getRenderer();
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getRotationState() == RotationState.NONE ? definition.getShape(Direction.NORTH) :
                definition.getShape(pState.getValue(getRotationState().property));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        var machine = getMachine(level, pos);
        if (machine != null) {
            machine.animateTick(random);
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity player,
                            ItemStack pStack) {
        if (!pLevel.isClientSide()) {
            var machine = getMachine(pLevel, pPos);
            if (machine != null) {
                if (player instanceof ServerPlayer sPlayer) {
                    machine.setOwnerUUID(sPlayer.getUUID());
                    machine.markDirty();
                }
            }
            if (machine instanceof IDropSaveMachine dropSaveMachine) {
                var data = pStack.get(DataComponents.BLOCK_ENTITY_DATA);
                if (data != null) {
                    HolderLookup.Provider lookup = pLevel.registryAccess();
                    var input = TagValueInput.create(ProblemReporter.DISCARDING, lookup, data.copyTagWithoutId());
                    dropSaveMachine.loadFromItem(input);
                }
            }
            if (machine instanceof IMachineLife machineLife) {
                machineLife.onMachinePlaced(player, pStack);
            }
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        // needed to trigger block updates so machines connect to open cables properly.
        level.updateNeighbourForOutputSignal(pos, this);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        RotationState rotationState = getRotationState();
        var player = context.getPlayer();
        var blockPos = context.getClickedPos();
        var state = defaultBlockState();
        if (player != null && rotationState != RotationState.NONE) {
            if (rotationState == RotationState.Y_AXIS) {
                state = state.setValue(rotationState.property, Direction.UP);
            } else {
                state = state.setValue(rotationState.property, player.getDirection().getOpposite());
            }
            Vec3 pos = player.position();
            if (Math.abs(pos.x - (double) ((float) blockPos.getX() + 0.5F)) < 2.0D &&
                    Math.abs(pos.z - (double) ((float) blockPos.getZ() + 0.5F)) < 2.0D) {
                double d0 = pos.y + (double) player.getEyeHeight();
                if (d0 - (double) blockPos.getY() > 2.0D && rotationState.test(Direction.UP)) {
                    state = state.setValue(rotationState.property, Direction.UP);
                }
                if ((double) blockPos.getY() - d0 > 0.0D && rotationState.test(Direction.DOWN)) {
                    state = state.setValue(rotationState.property, Direction.DOWN);
                }
            }
            if (getDefinition().isAllowExtendedFacing()) {
                Direction frontFacing = state.getValue(rotationState.property);
                if (frontFacing == Direction.UP) {
                    state = state.setValue(IMachineBlock.UPWARDS_FACING_PROPERTY, player.getDirection());
                } else if (frontFacing == Direction.DOWN) {
                    state = state.setValue(IMachineBlock.UPWARDS_FACING_PROPERTY, player.getDirection().getOpposite());
                }
            }
        }
        return state;
    }

    public Direction getFrontFacing(BlockState state) {
        return getRotationState() == RotationState.NONE ? Direction.NORTH : state.getValue(getRotationState().property);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        ItemStack itemStack = super.getCloneItemStack(level, pos, state, includeData, player);
        return itemStack;
    }

    @Override
    protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int b0, int b1) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile != null) {
            return tile.triggerEvent(b0, b1);
        }
        return false;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        if (this.rotationState == RotationState.NONE) {
            return state;
        }
        return state.setValue(this.rotationState.property,
                rotation.rotate(state.getValue(this.rotationState.property)));
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity tileEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        var drops = super.getDrops(state, builder);
        if (tileEntity instanceof IMachineBlockEntity holder) {
            var machine = holder.getMetaMachine();
            if (machine instanceof IMachineModifyDrops machineModifyDrops) {
                machineModifyDrops.onDrops(drops);
            }
        }
        return drops;
    }
}
