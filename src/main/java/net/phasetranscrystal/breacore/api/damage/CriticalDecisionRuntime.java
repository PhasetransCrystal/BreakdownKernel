package net.phasetranscrystal.breacore.api.damage;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 运行时暴击决议缓存（static cache + consume-once）。
 */
public final class CriticalDecisionRuntime {

    public record Decision(boolean critical, double bonusMultiplier) {}

    private record Key(int attackerId, int victimId) {}

    private static final Map<Key, Decision> DECISIONS = new ConcurrentHashMap<>();

    private CriticalDecisionRuntime() {}

    public static void record(Player attacker, Entity victim, boolean critical, double bonusMultiplier) {
        if (attacker == null || victim == null) {
            return;
        }
        DECISIONS.put(new Key(attacker.getId(), victim.getId()), new Decision(critical, Math.max(0.0, bonusMultiplier)));
    }

    public static @Nullable Decision consume(Player attacker, Entity victim) {
        if (attacker == null || victim == null) {
            return null;
        }
        return DECISIONS.remove(new Key(attacker.getId(), victim.getId()));
    }

    public static void clear(Player attacker, Entity victim) {
        if (attacker == null || victim == null) {
            return;
        }
        DECISIONS.remove(new Key(attacker.getId(), victim.getId()));
    }
}
