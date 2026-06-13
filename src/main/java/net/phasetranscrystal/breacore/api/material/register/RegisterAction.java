package net.phasetranscrystal.breacore.api.material.register;

import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.registrylib.RegistryCore;

@FunctionalInterface
public interface RegisterAction {

    void register(RegistryCore registrate, MaterialVariant variant, Material material);
}
