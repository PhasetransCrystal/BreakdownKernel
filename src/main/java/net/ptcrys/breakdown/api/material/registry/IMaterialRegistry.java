package net.ptcrys.breakdown.api.material.registry;

import net.ptcrys.breakdown.api.material.Material;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public interface IMaterialRegistry extends Iterable<Material> {

    @UnmodifiableView
    @NotNull
    Collection<String> getUsedNamespaces();

    Material register(Material material);

    Material getMaterial(Identifier name);

    Optional<Holder.Reference<Material>> getHolder(ResourceKey<Material> key);

    Identifier getKey(Material material);

    void setFallbackMaterial(@NotNull String modId, @NotNull Material material);

    @NotNull
    Material getFallbackMaterial(@NotNull String modId);

    Stream<Material> stream();

    @NotNull
    Phase getPhase();

    default boolean canModifyMaterials() {
        return this.getPhase() != Phase.FROZEN && this.getPhase() != Phase.PRE;
    }

    enum Phase {
        /**
         * Material Registration and Modification is not started
         */
        PRE,
        /**
         * Material Registration and Modification is available
         */
        OPEN,
        /**
         * Material Registration is unavailable and only Modification is available
         */
        CLOSED,
        /**
         * Material Registration and Modification is unavailable
         */
        FROZEN
    }
}
