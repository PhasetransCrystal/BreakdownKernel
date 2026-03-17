package net.phasetranscrystal.breacore.api.material.attributes;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class AttributeType<T extends MaterialAttribute> {

    public static final AttributeType<GeneralAttribute> GENERAL = new AttributeType<>("general", GeneralAttribute.class, GeneralAttribute::new);
    public static final AttributeType<FluidAttribute> FLUID = new AttributeType<>("fluid", FluidAttribute.class, FluidAttribute::new);
    @SuppressWarnings("ClassEscapesDefinedScope")
    public static final AttributeType<PlaceholderAttribute> EMPTY = new AttributeType<>("empty", PlaceholderAttribute.class, PlaceholderAttribute::new);
    private final String key;
    private final Class<T> type;
    private final Supplier<T> defaultSupplier;

    public AttributeType(String key, Class<T> type) {
        this.key = key;
        this.type = type;
        defaultSupplier = null;
    }

    public AttributeType(String key, Class<T> type, @NotNull Supplier<T> defaultSupplier) {
        this.key = key;
        this.type = type;
        this.defaultSupplier = defaultSupplier;
    }

    protected String getKey() {
        return key;
    }

    protected Optional<T> constructDefault() {
        try {
            return Optional.ofNullable(defaultSupplier).map(Supplier::get);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public T cast(MaterialAttribute property) {
        return this.type.cast(property);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AttributeType) {
            return ((AttributeType<?>) o).getKey().equals(key);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return key;
    }

    private static class PlaceholderAttribute implements MaterialAttribute {

        private PlaceholderAttribute() {}

        @Override
        public boolean canBeAddedTo(MaterialAttributeSet currentSet) {
            return true;
        }
    }
}
