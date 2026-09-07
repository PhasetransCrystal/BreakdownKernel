package net.ptcrys.breakdown.data.ore;

import net.ptcrys.breakdown.api.ore.BuiltinBdApiOreEnvironments;
import net.ptcrys.breakdown.api.ore.BuiltinBdApiOreModes;
import net.ptcrys.breakdown.api.ore.BuiltinBdApiOrePolicies;
import net.ptcrys.breakdown.data.BreakdownKernalPlugin;
import net.ptcrys.topo.api.material.Material;
import net.ptcrys.topo.api.ore.OreEnvironment;
import net.ptcrys.topo.api.ore.OreVein;

public class BuiltinBdOreVeins {

    private static final int IRON_SCALE_ATTEMPTS = 20;

    private BuiltinBdOreVeins() {}

    public static void init() {}

    private static OreVein overworld(
                                     String id,
                                     String en,
                                     String cn,
                                     Material material,
                                     int minY,
                                     int maxY,
                                     int attempts,
                                     int size) {
        return featureVein(
                id,
                en,
                cn,
                BuiltinBdApiOreEnvironments.OVERWORLD_STONE_AND_DEEPSLATE,
                material,
                minY,
                maxY,
                attempts,
                size);
    }

    private static OreVein nether(
                                  String id,
                                  String en,
                                  String cn,
                                  Material material,
                                  int minY,
                                  int maxY,
                                  int attempts,
                                  int size) {
        return featureVein(
                id,
                en,
                cn,
                BuiltinBdApiOreEnvironments.NETHER_NETHERRACK,
                material,
                minY,
                maxY,
                attempts,
                size);
    }

    private static OreVein featureVein(
                                       String id,
                                       String en,
                                       String cn,
                                       OreEnvironment environment,
                                       Material material,
                                       int minY,
                                       int maxY,
                                       int attempts,
                                       int size) {
        return BreakdownKernalPlugin.INSTANCE.ore().vein(id)
                .lang(en, cn)
                .environment(environment)
                .airExposure(BuiltinBdApiOrePolicies.ALLOW, 0.0)
                .entry(material, 1)
                .feature(BuiltinBdApiOreModes.FEATURE, size, attempts)
                .triangularHeight(minY, maxY)
                .build();
    }

    private static int scale(double ironMultiplier) {
        return Math.max(1, (int) Math.round(IRON_SCALE_ATTEMPTS * ironMultiplier));
    }
}
