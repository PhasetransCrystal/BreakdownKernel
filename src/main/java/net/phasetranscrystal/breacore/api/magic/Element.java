package net.phasetranscrystal.breacore.api.magic;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;

import java.util.Map;

public enum Element {
    METAL("metal"),
    PLANT("plant"),
    WATER("water"),
    FIRE("fire"),
    GROUND("ground"),
    WIND("wind"),
    ICE("ice"),
    BLOOD("blood"),
    SILK("silk"),
    ELECTRICITY("electricity"),
    LIGHT("light"),
    DARK("dark"),
    ORI("ori"),
    NONE("none");

    public static final Map<String, Element> MAP;
    public static final Codec<Element> CODEC;

    static {
        ImmutableMap.Builder<String, Element> builder = ImmutableMap.builder();
        for (Element value : Element.values()) {
            builder.put(value.id, value);
        }
        MAP = builder.build();
        CODEC = Codec.STRING.xmap(
                id -> MAP.getOrDefault(id == null ? "none" : id.toLowerCase(), NONE),
                element -> (element == null ? NONE : element).id
        );
    }

    public final String id;



    Element(String id) {
        this.id = id;
    }
}
