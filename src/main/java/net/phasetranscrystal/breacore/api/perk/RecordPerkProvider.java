package net.phasetranscrystal.breacore.api.perk;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.EquipmentSlotGroup;

import java.util.List;
import java.util.Map;

public record RecordPerkProvider(Map<EquipmentSlotGroup, List<PerkStack>> map) implements IPerkProvider {
    public static final Codec<RecordPerkProvider> CODEC =
            Codec.unboundedMap(EquipmentSlotGroup.CODEC, PerkStack.CODEC.listOf())
                    .xmap(RecordPerkProvider::new, RecordPerkProvider::map);

    public RecordPerkProvider(EquipmentSlotGroup group, List<PerkStack> list) {
        this(Map.of(group, list));
    }

    public RecordPerkProvider(EquipmentSlotGroup group, Perk perk, float level) {
        this(Map.of(group, List.of(new PerkStack(perk, level))));
    }

    public RecordPerkProvider(Perk perk, float level) {
        this(EquipmentSlotGroup.ANY,perk,level);
    }

    @Override
    public Map<EquipmentSlotGroup, List<PerkStack>> getPerkStacks() {
        return map;
    }
}
