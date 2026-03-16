package net.phasetranscrystal.breacore.mixins;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ResourceKey.class)
public interface ResourceKeyAccessor {

    @Invoker("<init>")
    static <T> ResourceKey<T> callCreate(Identifier registryName, Identifier identifier) {
        throw new AssertionError();
    }
}
