package net.ptcrys.breakdown.api.ore;

import net.ptcrys.breakdown.api.BreakdownApiPlugin;
import net.ptcrys.breakdown.api.ore.common.display.FeatureVeinDisplay;
import net.ptcrys.breakdown.api.ore.common.display.GridVeinDisplay;
import net.ptcrys.topo.api.api.builtin.OreDomainRegistration;
import net.ptcrys.topo.api.ore.display.OreVeinDisplay;

/**
 * JEI display plugs keyed by placement-mode id. Registered after modes so handles resolve; bootstrap
 * requires every mode has a display.
 */
public final class BuiltinBdOreVeinDisplays {

    private static final OreDomainRegistration ORE = BreakdownApiPlugin.INSTANCE.ore();

    public static final OreVeinDisplay FEATURE = ORE.display(BuiltinBdOreModes.FEATURE.id(), FeatureVeinDisplay.INSTANCE);
    public static final OreVeinDisplay GRID = ORE.display(BuiltinBdOreModes.GRID.id(), GridVeinDisplay.INSTANCE);

    private BuiltinBdOreVeinDisplays() {}

    public static void init() {}
}
