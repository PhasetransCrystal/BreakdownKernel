package net.ptcrys.breakdown.mixins;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Attributes.class)
public class AttributeRangeMixin {

    // @Redirect(
    // method = "<clinit>",
    // at = @At(
    // value = "NEW",
    // target = "(Ljava/lang/String;DDD)Lnet/minecraft/world/entity/ai/attributes/RangedAttribute;"
    // )
    // )
    // private static RangedAttribute redirectMaxHealthAttribute(String descriptionId, double defaultValue, double
    // minValue, double maxValue) {
    // return switch (descriptionId) {
    // case "attribute.name.max_health",
    // "attribute.name.armor",
    // "attribute.name.armor_toughness",
    // "attribute.name.attack_damage" ->
    // new RangedAttribute(descriptionId, defaultValue, minValue, Float.MAX_VALUE);
    // default -> new RangedAttribute(descriptionId, defaultValue, minValue, maxValue);
    // };
    // }

    @Redirect(
              method = "<clinit>",
              at = @At(
                       value = "NEW",
                       target = "(Ljava/lang/String;DDD)Lnet/minecraft/world/entity/ai/attributes/RangedAttribute;",
                       ordinal = 0))
    private static RangedAttribute redirectArmorAttribute(String descriptionId, double defaultValue, double minValue, double maxValue) {
        return new RangedAttribute(descriptionId, defaultValue, minValue, Float.MAX_VALUE);
    }

    @Redirect(
              method = "<clinit>",
              at = @At(
                       value = "NEW",
                       target = "(Ljava/lang/String;DDD)Lnet/minecraft/world/entity/ai/attributes/RangedAttribute;",
                       ordinal = 1))
    private static RangedAttribute redirectArmorToughnessAttribute(String descriptionId, double defaultValue, double minValue, double maxValue) {
        return new RangedAttribute(descriptionId, defaultValue, minValue, Float.MAX_VALUE);
    }

    @Redirect(
              method = "<clinit>",
              at = @At(
                       value = "NEW",
                       target = "(Ljava/lang/String;DDD)Lnet/minecraft/world/entity/ai/attributes/RangedAttribute;",
                       ordinal = 2))
    private static RangedAttribute redirectRangeDamageAttribute(String descriptionId, double defaultValue, double minValue, double maxValue) {
        return new RangedAttribute(descriptionId, defaultValue, minValue, Float.MAX_VALUE);
    }

    @Redirect(
              method = "<clinit>",
              at = @At(
                       value = "NEW",
                       target = "(Ljava/lang/String;DDD)Lnet/minecraft/world/entity/ai/attributes/RangedAttribute;",
                       ordinal = 18))
    private static RangedAttribute redirectMaxHealthAttribute(String descriptionId, double defaultValue, double minValue, double maxValue) {
        return new RangedAttribute(descriptionId, defaultValue, minValue, Float.MAX_VALUE);
    }
}
