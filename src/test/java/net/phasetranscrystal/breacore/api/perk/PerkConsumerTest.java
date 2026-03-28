package net.phasetranscrystal.breacore.api.perk;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.Event;
import net.phasetranscrystal.breacore.api.eventdispatch.EventConsumer;
import net.phasetranscrystal.brealib.BreaLib;
import org.apache.commons.lang3.function.TriConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PerkConsumerTest {

    Perk testPerk;

    @BeforeEach
    void setup() {
        testPerk = new Perk(BreaLib.id("test"), PerkStackingType.SUM) {};
    }

    @Test
    void perkConsumerCreation() {
        TriConsumer<TestEvent, EventConsumer<TestEvent>, PerkInfo> triConsumer = (e, c, p) -> {};
        
        PerkConsumer<TestEvent> consumer = new PerkConsumer<>(
            TestEvent.class,
            false,
            triConsumer
        );

        assertEquals(TestEvent.class, consumer.eventType());
        assertFalse(consumer.runWhenCancelled());
        assertNotNull(consumer.triConsumer());
    }

    @Test
    void perkConsumerWithRunWhenCancelled() {
        TriConsumer<TestEvent, EventConsumer<TestEvent>, PerkInfo> triConsumer = (e, c, p) -> {};
        
        PerkConsumer<TestEvent> consumer = new PerkConsumer<>(
            TestEvent.class,
            true,
            triConsumer
        );

        assertTrue(consumer.runWhenCancelled());
    }

    @Test
    void perkWithEventConsumers() {
        TriConsumer<TestEvent, EventConsumer<TestEvent>, PerkInfo> handler = (e, c, p) -> {
            e.handled = true;
        };

        Perk perkWithConsumers = new Perk(BreaLib.id("with_consumers"), PerkStackingType.SUM) {
            @Override
            public List<PerkConsumer<?>> getEventConsumers(PerkInfo info) {
                return List.of(
                    new PerkConsumer<>(TestEvent.class, false, handler)
                );
            }
        };

        PerkInfo info = new PerkInfo(perkWithConsumers, 1.0f, Map.of());
        List<PerkConsumer<?>> consumers = perkWithConsumers.getEventConsumers(info);

        assertEquals(1, consumers.size());
        assertEquals(TestEvent.class, consumers.get(0).eventType());
    }

    @Test
    void perkWithAttributeModifiers() {
        Perk perkWithAttributes = new Perk(BreaLib.id("with_attributes"), PerkStackingType.SUM) {
            @Override
            public Collection<PerkAttributeModifier> getAttributeModifiers(LivingEntity entity, float level) {
                return List.of(
                    new PerkAttributeModifier(
                            Attributes.ARMOR,
                        AttributeModifier.Operation.ADD_VALUE,
                        level * 2.0
                    )
                );
            }
        };

        Collection<PerkAttributeModifier> modifiers = perkWithAttributes.getAttributeModifiers(null, 3.0f);

        assertEquals(1, modifiers.size());
        PerkAttributeModifier modifier = modifiers.iterator().next();
        assertEquals(6.0, modifier.value());
    }

    @Test
    void perkInfoWithItemStacks() {
        PerkInfo info = new PerkInfo(testPerk, 2.5f, Map.of());
        
        assertEquals(testPerk, info.perk());
        assertEquals(2.5f, info.level());
        assertTrue(info.itemStacks().isEmpty());
    }

    @Test
    void perkStackCreation() {
        PerkStack stack = new PerkStack(testPerk, 1.5f);
        
        assertEquals(testPerk, stack.perk());
        assertEquals(1.5f, stack.level());
    }

    @Test
    void perkStackWithLevel() {
        PerkStack original = new PerkStack(testPerk, 1.5f);
        PerkStack modified = original.withLevel(3.0f);
        
        assertEquals(1.5f, original.level());
        assertEquals(3.0f, modified.level());
        assertEquals(testPerk, modified.perk());
    }

    static class TestEvent extends Event {
        boolean handled = false;
    }
}
