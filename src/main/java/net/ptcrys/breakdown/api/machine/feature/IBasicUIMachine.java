package net.ptcrys.breakdown.api.machine.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;

public interface IBasicUIMachine extends IMachineFeature, BlockUIMenuType.BlockUI {

    @Override
    default BlockUIMenuType.BlockUIHolder createUIHolder(Player player, BlockPos pos, BlockState blockState) {
        return BlockUIMenuType.BlockUI.super.createUIHolder(player, pos, blockState);
    }

    @Override
    default boolean stillValid(BlockUIMenuType.BlockUIHolder holder) {
        return self().isInValid();
    }

    @Override
    default Component getUIDisplayName(BlockUIMenuType.BlockUIHolder holder) {
        return BlockUIMenuType.BlockUI.super.getUIDisplayName(holder);
    }
}
