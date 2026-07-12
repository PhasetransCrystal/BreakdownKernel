package net.phasetranscrystal.breacore.api.material.register;

import net.phasetranscrystal.registrylib.Group;

import net.phasetranscrystal.breacore.api.material.Material;

@FunctionalInterface
public interface RegisterAction {

    void register(Group registrate, MaterialVariant variant, Material material);
}
