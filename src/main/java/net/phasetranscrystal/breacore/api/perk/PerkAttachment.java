package net.phasetranscrystal.breacore.api.perk;

import net.phasetranscrystal.brealib.BreaLib;

import net.phasetranscrystal.breacore.api.eventdispatch.EventConsumer;
import net.phasetranscrystal.breacore.api.eventdispatch.EventDispatcher;
import net.phasetranscrystal.breacore.api.perk.event.PerkChangeEvent;
import net.phasetranscrystal.breacore.common.data.BreaAttachmentTypes;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;

import java.util.*;
import java.util.stream.Collectors;

public class PerkAttachment {

    public static final AttachmentType<PerkAttachment> TYPE = BreaAttachmentTypes.PERK_CONTROLLER.get();
    public static final Identifier SYSTEM_ID = BreaLib.id("perk_system");

    private final Map<Perk, PerkInfo> perkInfos = new HashMap<>();
    private final Map<Perk, Float> perkLevels = new HashMap<>();
    private final Map<EquipmentSlot, ItemStack> currentEquipment = new EnumMap<>(EquipmentSlot.class);
    private final Map<EquipmentSlot, Map<Perk, Float>> slotPerkCache = new EnumMap<>(EquipmentSlot.class);
    private final Map<Perk, Map<Identifier, Holder<Attribute>>> perkAttributeModifiers = new HashMap<>();

    public static PerkAttachment getOrCreate(LivingEntity entity) {
        return entity.getData(TYPE);
    }

    public static PerkInfo getPerkInfo(LivingEntity entity, Perk perk) {
        PerkAttachment attachment = entity.getExistingDataOrNull(TYPE);
        if (attachment == null) {
            return null;
        }
        return attachment.perkInfos.get(perk);
    }

    public static float getPerkLevel(LivingEntity entity, Perk perk) {
        PerkAttachment attachment = entity.getExistingDataOrNull(TYPE);
        if (attachment == null) {
            return 0f;
        }
        return attachment.perkLevels.getOrDefault(perk, 0f);
    }

    public static Map<Perk, Float> getAllPerkLevels(LivingEntity entity) {
        PerkAttachment attachment = entity.getExistingDataOrNull(TYPE);
        if (attachment == null) {
            return Map.of();
        }
        return new HashMap<>(attachment.perkLevels);
    }

    public static Map<Perk, Float> collectPerkStacks(ItemStack stack, EquipmentSlot slot) {
        if (stack.isEmpty()) {
            return Map.of();
        }

        Map<Perk, List<Float>> levelsByPerk = new HashMap<>();
        Collection<IPerkProvider> providers = getPerkProviders(stack);

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

    private static Collection<IPerkProvider> getPerkProviders(ItemStack stack) {
        return stack.getComponents().stream()
                .filter(c -> c.value() instanceof IPerkProvider)
                .map(c -> (IPerkProvider) c.value())
                .toList();
    }

    public void updateEquipment(LivingEntity entity, EquipmentSlot slot, ItemStack newStack, Map<Perk, Float> oldPerkStacks, Map<Perk, Float> newPerkStacks) {
        currentEquipment.put(slot, newStack);
        slotPerkCache.put(slot, newPerkStacks);

        updatePerks(entity, oldPerkStacks, newPerkStacks);
    }

    private void updatePerks(LivingEntity entity, Map<Perk, Float> oldStacks, Map<Perk, Float> newStacks) {
        Set<Perk> affectedPerks = new HashSet<>(oldStacks.keySet());
        affectedPerks.addAll(newStacks.keySet());

        for (Perk perk : affectedPerks) {
            List<Float> levels = slotPerkCache.values().stream()
                    .map(slotPerks -> slotPerks.get(perk))
                    .filter(Objects::nonNull)
                    .toList();

            float newLevel = perk.calculateLevel(levels);
            boolean hadPerk = perkLevels.containsKey(perk);

            if (!hadPerk) {
                addPerk(entity, perk, newLevel);
            } else {
                float oldLevel = perkLevels.get(perk);

                boolean hasLevel = !levels.isEmpty();

                if (!hasLevel) {
                    removePerk(entity, perk, oldLevel);
                } else if (oldLevel != newLevel) {
                    changePerkLevel(entity, perk, oldLevel, newLevel);
                }
            }
        }
    }

    private PerkInfo createPerkInfo(Perk perk, float level) {
        Map<EquipmentSlot, ItemStack> stacksMap = new EnumMap<>(EquipmentSlot.class);

        for (Map.Entry<EquipmentSlot, Map<Perk, Float>> entry : slotPerkCache.entrySet()) {
            if (entry.getValue().containsKey(perk)) {
                stacksMap.put(entry.getKey(), currentEquipment.get(entry.getKey()));
            }
        }

        return new PerkInfo(perk, level, stacksMap);
    }

    private void addPerk(LivingEntity entity, Perk perk, float level) {
        perkLevels.put(perk, level);
        PerkInfo info = createPerkInfo(perk, level);
        perkInfos.put(perk, info);

        perk.onAttached(entity, info);
        registerEventConsumers(entity, perk, info);
        updateAttributeModifiers(entity, perk, 0f, level);

        NeoForge.EVENT_BUS.post(new PerkChangeEvent(entity, perk, PerkChangeType.ADD, 0f, level, info));
    }

    private void removePerk(LivingEntity entity, Perk perk, float oldLevel) {
        PerkInfo info = perkInfos.get(perk);

        perk.onDetached(entity, info);
        unregisterEventConsumers(entity, perk);
        removeAttributeModifiers(entity, perk);

        perkLevels.remove(perk);
        perkInfos.remove(perk);

        NeoForge.EVENT_BUS.post(new PerkChangeEvent(entity, perk, PerkChangeType.REMOVE, oldLevel, 0f, info));
    }

    private void changePerkLevel(LivingEntity entity, Perk perk, float oldLevel, float newLevel) {
        perkLevels.put(perk, newLevel);
        PerkInfo info = createPerkInfo(perk, newLevel);
        perkInfos.put(perk, info);

        perk.onLevelChanged(entity, oldLevel, newLevel, info);
        updateAttributeModifiers(entity, perk, oldLevel, newLevel);

        NeoForge.EVENT_BUS.post(new PerkChangeEvent(entity, perk, PerkChangeType.CHANGE, oldLevel, newLevel, info));
    }

    private void registerEventConsumers(LivingEntity entity, Perk perk, PerkInfo info) {
        List<PerkConsumer<?>> perkConsumers = perk.getEventConsumers(info);

        Identifier perkId = perk.getId();
        Identifier systemId = BreaLib.id("perk_system");
        Identifier[] basePath = new Identifier[] { systemId, perkId };

        for (PerkConsumer<?> perkConsumer : perkConsumers) {
            EventConsumer<?> wrapped = createWrappedConsumer(entity, perkConsumer, perk, basePath);
            EventDispatcher.attach(entity, wrapped);
        }
    }

    private <T extends Event> EventConsumer<T> createWrappedConsumer(
                                                                     LivingEntity entity, PerkConsumer<T> perkConsumer, Perk perk, Identifier[] basePath) {
        return EventConsumer.of(
                perkConsumer.eventType(),
                basePath,
                perkConsumer.runWhenCancelled(),
                (event, consumer) -> {
                    PerkInfo currentInfo = perkInfos.get(perk);
                    if (currentInfo != null) {
                        perkConsumer.triConsumer().accept(event, consumer, currentInfo);
                    }
                });
    }

    private void unregisterEventConsumers(LivingEntity entity, Perk perk) {
        EventDispatcher.detachPath(entity, SYSTEM_ID, perk.getId());
    }

    private void updateAttributeModifiers(LivingEntity entity, Perk perk, float oldLevel, float newLevel) {
        removeAttributeModifiers(entity, perk);

        Collection<PerkAttributeModifier> modifiers = perk.getAttributeModifiers(entity, newLevel);
        Map<Identifier, Holder<Attribute>> modifierMap = new HashMap<>();

        for (PerkAttributeModifier perkModifier : modifiers) {
            AttributeInstance attributeInstance = entity.getAttribute(perkModifier.attribute());
            if (attributeInstance != null) {
                Identifier modifierId = perk.getAttributeModifierId(perkModifier.operation());
                modifierMap.put(modifierId, perkModifier.attribute());
                attributeInstance.addPermanentModifier(perkModifier.toModifier(modifierId));
            }
        }

        perkAttributeModifiers.put(perk, modifierMap);
    }

    private void removeAttributeModifiers(LivingEntity entity, Perk perk) {
        Map<Identifier, Holder<Attribute>> modifierMap = perkAttributeModifiers.remove(perk);
        if (modifierMap != null) {
            for (Map.Entry<Identifier, Holder<Attribute>> entry : modifierMap.entrySet()) {
                AttributeInstance attributeInstance = entity.getAttribute(entry.getValue());
                if (attributeInstance != null) {
                    attributeInstance.removeModifier(entry.getKey());
                }
            }
        }
    }

    public void recalculateAllPerks(LivingEntity entity) {
        currentEquipment.clear();
        slotPerkCache.clear();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = entity.getItemBySlot(slot);
            currentEquipment.put(slot, stack);
            slotPerkCache.put(slot, collectPerkStacks(stack, slot));
        }

        perkLevels.clear();
        perkInfos.keySet().forEach(perk -> {
            this.unregisterEventConsumers(entity, perk);
            this.removeAttributeModifiers(entity, perk);
        });
        perkInfos.clear();
        perkAttributeModifiers.clear();

        Set<Perk> allPerks = slotPerkCache.values().stream()
                .flatMap(m -> m.keySet().stream())
                .collect(Collectors.toSet());

        for (Perk perk : allPerks) {
            List<Float> levels = slotPerkCache.values().stream()
                    .map(slotPerks -> slotPerks.get(perk))
                    .filter(Objects::nonNull)
                    .toList();
            float level = perk.calculateLevel(levels);

            perkLevels.put(perk, level);
            PerkInfo info = createPerkInfo(perk, level);
            perkInfos.put(perk, info);

            perk.onAttached(entity, info);
            registerEventConsumers(entity, perk, info);
            updateAttributeModifiers(entity, perk, 0f, level);

            NeoForge.EVENT_BUS.post(new PerkChangeEvent(entity, perk, PerkChangeType.ADD, 0f, level, info));
        }
    }
}
