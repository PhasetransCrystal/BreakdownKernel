package net.phasetranscrystal.breacore.mixins;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.phasetranscrystal.breacore.api.damage.BreaDamageSource;
import net.phasetranscrystal.breacore.api.damage.CriticalDecisionRuntime;
import net.phasetranscrystal.breacore.api.damage.DamageCalculator;
import net.phasetranscrystal.breacore.api.damage.DamageArmorContext;
import net.phasetranscrystal.breacore.api.damage.DamageRuntimeContext;
import net.phasetranscrystal.breacore.api.damage.IBreaDamageArmorContextProvider;
import net.phasetranscrystal.breacore.api.damage.IBreaDamageSourceProvider;
import net.phasetranscrystal.breacore.api.damage.SimpleDamageArmorContext;
import net.phasetranscrystal.breacore.api.damage.WeaponDamageProfile;
import net.phasetranscrystal.breacore.api.damage.event.BreaDamageArmorContextEvent;
import net.phasetranscrystal.breacore.api.damage.event.BreaDamageSourceResolveEvent;
import net.phasetranscrystal.breacore.api.damage.event.DamageCalculationEvent;
import net.phasetranscrystal.breacore.api.magic.Element;
import net.phasetranscrystal.breacore.common.registry.AttributeRegistry;
import net.phasetranscrystal.breacore.common.registry.ItemComponentRegistry;
import net.phasetranscrystal.breacore.utils.AttributeHelper;
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
        BreaDamageSource breaDamageSource = breacore$tryConvertSource(instance, source);
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
        DamageArmorContext armorContext = breacore$resolveArmorContext(victim, breaDamageSource, rootAttacker, directAttacker, weapon);

        DamageCalculationEvent.Pre preEvent = DamageCalculator.calculatePre(breaDamageSource, armorContext, dmg);

        DamageRuntimeContext.pushCalculation(
                victim,
                breaDamageSource,
                armorContext,
                preEvent,
                preEvent.getArmorDurabilityLoss(),
                preEvent.getHardArmorAbsorbedDamage() + preEvent.getSoftArmorAbsorbedDamage(),
                preEvent.getSpellAbsorbedByShield()
        );

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

    private BreaDamageSource breacore$tryConvertSource(LivingEntity victim, DamageSource source) {
        if (source instanceof BreaDamageSource breaDamageSource) {
            return breacore$applyCriticalDecision(victim, source, breaDamageSource);
        }

        BreaDamageSource resolved = null;
        if (victim instanceof IBreaDamageSourceProvider provider) {
            resolved = provider.provideBreaDamageSource(source, victim);
        }

        if (resolved == null) {
            resolved = breacore$buildFallbackBreaDamageSource(source);
        }
        resolved = breacore$applyCriticalDecision(victim, source, resolved);
        BreaDamageSourceResolveEvent event = NeoForge.EVENT_BUS.post(
                new BreaDamageSourceResolveEvent(victim, source, resolved)
        );
        return event.getDamageSource();
    }

    private BreaDamageSource breacore$buildFallbackBreaDamageSource(DamageSource source) {

        LivingEntity attacker = breacore$resolveAttackerEntity(source);
        if (attacker == null) {
            return null;
        }

        AttributeInstance hardPenetration = attacker.getAttribute(AttributeRegistry.HARD_ARMOR_PENETRATION_VALUE);
        AttributeInstance softPenetration = attacker.getAttribute(AttributeRegistry.SOFT_ARMOR_PENETRATION_VALUE);
        if (hardPenetration == null || softPenetration == null) {
            return null;
        }

        double criticalChance = AttributeHelper.getValueOrDefault(attacker, AttributeRegistry.CRITICAL_HIT);
        double criticalDamage = AttributeHelper.getValueOrDefault(attacker, AttributeRegistry.CRITICAL_DAMAGE);

        Entity directEntity = source.getDirectEntity();
        Entity causingEntity = source.getEntity();

        WeaponDamageProfile weaponDamageProfile = breacore$resolveWeaponDamageProfile(attacker);

        return new BreaDamageSource(
                source.typeHolder(),
                directEntity,
                causingEntity,
                source.sourcePositionRaw(),
                weaponDamageProfile.getElement(),
                weaponDamageProfile.getInvulnerabilityTicks(),
                weaponDamageProfile.getSpellShieldHitRatio(),
                hardPenetration.getValue(),
                softPenetration.getValue(),
                weaponDamageProfile.getHardArmorActionRatio(),
                weaponDamageProfile.getSoftArmorActionRatio(),
                criticalChance,
                criticalDamage
        );
    }

    private BreaDamageSource breacore$applyCriticalDecision(LivingEntity victim, DamageSource originalSource, BreaDamageSource resolved) {
        if (resolved == null) {
            return null;
        }
        Entity causingEntity = originalSource.getEntity();
        Entity directEntity = originalSource.getDirectEntity();
        if (!(causingEntity instanceof Player player) || directEntity != player || victim == null) {
            return resolved;
        }

        CriticalDecisionRuntime.Decision decision = CriticalDecisionRuntime.consume(player, victim);
        if (decision == null) {
            return resolved;
        }
        resolved.setCriticalDecision(decision.critical(), decision.bonusMultiplier());
        return resolved;
    }

    private WeaponDamageProfile breacore$resolveWeaponDamageProfile(LivingEntity attacker) {
        ItemStack weapon = attacker.getMainHandItem();
        if (weapon.isEmpty()) {
            return WeaponDamageProfile.GEOGRAPHY_ONLY;
        }
        WeaponDamageProfile component = weapon.get(ItemComponentRegistry.WEAPON_DAMAGE_PROFILE);
        if (component == null) {
            return WeaponDamageProfile.GEOGRAPHY_ONLY;
        }
        return component;
    }

    private DamageArmorContext breacore$resolveArmorContext(
            LivingEntity victim,
            BreaDamageSource damageSource,
            Entity rootAttacker,
            Entity directAttacker,
            ItemStack weapon
    ) {
        DamageArmorContext armorContext = null;
        if (victim instanceof IBreaDamageArmorContextProvider provider) {
            armorContext = provider.provideDamageArmorContext(damageSource, victim);
        }
        if (armorContext == null) {
            armorContext = new SimpleDamageArmorContext(rootAttacker, directAttacker, victim, weapon);
        }

        BreaDamageArmorContextEvent event = NeoForge.EVENT_BUS.post(
                new BreaDamageArmorContextEvent(victim, damageSource, armorContext)
        );
        return event.getArmorContext();
    }

    private LivingEntity breacore$resolveAttackerEntity(DamageSource source) {
        Entity root = source.getEntity();
        if (root instanceof LivingEntity livingRoot) {
            return livingRoot;
        }
        Entity direct = source.getDirectEntity();
        if (direct instanceof LivingEntity livingDirect) {
            return livingDirect;
        }
        return null;
    }

}
