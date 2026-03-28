package net.phasetranscrystal.breacore.api.material.stack;

import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.tag.TagPrefix;
import net.phasetranscrystal.breacore.data.materials.BreaMaterials;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

public record MaterialEntry(@NotNull TagPrefix tagPrefix, @NotNull Material material) {

    public static final MaterialEntry NULL_ENTRY = new MaterialEntry(TagPrefix.NULL_PREFIX, BreaMaterials.NULL);
    private static final Map<String, MaterialEntry> PARSE_CACHE = new WeakHashMap<>();

    public MaterialEntry {
        Preconditions.checkNotNull(tagPrefix, "MaterialEntry TagPrefix cannot be null!");
        Preconditions.checkNotNull(material, "MaterialEntry Material cannot be null!");
    }

    public MaterialEntry(TagPrefix tagPrefix) {
        this(tagPrefix, BreaMaterials.NULL);
    }

    public static @Nullable MaterialEntry of(Object o) {
        if (o instanceof MaterialEntry entry) return entry;
        if (o instanceof CharSequence chars) {
            var str = chars.toString().trim();
            var cached = PARSE_CACHE.get(str);
            if (cached != null) return cached;

            var values = str.split(":", 2);
            if (values.length > 1) {
                var prefix = TagPrefix.get(values[0]);
                if (prefix == null) throw new IllegalArgumentException("Invalid TagPrefix: " + values[0]);
                cached = new MaterialEntry(prefix, BreaMaterials.get(values[1]));
                PARSE_CACHE.put(str, cached);
                return cached;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return this == NULL_ENTRY || material() == BreaMaterials.NULL || tagPrefix().isEmpty();
    }

    @Override
    public String toString() {
        if (tagPrefix.isEmpty()) {
            return material.getIdentifier().toString();
        }
        var tags = tagPrefix.getItemTags(material);
        if (tags.isEmpty()) {
            return tagPrefix.name + "/" + material.getName();
        }
        return tags.getFirst().location().toString();
    }
}
