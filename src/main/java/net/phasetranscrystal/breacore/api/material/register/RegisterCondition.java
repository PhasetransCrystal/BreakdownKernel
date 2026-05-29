package net.phasetranscrystal.breacore.api.material.register;

import net.phasetranscrystal.breacore.api.material.Material;

@FunctionalInterface
public interface RegisterCondition {

    boolean validate(Material material);
}
