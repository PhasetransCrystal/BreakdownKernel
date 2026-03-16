package net.phasetranscrystal.breacore.api.blockentity.debug;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
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

public class FluidFurnaceBlockEntity extends BlockEntity implements ISyncPersistRPCBlockEntity, ResourceHandler<FluidResource> {

    private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);
    @Persisted
    @DescSynced
    public final ItemStacksResourceHandler inventory = new ItemStacksResourceHandler(2);
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
     * 是否正在工作
     */
    @Persisted
    @DescSynced
    private boolean isWorking = false;
    /**
     * 每烧制一个物品需要的基础流体量 (原版岩浆1000mb烧100个物品，每个10mb)
     */
    private static final int FLUID_PER_ITEM = 10;
    /**
     * 烧制时间 (原版熔炉200tick)
     */
    public static final int SMELT_TIME = 200;
    private static final int FLUID_CAPACITY = 10000;
    /**
     * 当前剩余可烧制次数 (根据剩余流体计算)
     */
    @Persisted
    @DescSynced
    private int remainingSmelts = 0;
    /**
     * 当前配方
     */
    private RecipeHolder<?> currentRecipe;
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
    public void serverTick() {
        var inputResource = inventory.getResource(0);
        var outputResource = inventory.getResource(1);

        // 检查输入物品是否改变

        if (!inputResource.matches(cachedInput)) {
            currentRecipe = null;
            cachedInput = inputResource.toStack();
        }

        // 检查是否有有效的配方
        if (currentRecipe == null && !inputResource.isEmpty() && level != null) {
            currentRecipe = findSmeltingRecipe(inputResource.toStack());
        }

        // 如果有配方且有剩余烧制次数，开始烧制
        if (currentRecipe != null && remainingSmelts > 0) {
            var resultItem = ((AbstractCookingRecipe) currentRecipe.value()).assemble(new SingleRecipeInput(cachedInput));

            if (outputResource.isEmpty() || canMergeOutput(outputResource.toStack(inventory.getAmountAsInt(1)), resultItem)) {
                // 增加进度
                progress++;

                if (progress >= SMELT_TIME) {
                    // 烧制完成
                    completeSmelting(resultItem);
                }
                isWorking = true;
            } else {
                isWorking = false;
            }
        } else {
            isWorking = false;
            if (progress > 0) {
                progress = 0;
            }
        }
    }

    /**
     * 完成烧制
     */
    private void completeSmelting(ItemStack resultItem) {
        var inputResource = inventory.getResource(0);

        try (Transaction tx = Transaction.openRoot()) {
            inventory.insert(1, ItemResource.of(resultItem), resultItem.count(), tx);
            inventory.extract(0, inputResource, 1, tx);
            remainingSmelts--;
            fluidStack.extract(0, fluidStack.getResource(0), FLUID_PER_ITEM, tx);
            tx.commit();
        }
        progress = 0;
        currentRecipe = null;
        cachedInput = ItemStack.EMPTY;
    }

    /**
     * 查找烧制配方
     * 同时支持普通熔炉和高炉配方 (RAW IRON 等需要 BLASTING)
     */
    private RecipeHolder<?> findSmeltingRecipe(ItemStack stack) {
        if (level == null) return null;

        var recipeManager = getServerLevel().recipeAccess();
        var input = new SingleRecipeInput(stack);

        // 优先检查普通熔炉配方
        var smeltingRecipeHolder = recipeManager.getRecipeFor(RecipeType.SMELTING, input, level);
        if (smeltingRecipeHolder.isPresent())
            return smeltingRecipeHolder.get();
        var blastingRecipeHolder = recipeManager.getRecipeFor(RecipeType.BLASTING, input, level);
        return blastingRecipeHolder.orElse(null);
    }

    /**
     * 检查是否可以合并输出
     */
    private boolean canMergeOutput(ItemStack existing, ItemStack adding) {
        if (existing.isEmpty()) return true;
        if (!existing.is(adding.getItem())) return false;
        return existing.getCount() + adding.getCount() <= existing.getMaxStackSize();
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
                                                new ItemSlot().layout(layout -> layout.width(18).height(18))))
                        .setId("output_slot"),
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
                // fluidSlot.bind(this, 0);
            }
        });

        // 绑定输入槽
        ui.select("#input_slot").findFirst().ifPresent(element -> {
            if (element instanceof ItemSlot itemSlot) {
                // itemSlot.bind(inventory, 0);
            }
        });

        // 绑定输出槽
        ui.select("#output_slot").findFirst().ifPresent(element -> {
            if (element instanceof ItemSlot itemSlot) {
                // itemSlot.bind(inventory, 1);
            }
        });

        // 绑定进度条
        ui.select("#progress_bar").findFirst().ifPresent(element -> {
            if (element instanceof ProgressBar progressBar) {
                progressBar.setRange(0, SMELT_TIME)
                        .progressBarStyle(style -> style.fillDirection(FillDirection.LEFT_TO_RIGHT));
                progressBar.bindDataSource(SupplierDataSource.of(() -> progress));
            }
        });

        // 绑定状态标签
        ui.select("#status_label").findFirst().ifPresent(element -> {
            if (element instanceof Label label) {
                label.bindDataSource(SupplierDataSource.of(() -> {
                    if (isWorking) {
                        return Component.literal("Smelting... " + (int) (progress / SMELT_TIME * 100) + "%");
                    } else if (fluidStack.getResource(0).isEmpty()) {
                        return Component.literal("Need Fluid!");
                    } else if (inventory.getResource(0).isEmpty()) {
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
        remainingSmelts += additionalSmelts;
        return fluidStack.insert(index, resource, amount, transaction);
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        var fluidResource = fluidStack.getResource(index);
        if (fluidResource.isEmpty() || !resource.getFluid().isSame(fluidResource.getFluid())) return 0;
        int toDrain = Math.min(amount, fluidStack.getAmountAsInt(index));
        if (toDrain <= 0) return 0;
        int removedSmelts = toDrain / FLUID_PER_ITEM;
        remainingSmelts = Math.max(0, remainingSmelts - removedSmelts);
        return fluidStack.extract(index, resource, amount, transaction);
    }
}
