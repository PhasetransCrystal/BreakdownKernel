package net.phasetranscrystal.breacore.api.equipforge;

import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.perk.PerkStack;

import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;

public class MaterialPerkHelper {

    public static Map<Identifier, Double> extractBaseValues(Material material) {
        return Map.of();
    }

    public static Map<Identifier, List<PerkStack>> extractPerks(Material material) {
        return Map.of();
    }

    public static List<PerkStack> extractPerkList(Material material) {
        return List.of();
    }
}
