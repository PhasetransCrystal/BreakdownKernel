package net.ptcrys.breakdown.api.pipe.common;

import net.ptcrys.breakdown.api.pipe.BuiltinBdApiPipeLang;
import net.ptcrys.topo.api.machine.ui.MachineUiComponentStyle;
import net.ptcrys.topo.api.pipe.AggregationWindow;
import net.ptcrys.topo.api.pipe.PipeDefinition;
import net.ptcrys.topo.api.pipe.PipeDistributionContext;
import net.ptcrys.topo.api.pipe.PipePortStrategyConfig;
import net.ptcrys.topo.api.pipe.ui.PipePortAccess;
import net.ptcrys.topo.api.pipe.ui.PipePortUiCollector;

import net.minecraft.resources.Identifier;

import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.mojang.serialization.MapCodec;

public final class ByDistanceStrategy extends LangBoundStrategy {

    private final Identifier id;
    private final MapCodec<ByDistanceConfig> codec;

    public ByDistanceStrategy(Identifier id) {
        this.id = id;
        this.codec = ByDistanceConfig.codec(this);
    }

    @Override
    public Identifier id() {
        return id;
    }

    @Override
    public MapCodec<? extends PipePortStrategyConfig> configCodec() {
        return codec;
    }

    @Override
    public PipePortStrategyConfig initialPortConfig(PipeDefinition definition, AggregationWindow aggregation) {
        int interval = aggregation.initialInterval();
        return new ByDistanceConfig(
                this, DistanceOrder.NEAREST, definition.maxBatchAmount(interval), interval);
    }

    @Override
    public int budget(PipeDefinition definition, PipePortStrategyConfig config) {
        return config instanceof ByDistanceConfig byDistance ? byDistance.amount() : definition.maxBatchAmount(config.interval());
    }

    /** Greedy fill in distance order; FARTHEST simply walks the BFS-sorted order backwards. */
    @Override
    public void distribute(PipeDistributionContext context) {
        int count = context.destinationCount();
        if (count <= 0) {
            return;
        }
        boolean farthest = context.config() instanceof ByDistanceConfig byDistance && byDistance.order() == DistanceOrder.FARTHEST;
        for (int i = 0; i < count && context.budgetRemaining() > 0; i++) {
            context.transfer(farthest ? count - 1 - i : i, context.budgetRemaining());
        }
    }

    /**
     * One labeled settings row — "Order" caption left, the mutually exclusive
     * Nearest/Farthest pair right — matching the shell's row rhythm on the 176-wide screen.
     * Selection and edits share one hidden authoritative LDLib2 integer value.
     */
    @Override
    public void contributeConfigUi(PipePortUiCollector collector, PipePortAccess access) {
        DistanceOrder[] orders = DistanceOrder.values();
        Label[] labels = new Label[orders.length];
        var selectedOrder = access.bindInt(
                "topo_pipe_port_order_value",
                DistanceOrder.NEAREST.ordinal(),
                () -> access.currentConfig() instanceof ByDistanceConfig byDistance ? byDistance.order().ordinal() : DistanceOrder.NEAREST.ordinal(),
                selected -> {
                    if (selected < 0 || selected >= orders.length) {
                        return;
                    }
                    DistanceOrder order = orders[selected];
                    access.updateConfig(config -> config instanceof ByDistanceConfig byDistance ? byDistance.withOrder(order) : config);
                },
                selected -> {
                    for (int i = 0; i < labels.length; i++) {
                        int index = i;
                        labels[i].textStyle(style -> style.textColor(index == selected ? MachineUiComponentStyle.INSTANCE.getTextSelected() : MachineUiComponentStyle.INSTANCE.getTextMuted()));
                    }
                });
        var buttons = new UIElement[orders.length];
        for (DistanceOrder order : orders) {
            int ordinal = order.ordinal();
            buttons[ordinal] = collector.intChoiceButton(
                    "topo_pipe_port_order_" + order.getSerializedName(),
                    order.displayName(),
                    selectedOrder,
                    ordinal,
                    label -> labels[ordinal] = label);
        }
        collector.addRow(collector.labeledRow("topo_pipe_port_order_row",
                BuiltinBdApiPipeLang.UI_PIPE_PORT_DISTANCE_ORDER.getComponent(), buttons));
    }
}
