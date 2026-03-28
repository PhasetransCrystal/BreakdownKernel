package net.phasetranscrystal.breacore.api.block.debug;

import net.phasetranscrystal.breacore.api.blockentity.debug.FluidFurnaceBlockEntity;
import net.phasetranscrystal.breacore.data.blocks.BreaBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class FluidFurnaceBlock extends Block implements EntityBlock, BlockUIMenuType.BlockUI {

    public FluidFurnaceBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(@NonNull BlockState state, Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else if (player instanceof ServerPlayer serverPlayer) {
            BlockUIMenuType.openUI(serverPlayer, pos);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public ModularUI createUI(BlockUIMenuType.BlockUIHolder holder) {
        if (holder.player.level().getBlockEntity(holder.pos) instanceof FluidFurnaceBlockEntity furnaceBE) {
            return furnaceBE.createUI(holder);
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NonNull BlockState blockState, @NonNull BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        } else {
            return (_, _, _, blockEntity) -> {
                if (blockEntity instanceof FluidFurnaceBlockEntity furnaceBE) {
                    furnaceBE.serverTick();
                }
            };
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos blockPos, @NonNull BlockState blockState) {
        return new FluidFurnaceBlockEntity(BreaBlocks.FluidFurnaceBlock.getSibling(Registries.BLOCK_ENTITY_TYPE).value(), blockPos, blockState);
    }
}
