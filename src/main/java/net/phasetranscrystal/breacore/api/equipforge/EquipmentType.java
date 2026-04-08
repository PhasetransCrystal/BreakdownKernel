package net.phasetranscrystal.breacore.api.equipforge;

import net.phasetranscrystal.breacore.api.attribute.DetailedAttributeModifier;
import net.phasetranscrystal.breacore.api.equipforge.data.GuiPosition;
import net.phasetranscrystal.breacore.api.equipforge.data.PartData;
import net.phasetranscrystal.breacore.api.equipforge.data.PartSlot;
import net.phasetranscrystal.breacore.api.perk.PerkHelper;
import net.phasetranscrystal.breacore.api.perk.PerkStack;
import net.phasetranscrystal.breacore.api.registry.BreaRegistries;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;

import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public abstract class EquipmentType {

    private final int gridWidth;
    private final int gridHeight;
    private final Map<Identifier, PartSlot> parts;

    public EquipmentType(int gridWidth, int gridHeight, Map<Identifier, PartSlot> parts) {
        if (gridWidth < 1 || gridHeight < 1) {
            throw new IllegalArgumentException("Grid dimensions must be at least 1x1");
        }

        Set<GuiPosition> seenPositions = new HashSet<>();
        boolean hasRequiredSlot = false;

        for (PartSlot slot : parts.values()) {
            GuiPosition pos = new GuiPosition(slot.guiX(), slot.guiY());

            if (pos.x() < 0 || pos.x() >= gridWidth || pos.y() < 0 || pos.y() >= gridHeight) {
                throw new IllegalArgumentException("Slot position (" + pos.x() + ", " + pos.y() + ") is out of grid bounds (" + gridWidth + "x" + gridHeight + ")");
            }

            if (seenPositions.contains(pos)) {
                throw new IllegalArgumentException("Slot position (" + pos.x() + ", " + pos.y() + ") is overlapping");
            }
            seenPositions.add(pos);

            if (slot.required()) {
                hasRequiredSlot = true;
            }
        }

        if (!hasRequiredSlot) {
            throw new IllegalArgumentException("At least one slot must be required");
        }

        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.parts = parts;
    }

    public Identifier getId() {
        return BreaRegistries.EQUIPMENT_TYPES.getKey(this);
    }

    public abstract Map<Identifier, Double> mergeValues(Map<Identifier, PartData> partsData);

    /**
     * 自动处理allowUnprocessedId的mergeValues方法。
     * 会查询处理前后的key变化，对allowUnprocessedId返回true的部件，
     * 将其未被处理的key值累加到结果中。
     *
     * @param partsData 部件数据
     * @return 处理后的数值Map
     */
    public Map<Identifier, Double> mergeValuesWithAutoUnprocessed(Map<Identifier, PartData> partsData) {
        Map<Identifier, Double> result = new HashMap<>(mergeValues(partsData));
        Set<Identifier> processedKeys = result.keySet();

        Map<Identifier, Double> passThroughValues = new HashMap<>();

        for (Map.Entry<Identifier, PartData> entry : partsData.entrySet()) {
            Identifier partId = entry.getKey();

            if (!allowUnprocessedId(partId)) {
                continue;
            }

            PartData partData = entry.getValue();
            Map<Identifier, Double> cachedValues = partData.getCachedValues();

            for (Map.Entry<Identifier, Double> valueEntry : cachedValues.entrySet()) {
                Identifier valueKey = valueEntry.getKey();
                if (!processedKeys.contains(valueKey)) {
                    passThroughValues.merge(valueKey, valueEntry.getValue(), Double::sum);
                }
            }
        }

        result.putAll(passThroughValues);
        return result;
    }

    /**
     * 将合并后的数值数据转换为AttributeModifier集合。
     *
     * @param mergedValues mergeValues返回的已合并数值
     * @return AttributeModifier集合
     */
    public abstract Collection<EquipAttributeModifier> convertToAttributeModifiers(Map<Identifier, Double> mergedValues);

    public EquipAttributeModifier withDefaultEquipSlot(Identifier id, DetailedAttributeModifier root) {
        return new EquipAttributeModifier(id, getEquipmentSlot(), root);
    }

    /**
     * 将合并后的数值应用到物品堆上，用于锻造时一次性修改物品数据。
     *
     * @param stack        目标物品堆
     * @param mergedValues mergeValues返回的已合并数值
     */
    public void applyToItemStack(ItemStack stack, Map<Identifier, Double> mergedValues) {}

    /**
     * 判断指定部件ID的数值是否允许未被处理(未在mergeValues中明确处理)时直接求和继承。
     * 当返回true时，未被mergeValues明确处理的Identifier将直接累加到最终数值中；
     * 返回false时，未被处理的Identifier将被丢弃。
     *
     * @param partId 部件的Identifier
     * @return 是否允许未处理的ID直接继承
     */
    public boolean allowUnprocessedId(Identifier partId) {
        return true;
    }

    public List<PerkStack> mergePerks(Map<Identifier, PartData> partsData) {
        List<List<PerkStack>> stacksCollection = partsData.values().stream()
                .map(PartData::getCachedPerks)
                .collect(Collectors.toList());

        Map<net.phasetranscrystal.breacore.api.perk.Perk, Float> merged = PerkHelper.mergePerkStacks(stacksCollection);
        return merged.entrySet().stream()
                .map(e -> new PerkStack(e.getKey(), e.getValue()))
                .toList();
    }

    public abstract EquipmentSlotGroup getEquipmentSlot();

    public static Builder builder() {
        return new Builder();
    }

    @Getter
    public static class Builder {

        private int gridWidth = 1;
        private int gridHeight = 1;
        private final Map<Identifier, PartSlot> parts = new LinkedHashMap<>();

        public Builder gridSize(int width, int height) {
            this.gridWidth = width;
            this.gridHeight = height;
            return this;
        }

        public Builder addPart(Identifier slotId, int guiX, int guiY, boolean required, PartType partType) {
            this.parts.put(slotId, new PartSlot(slotId, guiX, guiY, required, partType));
            return this;
        }

        public Builder addPart(Identifier slotId, GuiPosition pos, boolean required, PartType partType) {
            return addPart(slotId, pos.x(), pos.y(), required, partType);
        }
    }
}
