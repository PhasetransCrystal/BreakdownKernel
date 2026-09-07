package net.ptcrys.breakdown.api.pipe.common;

import net.ptcrys.breakdown.api.pipe.BuiltinBdPipeLang;
import net.ptcrys.topo.api.api.lang.LangKey;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

import com.mojang.serialization.Codec;
import org.jspecify.annotations.NonNull;

import java.util.Locale;

/** Distance iteration order of {@link #BY_DISTANCE}. */
public enum DistanceOrder implements StringRepresentable {

    NEAREST,
    FARTHEST;

    public static final Codec<DistanceOrder> CODEC = StringRepresentable.fromEnum(DistanceOrder::values);

    @Override
    public @NonNull String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    /** Display handle for GUI toggle buttons / Jade labels. */
    public LangKey nameLang() {
        return BuiltinBdPipeLang.distanceOrder(this);
    }

    public Component displayName() {
        return nameLang().getComponent();
    }
}
