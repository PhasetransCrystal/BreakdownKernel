package net.phasetranscrystal.breacore.mixins;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.phasetranscrystal.breacore.api.damage.BreaDamageSource;
import net.phasetranscrystal.breacore.api.damage.DamageRuntimeContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityArmorDurabilityMixin {

    @Shadow
    protected abstract void hurtArmor(DamageSource source, float damage);

    @Redirect(
            method = "getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtArmor(Lnet/minecraft/world/damagesource/DamageSource;F)V")
    )
    private void breacore$useCalculatedArmorDurability(LivingEntity instance, DamageSource damageSource, float damage) {
        float vanillaDamage = damage;
        if (!(damageSource instanceof BreaDamageSource)) {
            this.hurtArmor(damageSource, vanillaDamage);
            return;
        }
        LivingEntity victim = (LivingEntity) (Object) this;
        double scheduledDurabilityLoss = DamageRuntimeContext.consumeArmorDurability(victim, damageSource);
        if (scheduledDurabilityLoss < 0.0) {
            this.hurtArmor(damageSource, vanillaDamage);
            return;
        }
        float applied = (float) Math.max(0.0, scheduledDurabilityLoss);
        DamageRuntimeContext.recordAppliedArmorDurability(victim, damageSource, applied);
        this.hurtArmor(damageSource, applied);
    }
}
