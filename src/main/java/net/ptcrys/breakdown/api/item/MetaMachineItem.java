package net.ptcrys.breakdown.api.item;

import net.ptcrys.breakdown.api.block.IMachineBlock;
import net.ptcrys.breakdown.api.machine.MachineDefinition;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

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
}
