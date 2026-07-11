package net.phasetranscrystal.breacore.api.material.register;

import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.material.attributes.AttributeType;

public class RegisterConditions {

    public static RegisterCondition GenerateDust = (Material material) -> material.hasAttribute(AttributeType.GENERAL);
    public static RegisterCondition GenerateGem = (Material material) -> material.hasAttribute(AttributeType.GEM);
    public static RegisterCondition GenerateIngot = (Material material) -> material.hasAttribute(AttributeType.INGOT);
    public static RegisterCondition GenerateFluid = (Material material) -> material.hasAttribute(AttributeType.FLUID);
}
