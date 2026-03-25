package net.phasetranscrystal.breacore.api.material.register;

import net.phasetranscrystal.breacore.api.material.Material;

@FunctionalInterface
public interface RegisterCondition {

    boolean doGenerator(Material material);
}
