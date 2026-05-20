package net.phasetranscrystal.breacore.common.event;

import net.phasetranscrystal.breacore.BreakdownCore;
import net.phasetranscrystal.breacore.api.damage.CriticalDecisionRuntime;
import net.phasetranscrystal.breacore.common.registry.AttributeRegistry;
import net.phasetranscrystal.breacore.utils.AttributeHelper;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

@EventBusSubscriber(modid = BreakdownCore.MOD_ID)
public final class CriticalDecisionEventHandler {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void processCriticalDecision(CriticalHitEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (event.isCriticalHit()) {
            return;
        }

        Player attacker = event.getEntity();
        double criticalChance = AttributeHelper.getValueOrDefault(attacker, AttributeRegistry.CRITICAL_HIT);
        if (attacker.getRandom().nextDouble() < Math.clamp(criticalChance, 0.0, 1.0)) {
            event.setCriticalHit(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void recordCriticalDecision(CriticalHitEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        Entity victim = event.getTarget();

        Player attacker = event.getEntity();
        boolean critical = event.isCriticalHit();
        double baselineMultiplier = event.isVanillaCritical() ? 1.5 : 1.0;
        double bonusMultiplier = Math.max(0.0, event.getDamageMultiplier() - baselineMultiplier);
        CriticalDecisionRuntime.record(attacker, victim, critical, bonusMultiplier);
        event.setDamageMultiplier(1);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void clearCriticalDecisionIfCanceled(LivingIncomingDamageEvent event) {
        if (!event.isCanceled()) {
            return;
        }
        Entity sourceEntity = event.getSource().getEntity();
        if (sourceEntity instanceof Player player) {
            CriticalDecisionRuntime.clear(player, event.getEntity());
        }
    }
}
