package net.ptcrys.breakdown.api.machine.builder;

import net.ptcrys.breakdown.api.block.IMachineBlock;
import net.ptcrys.breakdown.api.blockentity.IMachineBlockEntity;
import net.ptcrys.breakdown.api.blockentity.MetaMachineBlockEntity;
import net.ptcrys.breakdown.api.gui.EditableMachineUI;
import net.ptcrys.breakdown.api.item.MetaMachineItem;
import net.ptcrys.breakdown.api.machine.MachineDefinition;
import net.ptcrys.breakdown.api.machine.MetaMachine;
import net.ptcrys.breakdown.api.registry.BreaRegistries;
import net.ptcrys.breakdown.common.data.BreaTags;
import net.ptcrys.breakdown.config.ConfigHolder;
import net.ptcrys.breakdown.utils.RotationState;
import net.ptcrys.registrylib.RegistryCore;
import net.ptcrys.registrylib.annotations.StandardAPI;
import net.ptcrys.registrylib.annotations.SyntaxSugar;
import net.ptcrys.registrylib.builders.BlockBuilder;
import net.ptcrys.registrylib.builders.ItemBuilder;
import net.ptcrys.registrylib.tooltip.SubNode;
import net.ptcrys.registrylib.tooltip.TooltipNodeCollector;
import net.ptcrys.registrylib.util.entry.BlockEntry;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.*;

@Accessors(chain = true, fluent = true)
public class MachineBuilder<DEFINITION extends MachineDefinition> {

    protected final RegistryCore registrate;
    protected final String name;
    protected final BiFunction<BlockBehaviour.Properties, DEFINITION, IMachineBlock> blockFactory;
    protected final BiFunction<IMachineBlock, Item.Properties, MetaMachineItem> itemFactory;
    protected final TriFunction<BlockEntityType<?>, BlockPos, BlockState, IMachineBlockEntity> blockEntityFactory;
    @Setter // non-final for KJS
    protected Function<Identifier, DEFINITION> definition;
    @Setter // non-final for KJS
    protected Function<IMachineBlockEntity, MetaMachine> machine;
    @Setter
    private VoxelShape shape = Shapes.block();
    @Setter
    private RotationState rotationState = RotationState.NON_Y_AXIS;
    /**
     * Whether this machine can be rotated or face upwards.
     * todo: set to true by default if we manage to rotate the model accordingly
     */
    @Setter
    private boolean allowExtendedFacing = false;
    @Setter
    private boolean hasTESR;
    @Setter
    private boolean renderMultiblockWorldPreview = true;
    @Setter
    private boolean renderMultiblockXEIPreview = true;
    @Setter
    private UnaryOperator<BlockBehaviour.Properties> blockProp = p -> p;
    @Setter
    private UnaryOperator<Item.Properties> itemProp = p -> p;
    @Setter
    @Nullable
    private Consumer<BlockBuilder<? extends Block, ?>> blockBuilder;
    @Setter
    @Nullable
    private Consumer<ItemBuilder<? extends MetaMachineItem, ?>> itemBuilder;
    @Setter
    private Consumer<BlockEntityType<BlockEntity>> onBlockEntityRegister = MetaMachineBlockEntity::onBlockEntityRegister;
    @Setter
    private int paintingColor = Long.decode(ConfigHolder.INSTANCE.client.defaultPaintingColor).intValue();
    @Setter
    private BiFunction<ItemStack, Integer, Integer> itemColor = ((itemStack, tintIndex) -> tintIndex == 1 ? paintingColor : -1);
    // private PartAbility[] abilities = new PartAbility[0];
    private @Nullable ArrayList<TooltipNodeCollector.TooltipConfig> tooltipConfigs;
    @Setter
    @Nullable
    private BiConsumer<ItemStack, Consumer<Component>> tooltipBuilder;
    @Setter
    private boolean alwaysTryModifyRecipe;
    @Getter
    @Setter
    private boolean regressWhenWaiting = true;
    @Setter
    @Nullable
    private Supplier<BlockState> appearance;
    @Getter // getter for KJS
    @Setter
    @Nullable
    private EditableMachineUI editableUI;
    @Getter // getter for KJS
    @Setter
    @Nullable
    private String langValue = null;

    public MachineBuilder(RegistryCore registrate, String name,
                          Function<Identifier, DEFINITION> definition,
                          Function<IMachineBlockEntity, MetaMachine> machine,
                          BiFunction<BlockBehaviour.Properties, DEFINITION, IMachineBlock> blockFactory,
                          BiFunction<IMachineBlock, Item.Properties, MetaMachineItem> itemFactory,
                          TriFunction<BlockEntityType<?>, BlockPos, BlockState, IMachineBlockEntity> blockEntityFactory) {
        this.registrate = registrate;
        this.name = name;
        this.machine = machine;
        this.blockFactory = blockFactory;
        this.itemFactory = itemFactory;
        this.blockEntityFactory = blockEntityFactory;
        this.definition = definition;
    }

    public MachineBuilder<DEFINITION> appearanceBlock(Supplier<? extends Block> block) {
        appearance = () -> block.get().defaultBlockState();
        return this;
    }

    public MachineBuilder<DEFINITION> multiblockPreviewRenderer(boolean multiBlockWorldPreview,
                                                                boolean multiBlockXEIPreview) {
        this.renderMultiblockWorldPreview = multiBlockWorldPreview;
        this.renderMultiblockXEIPreview = multiBlockXEIPreview;
        return this;
    }

    @StandardAPI
    public MachineBuilder<DEFINITION> addTooltip(TooltipNodeCollector.@NotNull TooltipConfig config) {
        if (this.tooltipConfigs == null) {
            this.tooltipConfigs = new ArrayList<>();
        }

        this.tooltipConfigs.add(config);
        return this;
    }

    @SyntaxSugar("addTooltip((collector, stack) -> collector.node(new SubNode.Basic(component, 0)))")
    public MachineBuilder<DEFINITION> addTooltip(@NotNull Component component) {
        this.addTooltip((TooltipNodeCollector.TooltipConfig) ((collector, stack) -> collector.node(new SubNode.Basic(component, 0))));
        return this;
    }

    protected DEFINITION createDefinition() {
        return definition.apply(Identifier.fromNamespaceAndPath(registrate.getModid(), name));
    }

    public @NotNull DEFINITION register() {
        var definition = createDefinition();

        var blockBuilder = BlockBuilderWrapper.makeBlockBuilder(this, definition);
        if (this.langValue != null) {
            blockBuilder.lang(langValue);
            definition.setLangValue(langValue);
        }
        if (this.blockBuilder != null) {
            this.blockBuilder.accept(blockBuilder);
        }
        var block = blockBuilder.register();

        var itemBuilder = ItemBuilderWrapper.makeItemBuilder(this, block);
        if (this.itemBuilder != null) {
            this.itemBuilder.accept(itemBuilder);
        }
        var item = itemBuilder.register();

        var blockEntityBuilder = registrate
                .blockEntity(name, (type, pos, state) -> blockEntityFactory.apply(type, pos, state).self())
                .onRegister(onBlockEntityRegister)
                .validBlock(block);
        var blockEntity = blockEntityBuilder.register();
        // definition.setRecipeTypes(recipeTypes);
        definition.setBlockSupplier(block);
        definition.setItemSupplier(item);
        // definition.setRecipeOutputLimits(recipeOutputLimits);
        definition.setBlockEntityTypeSupplier(blockEntity::get);
        definition.setMachineSupplier(machine);
        definition.setRegressWhenWaiting(this.regressWhenWaiting);
        if (appearance == null) {
            appearance = block::getDefaultState;
        }
        if (editableUI != null) {
            definition.setEditableUI(editableUI);
        }
        definition.setAppearance(appearance);
        definition.setAllowExtendedFacing(allowExtendedFacing);
        // definition.setRenderer(BreaUtil.isClientSide() ? renderer.get() : IRenderer.EMPTY);
        definition.setShape(shape);
        definition.setDefaultPaintingColor(paintingColor);
        definition.setRenderXEIPreview(renderMultiblockXEIPreview);
        definition.setRenderWorldPreview(renderMultiblockWorldPreview);
        BreaRegistries.MACHINE.register(definition.getId(), definition);
        return definition;
    }

    static class BlockBuilderWrapper {

        @SuppressWarnings("removal")
        public static <D extends MachineDefinition> BlockBuilder<Block, MachineBuilder<D>> makeBlockBuilder(MachineBuilder<D> owner, D definition) {
            return owner.registrate.block(owner, owner.name, properties -> {
                RotationState.set(owner.rotationState);
                MachineDefinition.setBuilt(definition);
                var b = owner.blockFactory.apply(properties, definition);
                RotationState.clear();
                MachineDefinition.clearBuilt();
                return b.self();
            })
                    .initialProperties(() -> Blocks.DISPENSER)
                    .addTag(BreaTags.MACHINE_BLOCK)
                    .properties(BlockBehaviour.Properties::noLootTable)
                    .properties(owner.blockProp);
        }
    }

    static class ItemBuilderWrapper {

        public static <D extends MachineDefinition> ItemBuilder<MetaMachineItem, MachineBuilder<D>> makeItemBuilder(MachineBuilder<D> owner,
                                                                                                                    BlockEntry<Block> block) {
            var builder = owner.registrate.item(owner, owner.name, properties -> owner.itemFactory.apply((IMachineBlock) block.get(), properties), false)
                    .properties(owner.itemProp);
            if (owner.tooltipConfigs != null) {
                owner.tooltipConfigs.forEach(builder::addTooltip);
            }
            return builder;
        }
    }
}
