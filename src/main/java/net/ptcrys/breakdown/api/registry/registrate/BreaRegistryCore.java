package net.ptcrys.breakdown.api.registry.registrate;

import net.ptcrys.breakdown.api.block.IMachineBlock;
import net.ptcrys.breakdown.api.block.MetaMachineBlock;
import net.ptcrys.breakdown.api.blockentity.IMachineBlockEntity;
import net.ptcrys.breakdown.api.blockentity.MetaMachineBlockEntity;
import net.ptcrys.breakdown.api.item.MetaMachineItem;
import net.ptcrys.breakdown.api.machine.MachineDefinition;
import net.ptcrys.breakdown.api.machine.MetaMachine;
import net.ptcrys.breakdown.api.machine.builder.MachineBuilder;
import net.ptcrys.registrylib.RegistryCore;
import net.ptcrys.registrylib.datagen.ProviderType;
import net.ptcrys.registrylib.datagen.provider.RegistryLibLangProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;

public class BreaRegistryCore extends RegistryCore {

    public static final ProviderType<RegistryLibLangProvider> LANG_ZH_CN = ProviderType.registerClientProvider(
            "lang_zh_cn", () -> c -> new ZhCnLangProvider(c.parent(), c.output()));

    protected BreaRegistryCore(String modid) {
        super(modid);
        withLangAlias("zh_cn", LANG_ZH_CN);
    }

    public static BreaRegistryCore create(String modid) {
        return new BreaRegistryCore(modid);
    }

    @Override
    public BreaGroup.Builder group(@NotNull String name) {
        return new BreaGroup.Builder(this, name);
    }

    public SoundEntryBuilder sound(String name) {
        return new SoundEntryBuilder(Identifier.fromNamespaceAndPath(getModid(), name));
    }

    public SoundEntryBuilder sound(Identifier name) {
        return new SoundEntryBuilder(name);
    }

    public <DEFINITION extends MachineDefinition> MachineBuilder<DEFINITION> machine(String name,
                                                                                     Function<Identifier, DEFINITION> definitionFactory,
                                                                                     Function<IMachineBlockEntity, MetaMachine> metaMachine,
                                                                                     BiFunction<BlockBehaviour.Properties, DEFINITION, IMachineBlock> blockFactory,
                                                                                     BiFunction<IMachineBlock, Item.Properties, MetaMachineItem> itemFactory,
                                                                                     TriFunction<BlockEntityType<?>, BlockPos, BlockState, IMachineBlockEntity> blockEntityFactory) {
        return new MachineBuilder<>(this, name, definitionFactory, metaMachine, blockFactory, itemFactory,
                blockEntityFactory);
    }

    public MachineBuilder<MachineDefinition> machine(String name,
                                                     Function<IMachineBlockEntity, MetaMachine> metaMachine) {
        return new MachineBuilder<>(this, name, MachineDefinition::createDefinition, metaMachine,
                MetaMachineBlock::new, MetaMachineItem::new, MetaMachineBlockEntity::createBlockEntity);
    }
}
