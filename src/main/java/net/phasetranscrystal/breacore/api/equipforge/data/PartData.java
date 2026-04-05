package net.phasetranscrystal.breacore.api.equipforge.data;

import net.phasetranscrystal.breacore.api.equipforge.MaterialPerkHelper;
import net.phasetranscrystal.breacore.api.equipforge.ModificationType;
import net.phasetranscrystal.breacore.api.equipforge.PartType;
import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.perk.PerkStack;
import net.phasetranscrystal.breacore.api.registry.BreaRegistries;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
public class PartData {

    public static final Codec<PartData> CODEC = RecordCodecBuilder.create(i -> i.group(
            BreaRegistries.MATERIALS.byNameCodec().fieldOf("material").forGetter(PartData::getMaterial),
            BreaRegistries.MODIFICATION_TYPES.byNameCodec().optionalFieldOf("modification").forGetter(PartData::getModification),
            BreaRegistries.PART_TYPES.byNameCodec().fieldOf("partType").forGetter(PartData::getPartType)).apply(i, PartData::new));

    public static final StreamCodec<ByteBuf, PartData> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    private final Material material;
    private final Optional<ModificationType> modification;
    private final PartType partType;
    private Map<Identifier, Double> cachedValues;
    private List<PerkStack> cachedPerks;

    public PartData(Material material, Optional<ModificationType> modification, PartType partType) {
        this.material = material;
        this.modification = modification;
        this.partType = partType;

        recalculateCache();
    }

    public void recalculateCache() {
        Map<Identifier, Double> baseValues = MaterialPerkHelper.extractBaseValues(material);
        this.cachedValues = calculateValues(baseValues, partType, modification.orElse(null));

        List<PerkStack> basePerks = MaterialPerkHelper.extractPerkList(material);
        this.cachedPerks = new ArrayList<>(partType.getPerks(Map.of(partType.getPerkSourceId(), basePerks)));
    }

    private Map<Identifier, Double> calculateValues(Map<Identifier, Double> baseValues, PartType partType, ModificationType modification) {
        Map<Identifier, Double> result = new HashMap<>(partType.getBaseValues(baseValues));

        if (modification != null) {
            for (Map.Entry<Identifier, Double> entry : modification.getValueModifiers().entrySet()) {
                result.merge(entry.getKey(), entry.getValue(), Double::sum);
            }
        }

        return Map.copyOf(result);
    }

    public PartData(Material material, Optional<ModificationType> modification, PartType partType, Map<Identifier, Double> cachedValues, List<PerkStack> cachedPerks) {
        this.material = material;
        this.modification = modification;
        this.partType = partType;
        this.cachedValues = cachedValues;
        this.cachedPerks = cachedPerks;
    }
}
