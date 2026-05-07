package net.phasetranscrystal.breacore.mixins;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.phasetranscrystal.breacore.api.damage.BreaDamageSource;
import net.phasetranscrystal.breacore.api.damage.DamageCalculator;
import net.phasetranscrystal.breacore.api.damage.DamageRuntimeContext;
import net.phasetranscrystal.breacore.api.damage.SimpleDamageArmorContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDamageMixin {

    @Redirect(
            method = "hurtServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V"
            )
    )
    private void breacore$replaceDamageForActuallyHurt(LivingEntity instance, ServerLevel level, DamageSource source, float dmg) {
        BreaDamageSource breaDamageSource = breacore$tryConvertSource(source);
        if (breaDamageSource == null) {
            ((LivingEntityAccessor) instance).breacore$invokeActuallyHurt(level, source, dmg);
            return;
        }

        LivingEntity victim = instance;
        Entity rootAttacker = breaDamageSource.getEntity();
        Entity directAttacker = breaDamageSource.getDirectEntity();
        ItemStack weapon = rootAttacker instanceof LivingEntity livingAttacker
                ? livingAttacker.getMainHandItem()
                : ItemStack.EMPTY;
        SimpleDamageArmorContext armorContext = new SimpleDamageArmorContext(rootAttacker, directAttacker, victim, weapon);

        float replacedDamage = (float) DamageCalculator
                .prepareForVanillaApply(breaDamageSource, armorContext, dmg)
                .getFinalDamage();

        boolean invokeSucceeded = false;
        try {
            ((LivingEntityAccessor) instance).breacore$invokeActuallyHurt(level, source, replacedDamage);
            invokeSucceeded = true;
        } finally {
            if (!invokeSucceeded) {
                DamageRuntimeContext.clearArmorDurability(victim, source);
                DamageRuntimeContext.clearPendingCalculation(victim, source);
            }
        }
    }

    @Inject(method = "hurtServer", at = @At("RETURN"))
    private void breacore$finalizeAfterVanillaHurt(
            ServerLevel level,
            DamageSource source,
            float damage,
            CallbackInfoReturnable<Boolean> cir
    ) {
        BreaDamageSource breaDamageSource = breacore$tryConvertSource(source);
        if (breaDamageSource == null) {
            return;
        }

        LivingEntity victim = (LivingEntity) (Object) this;
        DamageCalculator.finalizePendingForVanillaApply(victim, source, cir.getReturnValue());
    }

    private BreaDamageSource breacore$tryConvertSource(DamageSource source) {
        if (source instanceof BreaDamageSource breaDamageSource) {
            return breaDamageSource;
        }
        // TODO 预留：将非 BreaDamageSource 的原版伤害源转接为 BreaDamageSource。
        return null;
    }
}
