package net.phasetranscrystal.breacore.api.material.register;

import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.material.attributes.AttributeType;

public class RegisterConditions {

    public static RegisterCondition GenerateDust = (Material material) -> {
        if (!material.hasAttribute(AttributeType.GENERAL)) throw new IllegalArgumentException();
    };
    public static RegisterCondition GenerateGem = (Material material) -> {
        if (!material.hasAttribute(AttributeType.GEM)) throw new IllegalArgumentException();
    };
    public static RegisterCondition GenerateIngot = (Material material) -> {
        if (!material.hasAttribute(AttributeType.INGOT)) throw new IllegalArgumentException();
    };
}
