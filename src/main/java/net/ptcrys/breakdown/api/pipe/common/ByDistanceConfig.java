package net.ptcrys.breakdown.api.pipe.common;

import net.ptcrys.topo.api.pipe.PipeDistributionStrategy;
import net.ptcrys.topo.api.pipe.PipePortStrategyConfig;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ByDistanceConfig(PipeDistributionStrategy strategy, DistanceOrder order, int amount, int interval) implements PipePortStrategyConfig {

    static MapCodec<ByDistanceConfig> codec(PipeDistributionStrategy strategy) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                DistanceOrder.CODEC.optionalFieldOf("order", DistanceOrder.NEAREST)
                        .forGetter(ByDistanceConfig::order),
                Codec.INT.fieldOf("amount").forGetter(ByDistanceConfig::amount),
                Codec.INT.fieldOf("interval").forGetter(ByDistanceConfig::interval)).apply(instance, (order, amount, interval) -> new ByDistanceConfig(strategy, order, amount, interval)));
    }

    public ByDistanceConfig withOrder(DistanceOrder newOrder) {
        return new ByDistanceConfig(strategy, newOrder, amount, interval);
    }

    @Override
    public int amountOr(int fallback) {
        return amount;
    }

    @Override
    public PipePortStrategyConfig withAmount(int newAmount) {
        return new ByDistanceConfig(strategy, order, newAmount, interval);
    }

    @Override
    public PipePortStrategyConfig withInterval(int newInterval) {
        return new ByDistanceConfig(strategy, order, amount, newInterval);
    }
}
