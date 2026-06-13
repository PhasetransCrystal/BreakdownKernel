package net.phasetranscrystal.breacore.api.registry.registrate;

import net.phasetranscrystal.breacore.api.registry.registrate.builder.ModBlockBuilder;
import net.phasetranscrystal.breacore.api.registry.registrate.builder.ModEntityBuilder;
import net.phasetranscrystal.breacore.api.registry.registrate.builder.ModFluidBuilder;
import net.phasetranscrystal.breacore.api.registry.registrate.builder.ModItemBuilder;
import net.phasetranscrystal.registrylib.RegistryCore;
import net.phasetranscrystal.registrylib.builders.FluidBuilder;
import net.phasetranscrystal.registrylib.composite.ComponentItem;
import net.phasetranscrystal.registrylib.composite.IComponentItem;
import net.phasetranscrystal.registrylib.datagen.ProviderType;
import net.phasetranscrystal.registrylib.datagen.provider.RegistryLibLangProvider;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import org.jetbrains.annotations.NotNull;

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
    public <T extends Block, P> ModBlockBuilder<T, P> block(
                                                            @NotNull P parent,
                                                            @NotNull String name,
                                                            @NotNull Function<BlockBehaviour.Properties, T> factory) {
        return ModBlockBuilder.create(this, parent, name, factory);
    }

    @Override
    public <T extends Item> ModItemBuilder<T, RegistryCore> item(
                                                                 @NotNull String name, @NotNull Function<Item.Properties, T> factory) {
        return item(this, name, factory, false);
    }

    @Override
    public ModItemBuilder<Item, RegistryCore> item(@NotNull String name) {
        return item(this, name, Item::new, false);
    }

    @Override
    public <T extends Item & IComponentItem<T>> ModItemBuilder<T, RegistryCore> componentItem(
                                                                                              @NotNull String name, @NotNull Function<Item.Properties, T> factory) {
        return item(this, name, factory, true);
    }

    @Override
    public ModItemBuilder<ComponentItem, RegistryCore> componentItem(@NotNull String name) {
        return componentItem(name, ComponentItem::new);
    }

    @Override
    public <T extends Item, P> ModItemBuilder<T, P> item(
                                                         @NotNull P parent,
                                                         @NotNull String name,
                                                         @NotNull Function<Item.Properties, T> factory,
                                                         boolean isComponentItem) {
        return ModItemBuilder.create(this, parent, name, factory, isComponentItem);
    }

    @Override
    public <T extends BaseFlowingFluid, P> ModFluidBuilder<T, P> fluid(
                                                                       @NotNull P parent,
                                                                       @NotNull String name,
                                                                       @NotNull Identifier stillTexture,
                                                                       @NotNull Identifier flowingTexture,
                                                                       @NotNull FluidBuilder.FluidFactory<T> fluidFactory) {
        return (ModFluidBuilder<T, P>) super.fluid(parent, name, stillTexture, flowingTexture, fluidFactory);
    }

    // ── Builder hooks ────────────────────────────────────────────────────────

    @Override
    public <T extends Entity> ModEntityBuilder<T, RegistryCore> entity(
                                                                       @NotNull String name,
                                                                       @NotNull EntityType.EntityFactory<T> factory,
                                                                       @NotNull MobCategory category) {
        return entity(this, name, factory, category);
    }

    @Override
    public <T extends Entity, P> ModEntityBuilder<T, P> entity(
                                                               @NotNull P parent,
                                                               @NotNull String name,
                                                               @NotNull EntityType.EntityFactory<T> factory,
                                                               @NotNull MobCategory category) {
        return ModEntityBuilder.create(this, parent, name, factory, category);
    }

    @Override
    protected <T extends BaseFlowingFluid, P> FluidBuilder<T, P> newFluidBuilder(
                                                                                 @NotNull P parent, @NotNull String name, @NotNull FluidBuilder.FluidFactory<T> fluidFactory) {
        return ModFluidBuilder.create(this, parent, name, fluidFactory);
    }

    public SoundEntryBuilder sound(String name) {
        return new SoundEntryBuilder(Identifier.fromNamespaceAndPath(getModid(), name));
    }

    public SoundEntryBuilder sound(Identifier name) {
        return new SoundEntryBuilder(name);
    }

    /*
     * public <DEFINITION extends MachineDefinition> MachineBuilder<DEFINITION> machine(String name,
     * Function<Identifier, DEFINITION> definitionFactory,
     * Function<IMachineBlockEntity, MetaMachine> metaMachine,
     * BiFunction<BlockBehaviour.Properties, DEFINITION, IMachineBlock> blockFactory,
     * BiFunction<IMachineBlock, Item.Properties, MetaMachineItem> itemFactory,
     * TriFunction<BlockEntityType<?>, BlockPos, BlockState, IMachineBlockEntity> blockEntityFactory) {
     * return new MachineBuilder<>(this, name, definitionFactory, metaMachine, blockFactory, itemFactory,
     * blockEntityFactory);
     * }
     *
     * public MachineBuilder<MachineDefinition> machine(String name,
     * Function<IMachineBlockEntity, MetaMachine> metaMachine) {
     * return new MachineBuilder<>(this, name, MachineDefinition::createDefinition, metaMachine,
     * MetaMachineBlock::new, MetaMachineItem::new, MetaMachineBlockEntity::createBlockEntity);
     * }
     */
}
