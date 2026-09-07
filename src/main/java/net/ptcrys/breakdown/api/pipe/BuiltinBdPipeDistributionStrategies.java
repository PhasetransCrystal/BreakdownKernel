package net.ptcrys.breakdown.api.pipe;

import net.ptcrys.breakdown.BreaLib;
import net.ptcrys.breakdown.api.BreakdownApiPlugin;
import net.ptcrys.breakdown.api.pipe.common.ByDistanceStrategy;
import net.ptcrys.breakdown.api.pipe.common.EqualSplitStrategy;
import net.ptcrys.breakdown.api.pipe.common.LangBoundStrategy;
import net.ptcrys.breakdown.api.pipe.common.RoundRobinStrategy;
import net.ptcrys.topo.api.api.builtin.LangDomainRegistration;
import net.ptcrys.topo.api.pipe.PipeDistributionStrategies;
import net.ptcrys.topo.api.pipe.PipeDistributionStrategy;

/**
 * Builtin pipe distribution strategies. Tier gating and per-(pipe x strategy) envelopes are
 * expressed at the pipe registration sites ({@code BuiltinTopoPipes}) through
 * {@code PipeStrategyOffer} aggregation windows, not here.
 *
 * <p>
 * All builtin configs persist the per-window batch {@code amount} (clamped into
 * {@code maxExtractRate × interval} by the runtime) and the aggregation {@code interval}
 * (clamped into the offer's {@code AggregationWindow}).
 */
public final class BuiltinBdPipeDistributionStrategies {

    private static final LangDomainRegistration LANG = BreakdownApiPlugin.INSTANCE.lang();

    public static final PipeDistributionStrategy BY_DISTANCE = register(new ByDistanceStrategy(BreaLib.id("by_distance")),
            "By Distance", "按距离",
            "DIST", "DIST",
            "Nearest or farthest first", "最近或最远优先");

    public static final PipeDistributionStrategy EQUAL_SPLIT = register(new EqualSplitStrategy(BreaLib.id("equal_split")),
            "Equal Split", "均分",
            "EQUL", "EQUL",
            "Even share per target", "每目标均分");

    public static final PipeDistributionStrategy ROUND_ROBIN = register(new RoundRobinStrategy(BreaLib.id("round_robin")),
            "Round Robin", "轮询",
            "ROBN", "ROBN",
            "Targets take turns", "目标轮流");

    private BuiltinBdPipeDistributionStrategies() {}

    /** Only activates class initialization. Must remain empty. */
    public static void init() {}

    private static <T extends LangBoundStrategy> T register(
                                                            T strategy,
                                                            String en,
                                                            String cn,
                                                            String shortEn,
                                                            String shortCn,
                                                            String descEn,
                                                            String descCn) {
        // Key minting lives only at registration (pipe.strategy.<ns>.<path>[.short|.desc]).
        String baseKey = strategy.id().toLanguageKey("pipe.strategy");
        strategy.bindLang(
                LANG.resource(strategy.id(), "pipe.strategy", en, cn),
                LANG.absolute(baseKey + ".desc", descEn, descCn),
                LANG.absolute(baseKey + ".short", shortEn, shortCn));
        PipeDistributionStrategies.register(strategy);
        return strategy;
    }
}
