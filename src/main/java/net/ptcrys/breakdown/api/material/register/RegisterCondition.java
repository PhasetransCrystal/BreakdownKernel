package net.ptcrys.breakdown.api.material.register;

import net.ptcrys.breakdown.api.material.Material;

@FunctionalInterface
public interface RegisterCondition {

    boolean validate(Material material);
}
