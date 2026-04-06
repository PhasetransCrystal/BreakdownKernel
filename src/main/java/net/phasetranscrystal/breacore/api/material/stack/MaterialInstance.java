package net.phasetranscrystal.breacore.api.material.stack;

import net.phasetranscrystal.breacore.api.material.MarkerMaterial;
import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.registry.BreaRegistries;

import net.minecraft.core.Holder;
import net.minecraft.core.TypedInstance;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

public interface MaterialInstance extends TypedInstance<Material>, DataComponentGetter {

    String MATERIAL_ID = "id";
    String MATERIAL_AMOUNT = "amount";
    String FIELD_COMPONENTS = "components";

    Codec<Holder<Material>> MATERIAL_HOLDER_CODEC = BreaRegistries.MATERIALS
            .holderByNameCodec()
            .validate(material -> material.is(MarkerMaterial.NULL.builtInRegistryHolder()) ? DataResult.error(() -> "Material must not be breacore:null") : DataResult.success(material));

    StreamCodec<RegistryFriendlyByteBuf, Holder<Material>> MATERIAL_HOLDER_STREAM_CODEC = ByteBufCodecs.holderRegistry(BreaRegistries.MATERIAL_KEY);

    Codec<Holder<Material>> MATERIAL_HOLDER_CODEC_WITH_BOUND_COMPONENTS = MATERIAL_HOLDER_CODEC.validate(
            material -> !material.areComponentsBound() ? DataResult.error(() -> "Material " + material.getRegisteredName() + " does not have components yet") : DataResult.success(material));

    int amount();
}
