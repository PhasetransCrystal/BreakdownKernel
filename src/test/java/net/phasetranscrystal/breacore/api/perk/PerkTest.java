package net.phasetranscrystal.breacore.api.perk;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.phasetranscrystal.breacore.api.registry.BreaRegistries;
import net.phasetranscrystal.brealib.BreaLib;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PerkTest {

    static Perk maxPerk;
    static Perk sumPerk;
    static Perk minPerk;
    static Perk avgPerk;

    static {
        BreaRegistries.PERKS.unfreeze(true);
        maxPerk = BreaRegistries.PERKS.register(BreaLib.id("test_max"), new Perk(BreaLib.id("test_max"), PerkStackingType.MAX));
        sumPerk = BreaRegistries.PERKS.register(BreaLib.id("test_sum"), new Perk(BreaLib.id("test_sum"), PerkStackingType.SUM));
        minPerk = BreaRegistries.PERKS.register(BreaLib.id("test_min"), new Perk(BreaLib.id("test_min"), PerkStackingType.MIN));
        avgPerk = BreaRegistries.PERKS.register(BreaLib.id("test_avg"), new Perk(BreaLib.id("test_avg"), PerkStackingType.AVERAGE));
        BreaRegistries.PERKS.freeze();
    }


    @Test
    void perkCreation() {
        assertEquals(BreaLib.id("test_max"), maxPerk.getId());
        assertEquals(PerkStackingType.MAX, maxPerk.getStackingType());
    }

    @Test
    void calculateLevelMax() {
        List<Float> levels = List.of(1.0f, 2.0f, 3.0f);
        assertEquals(3.0f, maxPerk.calculateLevel(levels));
    }

    @Test
    void calculateLevelSum() {
        List<Float> levels = List.of(1.0f, 2.0f, 3.0f);
        assertEquals(6.0f, sumPerk.calculateLevel(levels));
    }

    @Test
    void calculateLevelMin() {
        List<Float> levels = List.of(1.0f, 2.0f, 3.0f);
        assertEquals(1.0f, minPerk.calculateLevel(levels));
    }

    @Test
    void calculateLevelAverage() {
        List<Float> levels = List.of(1.0f, 2.0f, 3.0f);
        assertEquals(2.0f, avgPerk.calculateLevel(levels));
    }

    @Test
    void calculateLevelEmpty() {
        List<Float> empty = List.of();
        assertEquals(0f, maxPerk.calculateLevel(empty));
    }

    @Test
    void getAttributeModifierIdAddValue() {
        Identifier id = sumPerk.getAttributeModifierId(AttributeModifier.Operation.ADD_VALUE);
        assertEquals(BreaLib.id("perk_system/brealib/test_sum/stage1"), id);
    }

    @Test
    void getAttributeModifierIdMultipliedBase() {
        Identifier id = sumPerk.getAttributeModifierId(AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        assertEquals(BreaLib.id("perk_system/brealib/test_sum/stage2"), id);
    }

    @Test
    void getAttributeModifierIdMultipliedTotal() {
        Identifier id = sumPerk.getAttributeModifierId(AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        assertEquals(BreaLib.id("perk_system/brealib/test_sum/stage3"), id);
    }
}
