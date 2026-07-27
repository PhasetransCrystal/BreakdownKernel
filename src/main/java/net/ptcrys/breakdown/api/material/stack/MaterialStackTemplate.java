package net.ptcrys.breakdown.api.material.stack;

import net.ptcrys.breakdown.api.material.MarkerMaterial;
import net.ptcrys.breakdown.api.material.Material;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jspecify.annotations.Nullable;

public record MaterialStackTemplate(Holder<Material> material, long amount, DataComponentPatch components) implements MaterialInstance {

    public static final MapCodec<MaterialStackTemplate> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            MATERIAL_HOLDER_CODEC.fieldOf(MATERIAL_ID).forGetter(MaterialStackTemplate::material),
            ExtraCodecs.POSITIVE_LONG.fieldOf(MATERIAL_AMOUNT).forGetter(MaterialStackTemplate::amount),
            DataComponentPatch.CODEC.optionalFieldOf(FIELD_COMPONENTS, DataComponentPatch.EMPTY).forGetter(MaterialStackTemplate::components)).apply(i, MaterialStackTemplate::new));
    public static final Codec<MaterialStackTemplate> CODEC = Codec.withAlternative(MAP_CODEC.codec(), MATERIAL_HOLDER_CODEC, material -> new MaterialStackTemplate(material.value(), 0));

    public static final StreamCodec<RegistryFriendlyByteBuf, MaterialStackTemplate> STREAM_CODEC = StreamCodec.composite(
            MATERIAL_HOLDER_STREAM_CODEC, MaterialStackTemplate::material,
            ByteBufCodecs.VAR_LONG, MaterialStackTemplate::amount,
            DataComponentPatch.STREAM_CODEC, MaterialStackTemplate::components,
            MaterialStackTemplate::new);

    public MaterialStackTemplate {
        if (material.is(MarkerMaterial.NULL.builtInRegistryHolder()) || amount <= 0) {
            throw new IllegalStateException("Material must be non-empty!");
        }
    }

    public MaterialStackTemplate(Holder<Material> material, long amount) {
        this(material, amount, DataComponentPatch.EMPTY);
    }

    public MaterialStackTemplate(Material material, long amount, DataComponentPatch components) {
        this(material.builtInRegistryHolder(), amount, components);
    }

    public MaterialStackTemplate(Material material, long amount) {
        this(material, amount, DataComponentPatch.EMPTY);
    }

    public static MaterialStackTemplate fromNomEmptyStack(MaterialStack stack) {
        if (stack.isEmpty()) {
            throw new IllegalStateException("Stack must be non-empty");
        }

        return new MaterialStackTemplate(stack.typeHolder(), stack.getAmount(), stack.getComponentsPatch());
    }

    public MaterialStackTemplate withAmount(long amount) {
        return this.amount == amount ? this : new MaterialStackTemplate(material, amount, components);
    }

    public MaterialStack create() {
        return new MaterialStack(material, amount, components);
    }

    public MaterialStack apply(DataComponentPatch additionalPatch) {
        return apply(amount, additionalPatch);
    }

    public MaterialStack apply(long amount, DataComponentPatch additionalPatch) {
        var stack = new MaterialStack(material, amount, additionalPatch);
        stack.applyComponents(components);
        return stack;
    }

    @Override
    public Holder<Material> typeHolder() {
        return material;
    }

    @Override
    public @Nullable <T> T get(DataComponentType<? extends T> type) {
        return components.get(material.components(), type);
    }

    public static Codec<MaterialStackTemplate> fixedAmountCodec(long amount) {
        return Codec.lazyInitialized(() -> RecordCodecBuilder.create(i -> i.group(
                MATERIAL_HOLDER_CODEC.fieldOf(MATERIAL_ID).forGetter(MaterialStackTemplate::material),
                DataComponentPatch.CODEC.optionalFieldOf(FIELD_COMPONENTS, DataComponentPatch.EMPTY).forGetter(MaterialStackTemplate::components)).apply(i, (holder, patch) -> new MaterialStackTemplate(holder, amount, patch))));
    }

    public static StreamCodec<RegistryFriendlyByteBuf, MaterialStackTemplate> fixedAmountStreamCodec(long amount) {
        return StreamCodec.composite(
                MATERIAL_HOLDER_STREAM_CODEC, MaterialStackTemplate::material,
                DataComponentPatch.STREAM_CODEC, MaterialStackTemplate::components,
                (holder, patch) -> new MaterialStackTemplate(holder, amount, patch));
    }
}
