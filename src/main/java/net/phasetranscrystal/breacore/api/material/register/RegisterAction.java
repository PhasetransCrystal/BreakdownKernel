package net.phasetranscrystal.breacore.api.material.register;

import net.phasetranscrystal.registrylib.RegistryCore;

import net.phasetranscrystal.breacore.api.material.Material;

@FunctionalInterface
public interface RegisterAction {

    void register(RegistryCore registrate, MaterialVariant variant, Material material);
}
