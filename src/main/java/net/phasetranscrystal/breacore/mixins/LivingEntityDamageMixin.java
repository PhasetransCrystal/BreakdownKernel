package net.phasetranscrystal.breacore.mixins;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.phasetranscrystal.breacore.api.damage.BreaDamageSource;
import net.phasetranscrystal.breacore.api.damage.DamageCalculator;
import net.phasetranscrystal.breacore.api.damage.DamageRuntimeContext;
import net.phasetranscrystal.breacore.api.damage.SimpleDamageArmorContext;
import net.phasetranscrystal.breacore.api.damage.event.DamageCalculationEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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

        DamageCalculationEvent.Pre preEvent = DamageCalculator
                .prepareForVanillaApply(breaDamageSource, armorContext, dmg);
        double weightedUnabsorbedWithCriticalRule =
                preEvent.getSpellWeightedDamage() +
                preEvent.getPhysicalWeightedDamage();
        float replacedDamage = (float) Math.max(0.0, weightedUnabsorbedWithCriticalRule);

        var containers = ((LivingEntityAccessor) instance).breacore$getDamageContainers();
        if (!containers.isEmpty()) {
            DamageContainer damageContainer = containers.peek();
            damageContainer.setNewDamage(Math.max(0.0F, replacedDamage));
        }

        float beforeHealth = victim.getHealth();
        try {
            ((LivingEntityAccessor) instance).breacore$invokeActuallyHurt(level, breaDamageSource, replacedDamage);
            boolean damageApplied = victim.getHealth() < beforeHealth || victim.isDeadOrDying();
            if (damageApplied) {
                victim.invulnerableTime = breaDamageSource.getInvulnerabilityTicks();
            }
            DamageCalculator.finalizePendingForVanillaApply(victim, breaDamageSource, damageApplied);
        } finally {
            DamageRuntimeContext.clearCalculation(victim, breaDamageSource);
        }
    }

    private BreaDamageSource breacore$tryConvertSource(DamageSource source) {
        if (source instanceof BreaDamageSource breaDamageSource) {
            return breaDamageSource;
        }
        // TODO 预留：将非 BreaDamageSource 的原版伤害源转接为 BreaDamageSource。
        return null;
    }
}
