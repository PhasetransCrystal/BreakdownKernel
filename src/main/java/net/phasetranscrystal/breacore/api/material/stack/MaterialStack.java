package net.phasetranscrystal.breacore.api.material.stack;

import net.phasetranscrystal.breacore.api.material.MarkerMaterial;
import net.phasetranscrystal.breacore.api.material.Material;

import net.minecraft.core.Holder;
import net.minecraft.core.component.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.common.MutableDataComponentHolder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import lombok.Setter;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public final class MaterialStack implements MutableDataComponentHolder, MaterialInstance, DataComponentHolder {

    public static final MapCodec<MaterialStack> MAP_CODEC = MapCodec.recursive(
            "MaterialStack",
            c -> RecordCodecBuilder.mapCodec(
                    instance -> instance.group(
                            MATERIAL_HOLDER_CODEC_WITH_BOUND_COMPONENTS.fieldOf(MATERIAL_ID).forGetter(MaterialStack::typeHolder),
                            ExtraCodecs.POSITIVE_LONG.fieldOf(MATERIAL_AMOUNT).forGetter(MaterialStack::getAmount),
                            DataComponentPatch.CODEC.optionalFieldOf(FIELD_COMPONENTS, DataComponentPatch.EMPTY)
                                    .forGetter(stack -> stack.components.asPatch()))
                            .apply(instance, MaterialStack::new)));
    public static final Codec<MaterialStack> CODEC = Codec.lazyInitialized(MAP_CODEC::codec);

    public static Codec<MaterialStack> fixedAmountCodec(long amount) {
        return Codec.lazyInitialized(
                () -> RecordCodecBuilder.create(
                        instance -> instance.group(
                                MATERIAL_HOLDER_CODEC.fieldOf(MATERIAL_ID).forGetter(MaterialStack::typeHolder),
                                DataComponentPatch.CODEC.optionalFieldOf(FIELD_COMPONENTS, DataComponentPatch.EMPTY)
                                        .forGetter(stack -> stack.components.asPatch()))
                                .apply(instance, (holder, patch) -> new MaterialStack(holder, amount, patch))));
    }

    public static final Codec<MaterialStack> OPTIONAL_CODEC = ExtraCodecs.optionalEmptyMap(CODEC)
            .xmap(optional -> optional.orElse(MaterialStack.EMPTY), stack -> stack.isEmpty() ? Optional.empty() : Optional.of(stack));

    public static final StreamCodec<RegistryFriendlyByteBuf, MaterialStack> OPTIONAL_STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, MaterialStack>() {

        @Override
        public MaterialStack decode(RegistryFriendlyByteBuf buf) {
            var amount = buf.readVarLong();
            if (amount <= 0)
                return MaterialStack.EMPTY;
            else {
                var holder = MATERIAL_HOLDER_STREAM_CODEC.decode(buf);
                var patch = DataComponentPatch.STREAM_CODEC.decode(buf);
                return new MaterialStack(holder, amount, patch);
            }
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, MaterialStack stack) {
            if (stack.isEmpty())
                buf.writeVarLong(0);
            else {
                buf.writeVarLong(stack.getAmount());
                MATERIAL_HOLDER_STREAM_CODEC.encode(buf, stack.typeHolder());
                DataComponentPatch.STREAM_CODEC.encode(buf, stack.components.asPatch());
            }
        }
    };
    public static final StreamCodec<RegistryFriendlyByteBuf, MaterialStack> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, MaterialStack>() {

        @Override
        public MaterialStack decode(RegistryFriendlyByteBuf buf) {
            var stack = MaterialStack.OPTIONAL_STREAM_CODEC.decode(buf);
            if (stack.isEmpty())
                throw new DecoderException("Empty MaterialStack not allowed");
            return stack;
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, MaterialStack stack) {
            if (stack.isEmpty())
                throw new EncoderException("Empty MaterialStack not allowed");
            MaterialStack.OPTIONAL_STREAM_CODEC.encode(buf, stack);
        }
    };

    public static final MaterialStack EMPTY = new MaterialStack(null);
    @Setter
    private long amount;
    private final @Nullable Holder<Material> material;
    private final PatchedDataComponentMap components;

    @Override
    public DataComponentMap getComponents() {
        return isEmpty() ? DataComponentMap.EMPTY : components;
    }

    public DataComponentPatch getComponentsPatch() {
        return !this.isEmpty() ? this.components.asPatch() : DataComponentPatch.EMPTY;
    }

    public DataComponentMap immutableComponents() {
        return !this.isEmpty() ? this.components.toImmutableMap() : DataComponentMap.EMPTY;
    }

    public boolean hasNonDefault(DataComponentType<?> type) {
        return !isEmpty() && components.hasNonDefault(type);
    }

    public boolean isComponentsPatchEmpty() {
        return this.isEmpty() || this.components.isPatchEmpty();
    }

    public MaterialStack(Material material, long amount, DataComponentPatch patch) {
        this(material.builtInRegistryHolder(), amount, patch);
    }

    public MaterialStack(Material material, long amount) {
        this(material.builtInRegistryHolder(), amount, DataComponentPatch.EMPTY);
    }

    public MaterialStack(Holder<Material> material, long amount, DataComponentPatch patch) {
        this(material, amount, PatchedDataComponentMap.fromPatch(material.components(), patch));
    }

    public MaterialStack(Holder<Material> material, long amount) {
        this(material, amount, DataComponentPatch.EMPTY);
    }

    public MaterialStack(Holder<Material> material, long amount, PatchedDataComponentMap components) {
        this.material = material;
        this.amount = amount;
        this.components = components;
    }

    private MaterialStack(@Nullable Void unused) {
        this.material = null;
        this.components = new PatchedDataComponentMap(DataComponentMap.EMPTY);
    }

    public boolean isEmpty() {
        return this == EMPTY || material.value().isSame(MarkerMaterial.NULL) || this.amount <= 0;
    }

    public MaterialStack split(long amount) {
        long i = Math.min(amount, getAmount());
        MaterialStack materialStack = this.copyWithAmount(i);
        this.shrink(i);
        return materialStack;
    }

    public MaterialStack copyAndClear() {
        if (this.isEmpty()) {
            return EMPTY;
        } else {
            MaterialStack materialStack = this.copy();
            this.setAmount(0);
            return materialStack;
        }
    }

    public Material getMaterial() {
        return typeHolder().value();
    }

    @Override
    public Holder<Material> typeHolder() {
        return isEmpty() ? MarkerMaterial.NULL.builtInRegistryHolder() : material;
    }

    public boolean is(Predicate<Holder<Material>> holderPredicate) {
        return holderPredicate.test(this.typeHolder());
    }

    public MaterialStack copy() {
        if (this.isEmpty()) {
            return EMPTY;
        } else {
            return new MaterialStack(typeHolder(), amount(), this.components.copy());
        }
    }

    public MaterialStack copyWithAmount(long amount) {
        if (this.isEmpty()) {
            return EMPTY;
        } else {
            MaterialStack materialStack = this.copy();
            materialStack.setAmount(amount);
            return materialStack;
        }
    }

    public MaterialStack transmuteCopy(Material newMaterial) {
        return transmuteCopy(newMaterial, amount());
    }

    public MaterialStack transmuteCopy(Material newMaterial, long newAmount) {
        return isEmpty() ? EMPTY : transmuteCopyIgnoreEmpty(newMaterial, newAmount);
    }

    private MaterialStack transmuteCopyIgnoreEmpty(Material newMaterial, long newAmount) {
        return new MaterialStack(newMaterial, newAmount, components.asPatch());
    }

    @Override
    public String toString() {
        return this.getAmount() + " " + this.getMaterial();
    }

    @Override
    public @Nullable <T> T set(DataComponentType<T> componentType, @Nullable T value) {
        return this.components.set(componentType, value);
    }

    public <T> @Nullable T set(TypedDataComponent<T> value) {
        return components.set(value);
    }

    @Override
    public @Nullable <T> T remove(DataComponentType<? extends T> componentType) {
        return this.components.remove(componentType);
    }

    @Override
    public void applyComponents(DataComponentPatch patch) {
        this.components.applyPatch(patch);
    }

    @Override
    public void applyComponents(DataComponentMap components) {
        this.components.setAll(components);
    }

    @Override
    public long amount() {
        return this.isEmpty() ? 0 : this.amount;
    }

    public long getAmount() {
        return amount();
    }

    public void grow(long addedAmount) {
        this.setAmount(this.getAmount() + addedAmount);
    }

    public void shrink(long removedAmount) {
        this.grow(-removedAmount);
    }

    public static boolean matches(MaterialStack first, MaterialStack second) {
        if (first == second) {
            return true;
        } else {
            return first.getAmount() != second.getAmount() ? false : isSameMaterialSameComponents(first, second);
        }
    }

    public static boolean matches(MaterialStack a, @Nullable MaterialStackTemplate b) {
        if (b == null) {
            return a.isEmpty();
        }

        return a.amount() == b.amount() && isSameMaterialSameComponents(a, b);
    }

    public static boolean isSameMaterial(MaterialStack first, MaterialStack second) {
        return first.is(second.getMaterial());
    }

    public static boolean isSameMaterial(MaterialStack a, MaterialStackTemplate b) {
        return b == null ? a.isEmpty() : a.is(b.material());
    }

    /**
     * Checks if the two fluid stacks have the same fluid and components. Ignores amount.
     *
     * @return {@code true} if the two fluid stacks have the same fluid and components
     */
    public static boolean isSameMaterialSameComponents(MaterialStack first, MaterialStack second) {
        if (!first.is(second.getMaterial())) {
            return false;
        } else {
            return first.isEmpty() && second.isEmpty() ? true : Objects.equals(first.components, second.components);
        }
    }

    public static boolean isSameMaterialSameComponents(MaterialStack a, MaterialStackTemplate b) {
        if (a.isEmpty() || b == null) {
            return a.isEmpty() == (b == null);
        } else {
            return a.is(b.material()) && a.components.patchEquals(b.components());
        }
    }

    public static MapCodec<MaterialStack> lenientOptionalFieldOf(String fieldName) {
        return CODEC.lenientOptionalFieldOf(fieldName)
                .xmap(optional -> optional.orElse(EMPTY), stack -> stack.isEmpty() ? Optional.empty() : Optional.of(stack));
    }

    /**
     * Hashes the fluid and components of this stack, ignoring the amount.
     */
    public static long hashMaterialAndComponents(@Nullable MaterialStack stack) {
        if (stack != null) {
            long i = 31 + stack.getMaterial().hashCode();
            return 31 * i + stack.getComponents().hashCode();
        } else {
            return 0;
        }
    }
}
