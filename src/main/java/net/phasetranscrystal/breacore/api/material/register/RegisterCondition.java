package net.phasetranscrystal.breacore.api.material.register;

import net.phasetranscrystal.breacore.api.material.Material;

@FunctionalInterface
public interface RegisterCondition {
    void validate(Material material) throws IllegalArgumentException;
}
