package net.ptcrys.breakdown.api.registry.registrate;

import net.ptcrys.registrylib.Group;
import net.ptcrys.registrylib.RegistryCore;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class BreaGroup extends Group {

    protected BreaGroup(Builder builder) {
        super(builder);
    }

    public static class Builder extends Group.Builder {

        protected Builder(RegistryCore core, String name) {
            super(core, name);
        }

        public BreaGroup build() {
            return new BreaGroup(this);
        }

        @Override
        public BreaGroup.Builder initialBlockProperties(Supplier<? extends Block> block) {
            super.initialBlockProperties(block);
            return this;
        }

        @Override
        public BreaGroup.Builder initialBlockProperties(Block block) {
            super.initialBlockProperties(block);
            return this;
        }

        @Override
        public BreaGroup.Builder langPrefix(String prefix) {
            super.langPrefix(prefix);
            return this;
        }

        @Override
        public BreaGroup.Builder tab(@Nullable ResourceKey<CreativeModeTab> tab) {
            super.tab(tab);
            return this;
        }

        @Override
        public BreaGroup.Builder initialItemProperties(Supplier<Item.Properties> initialItemProperties) {
            super.initialItemProperties(initialItemProperties);
            return this;
        }

        @Override
        public BreaGroup.Builder blockProperties(UnaryOperator<BlockBehaviour.Properties> blockProperties) {
            super.blockProperties(blockProperties);
            return this;
        }

        @Override
        public BreaGroup.Builder itemProperties(UnaryOperator<Item.Properties> itemProperties) {
            super.itemProperties(itemProperties);
            return this;
        }
    }
}
