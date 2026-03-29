package net.phasetranscrystal.breacore.api.perk.client;

import net.phasetranscrystal.breacore.api.perk.IPerkProvider;
import net.phasetranscrystal.breacore.api.perk.Perk;
import net.phasetranscrystal.breacore.api.perk.PerkDisplayInfo;
import net.phasetranscrystal.breacore.api.perk.PerkStack;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PerkTooltipHelper {

    public static List<PerkTooltipEntry> getPerkEntries(ItemStack stack) {
        List<PerkTooltipEntry> entries = new ArrayList<>();
        Collection<IPerkProvider> providers = getPerkProviders(stack);

        for (IPerkProvider provider : providers) {
            Map<EquipmentSlotGroup, List<PerkStack>> providerStacks = provider.getPerkStacks();
            for (Map.Entry<EquipmentSlotGroup, List<PerkStack>> entry : providerStacks.entrySet()) {
                List<PerkStack> stacks = entry.getValue();

                for (PerkStack stackEntry : stacks) {
                    Perk perk = stackEntry.perk();
                    float level = stackEntry.level();
                    PerkDisplayInfo displayInfo = perk.getDisplayInfo();

                    entries.add(new PerkTooltipEntry(perk.getNameKey(), level, displayInfo.perkId()));
                }
            }
        }

        return entries;
    }

    private static Collection<IPerkProvider> getPerkProviders(ItemStack stack) {
        if (stack.isEmpty()) {
            return List.of();
        }
        return stack.getComponents().stream()
                .filter(c -> c.value() instanceof IPerkProvider)
                .map(c -> (IPerkProvider) c.value())
                .toList();
    }

    public record PerkTooltipEntry(
                                   String perkNameKey,
                                   float level,
                                   Identifier perkId) {}
}
