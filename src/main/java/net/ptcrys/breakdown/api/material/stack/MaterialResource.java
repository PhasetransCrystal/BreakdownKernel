package net.ptcrys.breakdown.api.material.stack;

import net.ptcrys.breakdown.api.material.MarkerMaterial;
import net.ptcrys.breakdown.api.material.Material;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.resource.DataComponentHolderResource;

import com.mojang.serialization.Codec;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MaterialResource implements DataComponentHolderResource<Material> {

    public static final MaterialResource EMPTY = new MaterialResource(MaterialStack.EMPTY);
    public static final Codec<MaterialResource> CODEC = MaterialStack.fixedAmountCodec(1)
            .xmap(MaterialResource::of, resource -> resource.toStack(1));
    public static final Codec<MaterialResource> OPTIONAL_CODEC = ExtraCodecs.optionalEmptyMap(CODEC).xmap(
            optional -> optional.orElse(MaterialResource.EMPTY),
            resource -> resource.isEmpty() ? Optional.empty() : Optional.of(resource));
    public static final StreamCodec<RegistryFriendlyByteBuf, MaterialResource> STREAM_CODEC = StreamCodec.composite(
            MaterialInstance.MATERIAL_HOLDER_STREAM_CODEC, MaterialResource::typeHolder,
            DataComponentPatch.STREAM_CODEC, MaterialResource::getComponentsPatch,
            MaterialResource::of);

    public static MaterialResource of(MaterialStack stack) {
        if (stack.isEmpty() || stack.isComponentsPatchEmpty()) {
            return of(stack.getMaterial());
        }
        return new MaterialResource(stack.copyWithAmount(1));
    }

    public static MaterialResource of(Material material) {
        if (material == MarkerMaterial.NULL) return EMPTY;
        return material.computeDefaultResource(m -> new MaterialResource(new MaterialStack(m, 1)));
    }

    public static MaterialResource of(Holder<Material> material) {
        return of(material.value());
    }

    public static MaterialResource of(Holder<Material> holder, DataComponentPatch patch) {
        if (holder.value() == MarkerMaterial.NULL || patch.isEmpty())
            return of(holder.value());
        return new MaterialResource(new MaterialStack(holder, 1, patch));
    }

    private final MaterialStack innerStack;

    private MaterialResource(MaterialStack innerStack) {
        this.innerStack = innerStack;
    }

    @Override
    public Material value() {
        return innerStack.getMaterial();
    }

    public Material getMaterial() {
        return value();
    }

    @Override
    public Holder<Material> typeHolder() {
        return innerStack.typeHolder();
    }

    @Override
    public boolean isEmpty() {
        return innerStack.isEmpty();
    }

    @Override
    public MaterialResource withMergedPatch(DataComponentPatch patch) {
        if (isEmpty() || patch.isEmpty())
            return this;
        var stack = innerStack.copy();
        stack.applyComponents(patch);
        return MaterialResource.of(stack);
    }

    @Override
    public <D> MaterialResource with(DataComponentType<D> type, D data) {
        if (isEmpty()) return MaterialResource.EMPTY;
        if (Objects.equals(get(type), data)) return this;
        var stack = innerStack.copy();
        stack.set(type, data);
        return MaterialResource.of(stack);
    }

    @Override
    public <D> MaterialResource with(Supplier<? extends DataComponentType<D>> type, D data) {
        return with(type.get(), data);
    }

    @Override
    public MaterialResource without(DataComponentType<?> type) {
        if (isEmpty()) return MaterialResource.EMPTY;
        if (get(type) == null) return this;
        var stack = innerStack.copy();
        stack.remove(type);
        return MaterialResource.of(stack);
    }

    @Override
    public MaterialResource without(Supplier<? extends DataComponentType<?>> type) {
        return without(type.get());
    }

    @Override
    public DataComponentMap getComponents() {
        return innerStack.immutableComponents();
    }

    @Override
    public DataComponentPatch getComponentsPatch() {
        return innerStack.getComponentsPatch();
    }

    public MaterialStack toStack(int amount) {
        TransferPreconditions.checkNonNegative(amount);
        if (amount == 0) return MaterialStack.EMPTY;
        return innerStack.copyWithAmount(amount);
    }

    @Override
    public boolean isComponentsPatchEmpty() {
        return innerStack.isComponentsPatchEmpty();
    }

    public boolean matches(MaterialStack stack) {
        return MaterialStack.isSameMaterialSameComponents(stack, innerStack);
    }

    public boolean test(Predicate<MaterialStack> predicate) {
        return predicate.test(innerStack);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        MaterialResource other = (MaterialResource) obj;
        return MaterialStack.isSameMaterialSameComponents(this.innerStack, other.innerStack);
    }

    @Override
    public int hashCode() {
        return Math.toIntExact(MaterialStack.hashMaterialAndComponents(innerStack));
    }

    @Override
    public String toString() {
        // Fluid type string with patch count
        return value() + " [" + getComponentsPatch().size() + "]";
    }
}
