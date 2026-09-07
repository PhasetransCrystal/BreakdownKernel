package net.ptcrys.breakdown.api.ore;

import net.ptcrys.breakdown.api.BreakdownApiPlugin;
import net.ptcrys.breakdown.api.ore.common.mode.FeatureVeinMode;
import net.ptcrys.breakdown.api.ore.common.mode.GridVeinMode;
import net.ptcrys.topo.api.api.builtin.OreDomainRegistration;
import net.ptcrys.topo.api.ore.mode.OreVeinMode;

/** Built-in placement channels: vanilla feature scatter and deterministic grid. */
public final class BuiltinBdApiOreModes {

    private static final OreDomainRegistration ORE = BreakdownApiPlugin.INSTANCE.ore();

    public static final OreVeinMode FEATURE = ORE.mode("feature", FeatureVeinMode.INSTANCE);
    public static final OreVeinMode GRID = ORE.mode("grid", GridVeinMode.INSTANCE);

    private BuiltinBdApiOreModes() {}

    public static void init() {}
}
