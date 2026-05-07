package net.phasetranscrystal.breacore.api.damage;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.phasetranscrystal.breacore.api.event.DamageCalculationEvent;

import java.util.ArrayDeque;
import java.util.Deque;

public final class DamageRuntimeContext {

    private static final ThreadLocal<Deque<ArmorDurabilityEntry>> ARMOR_DURABILITY_STACK = ThreadLocal.withInitial(ArrayDeque::new);
    private static final ThreadLocal<Deque<PendingCalculationEntry>> PENDING_CALCULATION_STACK = ThreadLocal.withInitial(ArrayDeque::new);

    private DamageRuntimeContext() {
    }

    public static void pushArmorDurability(LivingEntity victim, DamageSource source, double durabilityLoss) {
        ARMOR_DURABILITY_STACK.get().push(new ArmorDurabilityEntry(victim, source, Math.max(0.0, durabilityLoss)));
    }

    public static double consumeArmorDurability(LivingEntity victim, DamageSource source) {
        for (ArmorDurabilityEntry entry : ARMOR_DURABILITY_STACK.get()) {
            if (entry.victim == victim && entry.source == source) {
                return entry.pendingDurabilityLoss;
            }
        }
        return -1.0;
    }

    public static void recordAppliedArmorDurability(LivingEntity victim, DamageSource source, double appliedLoss) {
        for (ArmorDurabilityEntry entry : ARMOR_DURABILITY_STACK.get()) {
            if (entry.victim == victim && entry.source == source) {
                entry.appliedDurabilityLoss = Math.max(0.0, appliedLoss);
                return;
            }
        }
    }

    public static double pullAppliedArmorDurability(LivingEntity victim, DamageSource source) {
        for (ArmorDurabilityEntry entry : ARMOR_DURABILITY_STACK.get()) {
            if (entry.victim == victim && entry.source == source) {
                return Math.max(0.0, entry.appliedDurabilityLoss);
            }
        }
        return 0.0;
    }

    public static void clearArmorDurability(LivingEntity victim, DamageSource source) {
        Deque<ArmorDurabilityEntry> stack = ARMOR_DURABILITY_STACK.get();
        ArmorDurabilityEntry found = null;
        for (ArmorDurabilityEntry entry : stack) {
            if (entry.victim == victim && entry.source == source) {
                found = entry;
                break;
            }
        }
        if (found != null) {
            stack.remove(found);
        }
        if (stack.isEmpty()) {
            ARMOR_DURABILITY_STACK.remove();
        }
    }

    public static void clearPendingCalculation(LivingEntity victim, DamageSource source) {
        Deque<PendingCalculationEntry> stack = PENDING_CALCULATION_STACK.get();
        PendingCalculationEntry found = null;
        for (PendingCalculationEntry entry : stack) {
            if (entry.victim == victim && entry.source == source) {
                found = entry;
                break;
            }
        }
        if (found != null) {
            stack.remove(found);
        }
        if (stack.isEmpty()) {
            PENDING_CALCULATION_STACK.remove();
        }
    }

    public static void pushPendingCalculation(
            LivingEntity victim,
            DamageSource source,
            DamageArmorContext armorContext,
            DamageCalculationEvent.Pre preEvent
    ) {
        PENDING_CALCULATION_STACK.get().push(new PendingCalculationEntry(victim, source, armorContext, preEvent));
    }

    public static PendingCalculationEntry pullPendingCalculation(LivingEntity victim, DamageSource source) {
        Deque<PendingCalculationEntry> stack = PENDING_CALCULATION_STACK.get();
        PendingCalculationEntry found = null;
        for (PendingCalculationEntry entry : stack) {
            if (entry.victim == victim && entry.source == source) {
                found = entry;
                break;
            }
        }
        if (found != null) {
            stack.remove(found);
        }
        if (stack.isEmpty()) {
            PENDING_CALCULATION_STACK.remove();
        }
        return found;
    }

    private static final class ArmorDurabilityEntry {
        private final LivingEntity victim;
        private final DamageSource source;
        private final double pendingDurabilityLoss;
        private double appliedDurabilityLoss;

        private ArmorDurabilityEntry(LivingEntity victim, DamageSource source, double pendingDurabilityLoss) {
            this.victim = victim;
            this.source = source;
            this.pendingDurabilityLoss = pendingDurabilityLoss;
            this.appliedDurabilityLoss = 0.0;
        }
    }

    public static final class PendingCalculationEntry {
        private final LivingEntity victim;
        private final DamageSource source;
        private final DamageArmorContext armorContext;
        private final DamageCalculationEvent.Pre preEvent;

        private PendingCalculationEntry(
                LivingEntity victim,
                DamageSource source,
                DamageArmorContext armorContext,
                DamageCalculationEvent.Pre preEvent
        ) {
            this.victim = victim;
            this.source = source;
            this.armorContext = armorContext;
            this.preEvent = preEvent;
        }

        public DamageArmorContext getArmorContext() {
            return armorContext;
        }

        public DamageCalculationEvent.Pre getPreEvent() {
            return preEvent;
        }
    }
}
