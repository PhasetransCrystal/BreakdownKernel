package net.phasetranscrystal.breacore.api.damage;

import net.minecraft.world.entity.LivingEntity;
import net.phasetranscrystal.breacore.api.damage.event.DamageCalculationEvent;

import java.util.ArrayDeque;
import java.util.Deque;

public final class DamageRuntimeContext {

    private static final ThreadLocal<Deque<RuntimeEntry>> RUNTIME_STACK = ThreadLocal.withInitial(ArrayDeque::new);

    private DamageRuntimeContext() {
    }

    public static void pushCalculation(
            LivingEntity victim,
            BreaDamageSource source,
            DamageArmorContext armorContext,
            DamageCalculationEvent.Pre preEvent,
            double durabilityLoss,
            double armorReductionForContainer,
            double shieldReductionForContainer
    ) {
        RUNTIME_STACK.get().push(
                new RuntimeEntry(
                        victim,
                        source,
                        armorContext,
                        preEvent,
                        Math.max(0.0, durabilityLoss),
                        Math.max(0.0, armorReductionForContainer),
                        Math.max(0.0, shieldReductionForContainer)
                )
        );
    }

    public static double consumeArmorDurability(LivingEntity victim, BreaDamageSource source) {
        for (RuntimeEntry entry : RUNTIME_STACK.get()) {
            if (entry.victim == victim && entry.source == source) {
                return entry.pendingDurabilityLoss;
            }
        }
        return -1.0;
    }

    public static void recordAppliedArmorDurability(LivingEntity victim, BreaDamageSource source, double appliedLoss) {
        for (RuntimeEntry entry : RUNTIME_STACK.get()) {
            if (entry.victim == victim && entry.source == source) {
                entry.appliedDurabilityLoss = Math.max(0.0, appliedLoss);
                return;
            }
        }
    }

    public static double pullAppliedArmorDurability(LivingEntity victim, BreaDamageSource source) {
        for (RuntimeEntry entry : RUNTIME_STACK.get()) {
            if (entry.victim == victim && entry.source == source) {
                return Math.max(0.0, entry.appliedDurabilityLoss);
            }
        }
        return 0.0;
    }

    public static double consumeArmorReductionForContainer(LivingEntity victim, BreaDamageSource source) {
        for (RuntimeEntry entry : RUNTIME_STACK.get()) {
            if (entry.victim == victim && entry.source == source) {
                return Math.max(0.0, entry.armorReductionForContainer);
            }
        }
        return 0.0;
    }

    public static double consumeShieldReductionForContainer(LivingEntity victim, BreaDamageSource source) {
        for (RuntimeEntry entry : RUNTIME_STACK.get()) {
            if (entry.victim == victim && entry.source == source) {
                return Math.max(0.0, entry.shieldReductionForContainer);
            }
        }
        return 0.0;
    }

    public static void clearCalculation(LivingEntity victim, BreaDamageSource source) {
        Deque<RuntimeEntry> stack = RUNTIME_STACK.get();
        RuntimeEntry found = findByIdentity(stack, victim, source);
        if (found != null) {
            stack.remove(found);
        }
        if (stack.isEmpty()) {
            RUNTIME_STACK.remove();
        }
    }

    public static RuntimeEntry peekCalculation(LivingEntity victim, BreaDamageSource source) {
        for (RuntimeEntry entry : RUNTIME_STACK.get()) {
            if (entry.victim == victim && entry.source == source) {
                return entry;
            }
        }
        return null;
    }

    private static RuntimeEntry findByIdentity(Deque<RuntimeEntry> stack, LivingEntity victim, BreaDamageSource source) {
        for (RuntimeEntry entry : stack) {
            if (entry.victim == victim && entry.source == source) {
                return entry;
            }
        }
        return null;
    }

    public static final class RuntimeEntry {
        private final LivingEntity victim;
        private final BreaDamageSource source;
        private final DamageArmorContext armorContext;
        private final DamageCalculationEvent.Pre preEvent;
        private final double pendingDurabilityLoss;
        private final double armorReductionForContainer;
        private final double shieldReductionForContainer;
        private double appliedDurabilityLoss;

        private RuntimeEntry(
                LivingEntity victim,
                BreaDamageSource source,
                DamageArmorContext armorContext,
                DamageCalculationEvent.Pre preEvent,
                double pendingDurabilityLoss,
                double armorReductionForContainer,
                double shieldReductionForContainer
        ) {
            this.victim = victim;
            this.source = source;
            this.armorContext = armorContext;
            this.preEvent = preEvent;
            this.pendingDurabilityLoss = pendingDurabilityLoss;
            this.armorReductionForContainer = armorReductionForContainer;
            this.shieldReductionForContainer = shieldReductionForContainer;
            this.appliedDurabilityLoss = 0.0;
        }

        public DamageArmorContext getArmorContext() {
            return armorContext;
        }

        public DamageCalculationEvent.Pre getPreEvent() {
            return preEvent;
        }
    }
}
