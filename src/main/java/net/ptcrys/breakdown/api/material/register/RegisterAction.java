package net.ptcrys.breakdown.api.material.register;

import net.ptcrys.breakdown.api.material.Material;
import net.ptcrys.registrylib.Group;

@FunctionalInterface
public interface RegisterAction {

    void register(Group registrate, MaterialVariant variant, Material material);
}
