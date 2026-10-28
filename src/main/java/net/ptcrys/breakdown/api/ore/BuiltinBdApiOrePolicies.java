package net.ptcrys.breakdown.api.ore;

import net.ptcrys.breakdown.api.BreakdownApiPlugin;
import net.ptcrys.topo.api.api.builtin.OreDomainRegistration;
import net.ptcrys.topo.api.ore.policy.OreAirExposurePolicy;
import net.ptcrys.topo.api.ore.policy.OreConflictPolicy;

import net.minecraft.network.chat.Component;

/** Built-in air-exposure and overlap-conflict policies, including JEI display contributions. */
public final class BuiltinBdApiOrePolicies {

    private static final OreDomainRegistration ORE = BreakdownApiPlugin.INSTANCE.ore();

    public static final OreAirExposurePolicy ALLOW = ORE.airExposure("allow",
            new OreAirExposurePolicy.Strategy() {

                @Override
                public boolean discardExposed(double discardChance, double randomSample) {
                    return false;
                }

                @Override
                public Component describe(double discardChance) {
                    return BuiltinBdApiOreLang.EXPOSED_VISIBLE.getComponent();
                }
            });

    public static final OreAirExposurePolicy DISCARD = ORE.airExposure("discard",
            new OreAirExposurePolicy.Strategy() {

                @Override
                public boolean discardExposed(double discardChance, double randomSample) {
                    return true;
                }

                @Override
                public Component describe(double discardChance) {
                    return BuiltinBdApiOreLang.EXPOSED_HIDDEN.getComponent();
                }
            });

    public static final OreAirExposurePolicy PROBABILISTIC_DISCARD = ORE.airExposure("probabilistic_discard",
            new OreAirExposurePolicy.Strategy() {

                @Override
                public boolean discardExposed(double discardChance, double randomSample) {
                    return randomSample < discardChance;
                }

                @Override
                public Component describe(double discardChance) {
                    int percent = (int) Math.round((1.0 - discardChance) * 100.0);
                    return BuiltinBdApiOreLang.EXPOSED_PARTIAL.getComponent(percent + "%");
                }
            });

    public static final OreConflictPolicy PRESERVE_HIGHER_PRIORITY = ORE.conflict("preserve_higher_priority",
            (candidatePriority, existingPriority) -> false);

    public static final OreConflictPolicy REPLACE_LOWER_PRIORITY = ORE.conflict("replace_lower_priority",
            (candidatePriority, existingPriority) -> candidatePriority >= existingPriority);

    private BuiltinBdApiOrePolicies() {}

    public static void init() {}
}
