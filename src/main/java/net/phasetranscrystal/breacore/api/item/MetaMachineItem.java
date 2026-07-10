package net.phasetranscrystal.breacore.api.item;

import net.phasetranscrystal.breacore.api.block.IMachineBlock;
import net.phasetranscrystal.breacore.api.machine.MachineDefinition;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

import com.lowdragmc.lowdraglib2.client.renderer.IItemRendererProvider;
import com.lowdragmc.lowdraglib2.client.renderer.IRenderer;
import org.jetbrains.annotations.Nullable;

public class MetaMachineItem extends BlockItem implements IItemRendererProvider {

    public MetaMachineItem(IMachineBlock block, Properties properties) {
        super(block.self(), properties);
    }

    public MachineDefinition getDefinition() {
        return ((IMachineBlock) getBlock()).getDefinition();
    }

    @Nullable
    @Override
    public IRenderer getRenderer(ItemStack stack) {
        return ((IMachineBlock) getBlock()).getDefinition().getRenderer();
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        return super.placeBlock(context, state);
    }
}
