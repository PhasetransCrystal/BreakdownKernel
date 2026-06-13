package net.phasetranscrystal.breacore.common.blockentity.debug;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.SupplierDataSource;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.data.FillDirection;
import com.lowdragmc.lowdraglib2.gui.ui.elements.FluidSlot;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ProgressBar;
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.syncdata.holder.blockentity.ISyncPersistRPCBlockEntity;
import com.lowdragmc.lowdraglib2.syncdata.storage.FieldManagedStorage;
import com.lowdragmc.lowdraglib2.syncdata.storage.IManagedStorage;
import dev.vfyjxf.taffy.style.AlignContent;
import dev.vfyjxf.taffy.style.AlignItems;
import dev.vfyjxf.taffy.style.FlexDirection;

import java.util.Optional;

public class FluidFurnaceBlockEntity extends BlockEntity implements ISyncPersistRPCBlockEntity, ResourceHandler<FluidResource> {

    /**
     * 烧制时间 (原版熔炉200tick)
     */
    public static final int SMELT_TIME = 200;
    /**
     * 每烧制一个物品需要的基础流体量 (原版岩浆1000mb烧100个物品，每个10mb)
     */
    private static final int FLUID_PER_ITEM = 10;
    private static final int FLUID_CAPACITY = 10000;
    @Persisted
    @DescSynced
    public final ItemStacksResourceHandler input = new ItemStacksResourceHandler(1);
    @Persisted
    @DescSynced
    public final ItemStacksResourceHandler output = new ItemStacksResourceHandler(1);
    private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);
    @Persisted
    @DescSynced
    private final FluidStacksResourceHandler fluidStack = new FluidStacksResourceHandler(1, FLUID_CAPACITY);
    /**
     * 当前进度 (0-SMELT_TIME)
     */
    @Persisted
    @DescSynced
    private float progress = 0f;
    /**
     * 当前配方
     */
    private AbstractCookingRecipe currentRecipe;
    /**
     * 配方缓存的输入物品
     */
    private ItemStack cachedInput = ItemStack.EMPTY;

    public FluidFurnaceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    /**
     * 服务端每tick调用 - 处理烧制逻辑
     */
    public static void serverTick(Level level, BlockPos pos, BlockState state, FluidFurnaceBlockEntity be) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (be.fluidStack.getAmountAsInt(0) < FLUID_PER_ITEM) return;

        // 获取当前输入输出
        var inputResource = be.input.getResource(0);
        ItemStack inputStack = inputResource.toStack();
        SingleRecipeInput input = new SingleRecipeInput(inputStack);

        // 如果有当前配方，但输入不再匹配，则清空
        if (be.currentRecipe == null || !be.currentRecipe.matches(input, level)) {
            Optional<RecipeHolder<SmeltingRecipe>> smelting = serverLevel
                    .recipeAccess()
                    .getRecipeFor(RecipeType.SMELTING, input, serverLevel);
            if (smelting.isPresent()) {
                be.currentRecipe = smelting.get().value();
                be.progress = 0;
            } else {
                Optional<RecipeHolder<BlastingRecipe>> blasting = serverLevel
                        .recipeAccess()
                        .getRecipeFor(RecipeType.BLASTING, input, serverLevel);
                if (blasting.isEmpty()) {
                    be.progress = 0;
                    be.currentRecipe = null;
                    return;
                } else {
                    be.currentRecipe = blasting.get().value();
                    be.progress = 0;
                }
            }
        }

        be.progress += 2;
        if (be.progress >= be.currentRecipe.cookingTime()) {
            ItemStack result = be.currentRecipe.assemble(input);
            var outputResource = be.output.getResource(0);
            if (outputResource.isEmpty() || outputResource.matches(result)) {
                try (Transaction tx = Transaction.openRoot()) {
                    be.input.extract(inputResource, 1, tx);
                    ResourceHandlerUtil.insertStacking(be.output, ItemResource.of(result), result.count(), tx);
                    be.fluidStack.extract(0, be.fluidStack.getResource(0), FLUID_PER_ITEM, tx);
                    tx.commit();
                }
                be.progress = 0;
                be.currentRecipe = null;
                be.cachedInput = ItemStack.EMPTY;
            }
        }
        be.setChanged();
    }

    public ModularUI createUI(BlockUIMenuType.BlockUIHolder holder) {
        // 从XML加载UI
        // var xml = XmlUtils.loadXml(BreaLib.id("ui/fluid_furnace.xml"));
        // var ui = UI.of(xml);
        var root = new UIElement();
        root.addClass("panel_bg");
        root.layout(layout -> layout.height(176).width(166).paddingAll(8).flexDirection(FlexDirection.COLUMN).alignItems(AlignItems.CENTER));
        root.addChildren(
                new UIElement().layout(layout -> layout.height(6)),
                new UIElement().layout(layout -> layout.flexDirection(FlexDirection.ROW).alignItems(AlignItems.CENTER).justifyContent(AlignContent.CENTER).marginTop(8).marginBottom(6))
                        .addChildren(
                                new FluidSlot().layout(layout -> layout.width(20).height(48).marginRight(14)).setId("fluid_slot"),
                                new UIElement().layout(layout -> layout.flexDirection(FlexDirection.ROW).alignItems(AlignItems.CENTER).gapAll(4))
                                        .addChildren(
                                                new ItemSlot().layout(layout -> layout.width(18).height(18)).setId("input_slot"),
                                                new ProgressBar().setMaxValue(200).layout(layout -> layout.width(24).height(17).marginLeft(2).marginRight(2)).setId("progress_bar"),
                                                new ItemSlot().layout(layout -> layout.width(18).height(18)).setId("output_slot"))),
                new UIElement().layout(layout -> layout.height(12).alignItems(AlignItems.CENTER).justifyContent(AlignContent.CENTER).marginBottom(8))
                        .addChild(new Label().textStyle(style -> style.fontSize(10).textColor(0x505050)).setText("Ready").setId("status_label")),
                /*
                 * background-color: #8B8B8B;
                 * border-top: 1 solid #404040;
                 */
                new UIElement().layout(layout -> layout.width(162).height(1).marginTop(2).marginBottom(6)),
                new UIElement().layout(layout -> layout.marginTop(2))
                        .addChild(new InventorySlots()));
        var ui = UI.of(root);
        // 绑定流体槽
        ui.select("#fluid_slot").findFirst().ifPresent(element -> {
            if (element instanceof FluidSlot fluidSlot) {
                fluidSlot.setCapacity(FLUID_CAPACITY)
                        .slotStyle(style -> style.fillDirection(FillDirection.UP_TO_DOWN));
                fluidSlot.bind(this, 0);
            }
        });

        // 绑定输入槽
        ui.select("#input_slot").findFirst().ifPresent(element -> {
            if (element instanceof ItemSlot itemSlot) {
                itemSlot.bind(input, 0);
            }
        });

        // 绑定输出槽
        ui.select("#output_slot").findFirst().ifPresent(element -> {
            if (element instanceof ItemSlot itemSlot) {
                itemSlot.bind(output, 0);
            }
        });

        // 绑定进度条
        ui.select("#progress_bar").findFirst().ifPresent(element -> {
            if (element instanceof ProgressBar progressBar) {
                if (currentRecipe != null) {
                    progressBar.setRange(0, currentRecipe.cookingTime())
                            .progressBarStyle(style -> style.fillDirection(FillDirection.LEFT_TO_RIGHT));
                }
                progressBar.bindDataSource(SupplierDataSource.of(() -> progress));
            }
        });

        // 绑定状态标签
        ui.select("#status_label").findFirst().ifPresent(element -> {
            if (element instanceof Label label) {
                label.bindDataSource(SupplierDataSource.of(() -> {
                    if (fluidStack.getResource(0).isEmpty()) {
                        return Component.literal("Need Fluid!");
                    } else if (input.getResource(0).isEmpty()) {
                        return Component.literal("Place items to smelt");
                    } else if (currentRecipe == null) {
                        return Component.literal("No recipe for this item");
                    } else {
                        return Component.literal("Ready");
                    }
                }));
            }
        });

        return ModularUI.of(ui, holder.player);
    }

    @Override
    public IManagedStorage getSyncStorage() {
        return syncStorage;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public FluidResource getResource(int index) {
        return fluidStack.getResource(index);
    }

    @Override
    public long getAmountAsLong(int index) {
        return fluidStack.getAmountAsLong(index);
    }

    @Override
    public long getCapacityAsLong(int index, FluidResource resource) {
        return fluidStack.getCapacityAsLong(index, resource);
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return fluidStack.isValid(index, resource);
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        if (resource.isEmpty()) return 0;
        // 计算加入的流体可以烧制多少个物品
        int additionalSmelts = amount / FLUID_PER_ITEM;
        if (additionalSmelts <= 0) {
            return 0;
        }
        // 如果当前没有流体，接收任何流体；否则只接收同类型流体
        var fluidResource = fluidStack.getResource(index);
        if (!fluidResource.isEmpty() && !resource.getFluid().isSame(fluidResource.getFluid())) return 0;
        int space = fluidStack.getCapacityAsInt(index, fluidResource) - fluidStack.getAmountAsInt(index);
        int toFill = Math.min(amount, space);
        if (toFill <= 0) return 0;
        return fluidStack.insert(index, resource, amount, transaction);
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        var fluidResource = fluidStack.getResource(index);
        if (fluidResource.isEmpty() || !resource.getFluid().isSame(fluidResource.getFluid())) return 0;
        int toDrain = Math.min(amount, fluidStack.getAmountAsInt(index));
        if (toDrain <= 0) return 0;
        int removedSmelts = toDrain / FLUID_PER_ITEM;
        return fluidStack.extract(index, resource, amount, transaction);
    }
}
