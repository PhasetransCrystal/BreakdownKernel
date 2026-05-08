package net.phasetranscrystal.breacore.mixins;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.phasetranscrystal.breacore.api.damage.BreaDamageSource;
import net.phasetranscrystal.breacore.api.damage.DamageRuntimeContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityArmorDurabilityMixin {

    @Shadow
    protected abstract void hurtArmor(DamageSource source, float damage);

    @Inject(
            method = "getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F",
            at = @At("HEAD"),
            cancellable = true
    )
    private void breacore$shortCircuitArmorProtectionForBrea(
            DamageSource damageSource,
            float damage,
            CallbackInfoReturnable<Float> cir
    ) {
        if (!(damageSource instanceof BreaDamageSource breaDamageSource)) {
            return;
        }
        LivingEntity victim = (LivingEntity) (Object) this;
        double scheduledDurabilityLoss = DamageRuntimeContext.consumeArmorDurability(victim, breaDamageSource);
        if (scheduledDurabilityLoss >= 0.0) {
            float applied = (float) Math.max(0.0, scheduledDurabilityLoss);
            DamageRuntimeContext.recordAppliedArmorDurability(victim, breaDamageSource, applied);
            this.hurtArmor(breaDamageSource, applied);
        }

        DamageRuntimeContext.RuntimeEntry pending = DamageRuntimeContext.peekCalculation(victim, breaDamageSource);
        if (pending != null) {
            pending.getArmorContext().applySpellShieldLoss(
                    breaDamageSource,
                    pending.getPreEvent().getShieldHealthLoss(),
                    pending.getPreEvent().getShieldDurabilityLoss()
            );
        }

        double reduction = DamageRuntimeContext.consumeArmorReductionForContainer(victim, breaDamageSource)
                + DamageRuntimeContext.consumeShieldReductionForContainer(victim, breaDamageSource);
        float finalDamage = (float) Math.max(0.0, damage - reduction);
        cir.setReturnValue(finalDamage);
    }
}
