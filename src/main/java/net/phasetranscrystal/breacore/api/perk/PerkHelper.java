package net.phasetranscrystal.breacore.api.perk;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class PerkHelper {

    public static Map<Perk, Float> mergePerkStacks(Collection<List<PerkStack>> stacksCollection) {
        Map<Perk, List<Float>> levelsByPerk = new HashMap<>();

        for (List<PerkStack> stacks : stacksCollection) {
            if (stacks == null) continue;
            for (PerkStack perkStack : stacks) {
                levelsByPerk.computeIfAbsent(perkStack.perk(), k -> new ArrayList<>())
                        .add(perkStack.level());
            }
        }

        Map<Perk, Float> result = new HashMap<>();
        for (Map.Entry<Perk, List<Float>> entry : levelsByPerk.entrySet()) {
            result.put(entry.getKey(), entry.getKey().calculateLevel(entry.getValue()));
        }
        return result;
    }

    public static Map<Perk, Float> collectPerkStacks(ItemStack stack, EquipmentSlot slot) {
        if (stack.isEmpty()) {
            return Map.of();
        }

        Collection<IPerkProvider> providers = getPerkProviders(stack);
        return collectPerkStacksFromProviders(providers, slot);
    }

    public static Collection<IPerkProvider> getPerkProviders(ItemStack stack) {
        if (stack.isEmpty()) {
            return List.of();
        }
        return stack.getComponents().stream()
                .filter(c -> c.value() instanceof IPerkProvider)
                .map(c -> (IPerkProvider) c.value())
                .toList();
    }

    public static Map<Perk, Float> collectPerkStacksFromProviders(Collection<IPerkProvider> providers, EquipmentSlot slot) {
        Map<Perk, List<Float>> levelsByPerk = new HashMap<>();

        for (IPerkProvider provider : providers) {
            Map<EquipmentSlotGroup, List<PerkStack>> providerStacks = provider.getPerkStacks();
            for (Map.Entry<EquipmentSlotGroup, List<PerkStack>> entry : providerStacks.entrySet()) {
                if (entry.getKey().test(slot)) {
                    for (PerkStack perkStack : entry.getValue()) {
                        levelsByPerk.computeIfAbsent(perkStack.perk(), k -> new ArrayList<>())
                                .add(perkStack.level());
                    }
                }
            }
        }

        Map<Perk, Float> result = new HashMap<>();
        for (Map.Entry<Perk, List<Float>> entry : levelsByPerk.entrySet()) {
            result.put(entry.getKey(), entry.getKey().calculateLevel(entry.getValue()));
        }
        return result;
    }

    public static PerkInfo getPerkInfo(LivingEntity entity, Perk perk) {
        PerkAttachment attachment = entity.getExistingDataOrNull(PerkAttachment.TYPE);
        if (attachment == null) {
            return null;
        }
        return attachment.getPerkInfos().get(perk);
    }

    public static float getPerkLevel(LivingEntity entity, Perk perk) {
        PerkAttachment attachment = entity.getExistingDataOrNull(PerkAttachment.TYPE);
        if (attachment == null) {
            return 0f;
        }
        return attachment.getPerkLevels().getOrDefault(perk, 0f);
    }

    public static Map<Perk, Float> getAllPerkLevels(LivingEntity entity) {
        PerkAttachment attachment = entity.getExistingDataOrNull(PerkAttachment.TYPE);
        if (attachment == null) {
            return Map.of();
        }
        return new HashMap<>(attachment.getPerkLevels());
    }

    public static Map<Perk, PerkInfo> getAllPerkInfos(LivingEntity entity) {
        PerkAttachment attachment = entity.getExistingDataOrNull(PerkAttachment.TYPE);
        if (attachment == null) {
            return Map.of();
        }
        return new HashMap<>(attachment.getPerkInfos());
    }
}
