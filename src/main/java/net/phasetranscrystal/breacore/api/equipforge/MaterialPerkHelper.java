package net.phasetranscrystal.breacore.api.equipforge;

import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.material.attributes.AttributeType;
import net.phasetranscrystal.breacore.api.perk.PerkStack;

import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;

public class MaterialPerkHelper {

    public static Map<Identifier, Double> extractBaseValues(Material material, AttributeType<? extends IForgingProperty> forgingType) {
        IForgingProperty forging = material.getAttributeSet().getAttribute(forgingType);
        if (forging == null) {
            return Map.of();
        }
        return forging.getBaseValues();
    }

    public static Map<Identifier, List<PerkStack>> extractPerks(Material material, AttributeType<? extends IForgingProperty> forgingType) {
        IForgingProperty forging = material.getAttributeSet().getAttribute(forgingType);
        if (forging == null) {
            return Map.of();
        }
        return forging.getPerks();
    }

    public static List<PerkStack> extractPerkList(Material material, AttributeType<? extends IForgingProperty> forgingType) {
        Map<Identifier, List<PerkStack>> perks = extractPerks(material, forgingType);
        return perks.values().stream()
                .flatMap(List::stream)
                .toList();
    }
}
