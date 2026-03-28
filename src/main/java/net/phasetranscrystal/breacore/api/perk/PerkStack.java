package net.phasetranscrystal.breacore.api.perk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.phasetranscrystal.breacore.api.registry.BreaRegistries;

public record PerkStack(Perk perk, float level) {
    public static final Codec<PerkStack> CODEC = RecordCodecBuilder.create(i -> i.group(
            BreaRegistries.PERKS.byNameCodec().fieldOf("perk").forGetter(PerkStack::perk),
            Codec.FLOAT.fieldOf("level").forGetter(PerkStack::level)
    ).apply(i, PerkStack::new));

    public PerkStack withLevel(float level) {
        return new PerkStack(this.perk, level);
    }
}
