package net.ptcrys.breakdown.api.material.stack;

import net.minecraft.core.NonNullList;
import net.neoforged.neoforge.transfer.StacksResourceHandler;

import com.mojang.serialization.Codec;

public class MaterialStacksResourceHandler extends StacksResourceHandler<MaterialStack, MaterialResource> {

    protected MaterialStacksResourceHandler(NonNullList<MaterialStack> stacks, MaterialStack emptyStack, Codec<MaterialStack> stackCodec) {
        super(stacks, emptyStack, stackCodec);
    }

    protected MaterialStacksResourceHandler(int size, MaterialStack emptyStack, Codec<MaterialStack> stackCodec) {
        super(size, emptyStack, stackCodec);
    }

    @Override
    protected MaterialResource getResourceFrom(MaterialStack stack) {
        return null;
    }

    @Override
    protected int getAmountFrom(MaterialStack stack) {
        return 0;
    }

    @Override
    protected MaterialStack getStackFrom(MaterialResource resource, int amount) {
        return null;
    }

    @Override
    protected MaterialStack copyOf(MaterialStack stack) {
        return null;
    }

    @Override
    protected int getCapacity(int index, MaterialResource resource) {
        return 0;
    }
}
