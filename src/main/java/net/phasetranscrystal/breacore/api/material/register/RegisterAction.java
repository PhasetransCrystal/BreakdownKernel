package net.phasetranscrystal.breacore.api.material.register;

import net.phasetranscrystal.breacore.api.material.Material;

import com.tterrag.registrate.AbstractRegistrate;

@FunctionalInterface
public interface RegisterAction {

    void register(AbstractRegistrate<?> registrate, MaterialVariant variant, Material material);
}
