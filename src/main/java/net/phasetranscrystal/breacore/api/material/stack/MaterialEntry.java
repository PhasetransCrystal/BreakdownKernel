package net.phasetranscrystal.breacore.api.material.stack;

import net.phasetranscrystal.breacore.api.material.MarkerMaterial;
import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.material.register.MaterialVariant;
import net.phasetranscrystal.breacore.common.data.BreaMaterials;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

public record MaterialEntry(@NotNull MaterialVariant variant, @NotNull Material material) {

    public MaterialEntry {
        Preconditions.checkNotNull(variant, "MaterialEntry Variant cannot be null!");
        Preconditions.checkNotNull(material, "MaterialEntry Material cannot be null!");
    }

    public static final MaterialEntry NULL_ENTRY = new MaterialEntry(MaterialVariant.NULL, MarkerMaterial.NULL);

    private static final Map<String, MaterialEntry> PARSE_CACHE = new WeakHashMap<>();

    public MaterialEntry(MaterialVariant variant) {
        this(variant, MarkerMaterial.NULL);
    }

    public boolean isEmpty() {
        return this == NULL_ENTRY || material() == MarkerMaterial.NULL || variant().isEmpty();
    }

    @Override
    public String toString() {
        if (variant.isEmpty()) {
            return material.getIdentifier().toString();
        }
        var tags = variant.getItemTags(material);
        if (tags.length == 0) {
            return variant.langValue + "/" + material.getName();
        }
        return tags[0].location().toString();
    }

    public static @Nullable MaterialEntry of(Object o) {
        if (o instanceof MaterialEntry entry) return entry;
        if (o instanceof CharSequence chars) {
            var str = chars.toString().trim();
            var cached = PARSE_CACHE.get(str);
            if (cached != null) return cached;

            var values = str.split(":", 2);
            if (values.length > 1) {
                var prefix = MaterialVariant.get(values[0]);
                if (prefix == null) throw new IllegalArgumentException("Invalid TagPrefix: " + values[0]);
                cached = new MaterialEntry(prefix, BreaMaterials.get(values[1]));
                PARSE_CACHE.put(str, cached);
                return cached;
            }
        }
        return null;
    }
}
