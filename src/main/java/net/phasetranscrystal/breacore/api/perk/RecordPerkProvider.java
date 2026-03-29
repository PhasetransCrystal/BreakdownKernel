package net.phasetranscrystal.breacore.api.perk;

import net.minecraft.world.entity.EquipmentSlotGroup;

import com.mojang.serialization.Codec;

import java.util.List;
import java.util.Map;

public record RecordPerkProvider(Map<EquipmentSlotGroup, List<PerkStack>> map) implements IPerkProvider {

    public static final Codec<RecordPerkProvider> CODEC = Codec.unboundedMap(EquipmentSlotGroup.CODEC, PerkStack.CODEC.listOf())
            .xmap(RecordPerkProvider::new, RecordPerkProvider::map);

    public RecordPerkProvider(EquipmentSlotGroup group, List<PerkStack> list) {
        this(Map.of(group, list));
    }

    public RecordPerkProvider(EquipmentSlotGroup group, Perk perk, float level) {
        this(Map.of(group, List.of(new PerkStack(perk, level))));
    }

    public RecordPerkProvider(Perk perk, float level) {
        this(EquipmentSlotGroup.ANY, perk, level);
    }

    public RecordPerkProvider(EquipmentSlotGroup group, Perk perk, float level, Object... morePerks) {
        this(Map.ofEntries(
                Map.entry(group, buildPerkStackList(perk, level, morePerks))));
    }

    private static List<PerkStack> buildPerkStackList(Perk perk, float level, Object... morePerks) {
        java.util.ArrayList<PerkStack> list = new java.util.ArrayList<>();
        list.add(new PerkStack(perk, level));
        for (int i = 0; i < morePerks.length; i += 2) {
            list.add(new PerkStack((Perk) morePerks[i], (Float) morePerks[i + 1]));
        }
        return list;
    }

    @Override
    public Map<EquipmentSlotGroup, List<PerkStack>> getPerkStacks() {
        return map;
    }
}
