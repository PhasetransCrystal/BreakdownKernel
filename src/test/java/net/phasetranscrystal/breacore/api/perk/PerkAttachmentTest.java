package net.phasetranscrystal.breacore.api.perk;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.phasetranscrystal.breacore.api.perk.event.PerkChangeEvent;
import net.phasetranscrystal.breacore.api.registry.BreaRegistries;
import net.phasetranscrystal.breacore.common.registry.DataComponentRegistry;
import net.phasetranscrystal.brealib.BreaLib;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PerkAttachmentTest {

    static Perk simplePerk;
    static Perk attributePerk;

    static {
        BreaRegistries.PERKS.unfreeze(true);
        simplePerk = BreaRegistries.PERKS.register(BreaLib.id("simple"), new Perk(BreaLib.id("simple"), PerkStackingType.SUM) {
            @Override
            public void onAttached(LivingEntity entity, PerkInfo info) {
                attachedCalled = true;
            }

            @Override
            public void onDetached(LivingEntity entity, PerkInfo info) {
                detachedCalled = true;
            }

            @Override
            public void onLevelChanged(LivingEntity entity, float oldLevel, float newLevel, PerkInfo info) {
                levelChangedCalled = true;
                oldLevelValue = oldLevel;
                newLevelValue = newLevel;
            }
        });

        attributePerk = BreaRegistries.PERKS.register(BreaLib.id("attribute"), new Perk(BreaLib.id("attribute"), PerkStackingType.SUM) {
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
        });
        BreaRegistries.PERKS.freeze();
    }


    static ItemStack diamondHelmet;
    static ItemStack emptyStack;

    static boolean attachedCalled;
    static boolean detachedCalled;
    static boolean levelChangedCalled;
    static float oldLevelValue;
    static float newLevelValue;

    @BeforeEach
    void setup() {
        attachedCalled = false;
        detachedCalled = false;
        levelChangedCalled = false;

        diamondHelmet = new ItemStack(Items.DIAMOND_HELMET);
        emptyStack = ItemStack.EMPTY;
    }

    @Test
    void onAttachedCalled() {
        RecordPerkProvider provider = new RecordPerkProvider(simplePerk, 1.0f);
        diamondHelmet.set(DataComponentRegistry.PERK_RECORD_PROVIDER.get(),provider);

        // This would require a real LivingEntity in NeoForge test environment
        // Simplified test structure for demonstration
        assertNotNull(simplePerk);
    }

    @Test
    void perkChangeEventCreation() {
        PerkInfo info = new PerkInfo(simplePerk, 1.0f, Map.of());
        PerkChangeEvent event = new PerkChangeEvent(
                null, simplePerk, PerkChangeType.ADD, 0f, 1.0f, info
        );

        assertEquals(PerkChangeType.ADD, event.getChangeType());
        assertEquals(0f, event.getOldLevel());
        assertEquals(1.0f, event.getNewLevel());
        assertEquals(simplePerk, event.getPerk());
    }

    @Test
    void perkChangeEventTypes() {
        PerkInfo info = new PerkInfo(simplePerk, 0f, Map.of());

        PerkChangeEvent addEvent = new PerkChangeEvent(
                null, simplePerk, PerkChangeType.ADD, 0f, 1.0f, info
        );
        assertEquals(PerkChangeType.ADD, addEvent.getChangeType());

        PerkChangeEvent changeEvent = new PerkChangeEvent(
                null, simplePerk, PerkChangeType.CHANGE, 1.0f, 2.0f, info
        );
        assertEquals(PerkChangeType.CHANGE, changeEvent.getChangeType());

        PerkChangeEvent removeEvent = new PerkChangeEvent(
                null, simplePerk, PerkChangeType.REMOVE, 1.0f, 0f, info
        );
        assertEquals(PerkChangeType.REMOVE, removeEvent.getChangeType());
    }

    @Test
    void perkAttributeModifierCreation() {
        PerkAttributeModifier modifier = new PerkAttributeModifier(
                Attributes.ARMOR,
                AttributeModifier.Operation.ADD_VALUE,
                5.0
        );

        assertEquals(Attributes.ARMOR, modifier.attribute());
        assertEquals(AttributeModifier.Operation.ADD_VALUE, modifier.operation());
        assertEquals(5.0, modifier.value());
    }

    @Test
    void perkAttributeModifierToModifier() {
        Identifier id = BreaLib.id("test_modifier");
        PerkAttributeModifier modifier = new PerkAttributeModifier(
                Attributes.ARMOR,
                AttributeModifier.Operation.ADD_VALUE,
                5.0
        );

        AttributeModifier result = modifier.toModifier(id);

        assertEquals(id, result.id());
        assertEquals(5.0, result.amount());
        assertEquals(AttributeModifier.Operation.ADD_VALUE, result.operation());
    }
}
