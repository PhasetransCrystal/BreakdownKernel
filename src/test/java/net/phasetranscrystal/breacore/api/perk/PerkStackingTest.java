package net.phasetranscrystal.breacore.api.perk;

import net.phasetranscrystal.breacore.api.registry.BreaRegistries;
import net.phasetranscrystal.brealib.BreaLib;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PerkStackingTest {

    @ParameterizedTest
    @MethodSource("stackingTestProvider")
    void testStackingTypes(PerkStackingType type, List<Float> levels, float expected) {
        Perk perk = new Perk(BreaLib.id("test_" + type.name().toLowerCase()), type) {};
        assertEquals(expected, perk.calculateLevel(levels));
    }

    static Stream<Arguments> stackingTestProvider() {
        return Stream.of(
            Arguments.of(PerkStackingType.MAX, List.of(1.0f, 2.0f, 3.0f), 3.0f),
            Arguments.of(PerkStackingType.MAX, List.of(-1.0f, 0.0f, 5.0f), 5.0f),
            Arguments.of(PerkStackingType.MAX, List.of(2.5f, 2.5f, 2.5f), 2.5f),
            
            Arguments.of(PerkStackingType.SUM, List.of(1.0f, 2.0f, 3.0f), 6.0f),
            Arguments.of(PerkStackingType.SUM, List.of(-1.0f, 1.0f, 2.0f), 2.0f),
            Arguments.of(PerkStackingType.SUM, List.of(0.5f, 0.5f, 0.5f), 1.5f),
            
            Arguments.of(PerkStackingType.MIN, List.of(1.0f, 2.0f, 3.0f), 1.0f),
            Arguments.of(PerkStackingType.MIN, List.of(-1.0f, 0.0f, 5.0f), -1.0f),
            Arguments.of(PerkStackingType.MIN, List.of(2.5f, 2.5f, 2.5f), 2.5f),
            
            Arguments.of(PerkStackingType.AVERAGE, List.of(1.0f, 2.0f, 3.0f), 2.0f),
            Arguments.of(PerkStackingType.AVERAGE, List.of(0.0f, 2.0f, 4.0f), 2.0f),
            Arguments.of(PerkStackingType.AVERAGE, List.of(3.0f), 3.0f)
        );
    }

    @Test
    void testEmptyLevels() {
        for (PerkStackingType type : PerkStackingType.values()) {
            Perk perk = new Perk(BreaLib.id("test_" + type.name()), type) {};
            assertEquals(0f, perk.calculateLevel(List.of()));
        }
    }

    @Test
    void testSingleLevel() {
        for (PerkStackingType type : PerkStackingType.values()) {
            Perk perk = new Perk(BreaLib.id("test_" + type.name()), type) {};
            assertEquals(5.0f, perk.calculateLevel(List.of(5.0f)));
        }
    }

    @Test
    void testNullLevels() {
        Perk perk = new Perk(BreaLib.id("test"), PerkStackingType.SUM) {};
        assertEquals(0f, perk.calculateLevel(null));
    }

    @Test
    void testAllZeroLevels() {
        for (PerkStackingType type : PerkStackingType.values()) {
            Perk perk = new Perk(BreaLib.id("test_" + type.name()), type) {};
            assertEquals(0f, perk.calculateLevel(List.of(0f, 0f, 0f)));
        }
    }

    @Test
    void testNegativeLevels() {
        Perk maxPerk = new Perk(BreaLib.id("max"), PerkStackingType.MAX) {};
        Perk sumPerk = new Perk(BreaLib.id("sum"), PerkStackingType.SUM) {};
        
        assertEquals(-1.0f, maxPerk.calculateLevel(List.of(-1.0f, -2.0f, -3.0f)));
        assertEquals(-6.0f, sumPerk.calculateLevel(List.of(-1.0f, -2.0f, -3.0f)));
    }
}
