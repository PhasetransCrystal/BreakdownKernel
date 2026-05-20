package net.phasetranscrystal.breacore.api.equipforge.data;

import net.phasetranscrystal.breacore.api.equipforge.EquipAttributeModifier;
import net.phasetranscrystal.breacore.api.equipforge.EquipmentType;
import net.phasetranscrystal.breacore.api.perk.IPerkProvider;
import net.phasetranscrystal.breacore.api.perk.PerkStack;
import net.phasetranscrystal.breacore.api.registry.BreaRegistries;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EquipForgeData implements IPerkProvider {

    public static final Codec<EquipForgeData> CODEC = RecordCodecBuilder.create(i -> i.group(
            BreaRegistries.EQUIPMENT_TYPES.byNameCodec().fieldOf("type").forGetter(EquipForgeData::getEquipmentType),
            Codec.unboundedMap(Identifier.CODEC, PartData.CODEC).fieldOf("parts").forGetter(EquipForgeData::getParts),
            Codec.BOOL.fieldOf("broken").forGetter(EquipForgeData::isBroken)).apply(i, EquipForgeData::new));

    public static final StreamCodec<ByteBuf, EquipForgeData> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    @Getter
    private final EquipmentType equipmentType;
    @Getter
    private final Map<Identifier, PartData> parts;
    @Getter
    private boolean broken;

    private Map<Identifier, Double> mergedValuesCache;
    private List<PerkStack> mergedPerksCache;

    public EquipForgeData(EquipmentType type, Map<Identifier, PartData> parts, boolean broken) {
        this.equipmentType = type;
        this.parts = parts;
        this.broken = broken;
    }

    public Map<Identifier, Double> getMergedValues() {
        if (mergedValuesCache == null) {
            mergedValuesCache = equipmentType.mergeValuesWithAutoUnprocessed(parts);
        }
        return mergedValuesCache;
    }

    public Collection<EquipAttributeModifier> compute() {
        return broken ? Collections.emptyList() : equipmentType.convertToAttributeModifiers(getMergedValues());
    }

    public void applyToItemStack(ItemStack stack) {
        equipmentType.applyToItemStack(stack, getMergedValues());
    }

    public List<PerkStack> getMergedPerks() {
        if (mergedPerksCache == null) {
            mergedPerksCache = equipmentType.mergePerks(parts);
        }
        return mergedPerksCache;
    }

    public Map<EquipmentSlotGroup, List<PerkStack>> getPerkStacks() {
        if (broken) return Collections.emptyMap();
        EquipmentSlotGroup slotGroup = equipmentType.getEquipmentSlot();
        return Map.of(slotGroup, getMergedPerks());
    }

    public void recalculateCache() {
        mergedValuesCache = equipmentType.mergeValuesWithAutoUnprocessed(parts);
        mergedPerksCache = equipmentType.mergePerks(parts);
    }
}
