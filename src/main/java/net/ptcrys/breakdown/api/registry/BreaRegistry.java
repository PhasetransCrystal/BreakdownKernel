package net.ptcrys.breakdown.api.registry;

import net.ptcrys.breakdown.BreaLib;
import net.ptcrys.breakdown.mixins.MappedRegistryAccess;
import net.ptcrys.breakdown.mixins.ResourceKeyAccessor;

import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;

import com.google.common.collect.Maps;
import com.mojang.serialization.Lifecycle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BreaRegistry<T> extends MappedRegistry<T> {

    public BreaRegistry(Identifier registryName) {
        this(ResourceKeyAccessor.callCreate(BreaRegistries.ROOT_REGISTRY_NAME, registryName));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public BreaRegistry(ResourceKey<? extends Registry<T>> registryKey) {
        super(registryKey, Lifecycle.stable());
        if (!registryKey.identifier().equals(BreaRegistries.ROOT_REGISTRY_NAME)) {
            BreaRegistries.ROOT.register((ResourceKey) registryKey, this, RegistrationInfo.BUILT_IN);
        }
    }

    @Override
    public @NotNull Registry<T> freeze() {
        if (!checkActiveModContainerIsOwner()) {
            return this;
        }
        return super.freeze();
    }

    @Override
    public void unfreeze(boolean clearTags) {
        if (!checkActiveModContainerIsOwner()) {
            return;
        }
        super.unfreeze(clearTags);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean checkActiveModContainerIsOwner() {
        ModContainer container = ModLoadingContext.get().getActiveContainer();
        return container != null && (container.getModId().equals(this.key().identifier().getNamespace()) ||
                container.getModId().equals(BreaLib.Core_ID) ||
                container.getModId().equals("minecraft")); // check for minecraft in case of datagen or a mishap
    }

    public <V extends T> V register(ResourceKey<T> key, V value) {
        if (containsKey(key)) {
            throw new IllegalStateException(
                    "[BreaRegistry] registrate %s contains key %s already".formatted(key().identifier(), key.identifier()));
        }
        return this.registerOrOverride(key, value);
    }

    public <V extends T> V register(Identifier key, V value) {
        return this.register(ResourceKey.create(this.key(), key), value);
    }

    @Nullable
    public <V extends T> V replace(Identifier key, V value) {
        return this.replace(ResourceKey.create(this.key(), key), value);
    }

    @Nullable
    public <V extends T> V replace(ResourceKey<T> key, V value) {
        if (!containsKey(key)) {
            BreaLib.LOGGER.warn("[BreaRegistry] couldn't find key {} in registrate {}", key, key().identifier());
        }
        registerOrOverride(key, value);
        return value;
    }

    public <V extends T> V registerOrOverride(Identifier key, V value) {
        return this.registerOrOverride(ResourceKey.create(this.key(), key), value);
    }

    public <V extends T> V registerOrOverride(ResourceKey<T> key, V value) {
        super.register(key, value, RegistrationInfo.BUILT_IN);
        return value;
    }

    @UnmodifiableView
    @Override
    public @NotNull Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
        return super.entrySet();
    }

    @UnmodifiableView
    public Set<Map.Entry<Identifier, T>> entries() {
        return Collections.unmodifiableSet(Maps.transformValues(((MappedRegistryAccess<T>) this).getByLocation(), Holder::value).entrySet());
    }

    @UnmodifiableView
    public Map<Identifier, T> registry() {
        return Collections.unmodifiableMap(Maps.transformValues(((MappedRegistryAccess<T>) this).getByLocation(), Holder::value));
    }

    public T getOrDefault(Identifier name, T defaultValue) {
        var value = get(name);
        return value.map(Holder::value).orElse(defaultValue);
    }

    public Identifier getOrDefaultKey(T value, Identifier defaultKey) {
        Identifier key = getKey(value);
        return key != null ? key : defaultKey;
    }

    public boolean remove(Identifier key) {
        return remove(ResourceKey.create(this.key(), key));
    }

    public boolean remove(ResourceKey<T> key) {
        Holder<T> holder = this.getHolder(key).orElse(null);
        if (holder == null) {
            return false;
        }
        return remove(holder);
    }

    public Optional<Holder.Reference<T>> getHolder(ResourceKey<T> key) {
        return Optional.ofNullable(((MappedRegistryAccess<T>) this).getByKey().get(this.resolve(key)));
    }

    public boolean remove(Holder<T> holder) {
        MappedRegistryAccess<T> mixin;
        try {
            mixin = (MappedRegistryAccess<T>) this;
        } catch (Exception e) {
            return false;
        }
        ResourceKey<T> key = holder.getKey();
        boolean removed = true;
        removed &= mixin.getByKey().remove(key) != null;
        removed &= mixin.getByLocation().remove(key.identifier()) != null;
        removed &= mixin.getByValue().remove(holder.value()) != null;
        removed &= mixin.getById().remove(holder);
        removed &= mixin.getToId().removeInt(holder.value()) != -1;
        removed &= mixin.getRegistrationInfos().remove(key) != null;
        removed &= mixin.getRegistrationInfos().remove(key) != null;

        return removed;
    }

    public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
        return new StreamCodec<>() {

            public @NotNull T decode(@NotNull RegistryFriendlyByteBuf buffer) {
                return BreaRegistry.this.byIdOrThrow(VarInt.read(buffer));
            }

            public void encode(@NotNull RegistryFriendlyByteBuf buffer, @NotNull T value) {
                VarInt.write(buffer, BreaRegistry.this.getIdOrThrow(value));
            }
        };
    }

    @Override
    public void clear(boolean full) {
        super.clear(full);
    }
}
