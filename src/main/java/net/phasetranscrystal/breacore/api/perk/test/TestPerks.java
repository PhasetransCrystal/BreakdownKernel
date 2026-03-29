package net.phasetranscrystal.breacore.api.perk.test;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.phasetranscrystal.breacore.api.perk.*;
import net.phasetranscrystal.breacore.api.registry.BreaRegistries;
import net.phasetranscrystal.brealib.BreaLib;

import java.util.Collection;
import java.util.List;

public class TestPerks {
    public static void bootstrap(){
    }

    public static boolean attachedCalled;
    public static boolean detachedCalled;
    public static boolean levelChangedCalled;
    public static float oldLevelValue;
    public static float newLevelValue;
    public static int eventTriggerCount;
    public static float eventTriggerPerkLevel;

    public static Perk SUM;
    public static Perk MAX;
    public static Perk MIN;
    public static Perk AVERAGE;

    public static Perk ARMOR_PERK;
    public static Perk SPEED_PERK;
    public static Perk HEALTH_PERK;

    public static Perk EVENT_PERK;

    public static Perk COMBO_PERK;
    public static Perk COMBO_PERK_2;

    public static Perk COMBINED_A;
    public static Perk COMBINED_B;

    static {
        BreaRegistries.PERKS.unfreeze(true);

        SUM = BreaRegistries.PERKS.register(BreaLib.id("test_sum"), new Perk(PerkStackingType.SUM) {
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

        MAX = BreaRegistries.PERKS.register(BreaLib.id("test_max"), new Perk(PerkStackingType.MAX) {});

        MIN = BreaRegistries.PERKS.register(BreaLib.id("test_min"), new Perk(PerkStackingType.MIN) {});

        AVERAGE = BreaRegistries.PERKS.register(BreaLib.id("test_average"), new Perk(PerkStackingType.AVERAGE) {});

        ARMOR_PERK = BreaRegistries.PERKS.register(BreaLib.id("test_armor"), new Perk(PerkStackingType.SUM) {
            @Override
            public Collection<PerkAttributeModifier> getAttributeModifiers(LivingEntity entity, float level) {
                return List.of(new PerkAttributeModifier(
                        Attributes.ARMOR,
                        AttributeModifier.Operation.ADD_VALUE,
                        level * 2.0
                ));
            }
        });

        SPEED_PERK = BreaRegistries.PERKS.register(BreaLib.id("test_speed"), new Perk(PerkStackingType.SUM) {
            @Override
            public Collection<PerkAttributeModifier> getAttributeModifiers(LivingEntity entity, float level) {
                return List.of(new PerkAttributeModifier(
                        Attributes.MOVEMENT_SPEED,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                        level * 0.1
                ));
            }
        });

        HEALTH_PERK = BreaRegistries.PERKS.register(BreaLib.id("test_health"), new Perk(PerkStackingType.SUM) {
            @Override
            public Collection<PerkAttributeModifier> getAttributeModifiers(LivingEntity entity, float level) {
                return List.of(new PerkAttributeModifier(
                        Attributes.MAX_HEALTH,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                        level * 0.2
                ));
            }
        });

        EVENT_PERK = BreaRegistries.PERKS.register(BreaLib.id("test_event"), new Perk(PerkStackingType.SUM) {
            @Override
            public List<PerkConsumer<?>> getEventConsumers(PerkInfo info) {
                return List.of(new PerkConsumer<>(
                        LivingDamageEvent.Pre.class,
                        false,
                        (event, consumer, perkInfo) -> {
                            eventTriggerCount++;
                        }
                ));
            }
        });

        COMBO_PERK = BreaRegistries.PERKS.register(BreaLib.id("test_combo"), new Perk(PerkStackingType.SUM) {
            @Override
            public Collection<PerkAttributeModifier> getAttributeModifiers(LivingEntity entity, float level) {
                return List.of(new PerkAttributeModifier(
                        Attributes.ARMOR_TOUGHNESS,
                        AttributeModifier.Operation.ADD_VALUE,
                        level * 1.5
                ));
            }

            @Override
            public List<PerkConsumer<?>> getEventConsumers(PerkInfo info) {
                return List.of(new PerkConsumer<>(
                        LivingDamageEvent.Pre.class,
                        false,
                        (event, consumer, perkInfo) -> {
                            eventTriggerCount++;
                            eventTriggerPerkLevel = perkInfo.level();
                        }
                ));
            }
        });

        COMBO_PERK_2 = BreaRegistries.PERKS.register(BreaLib.id("test_combo_2"), new Perk(PerkStackingType.SUM) {
            @Override
            public Collection<PerkAttributeModifier> getAttributeModifiers(LivingEntity entity, float level) {
                return List.of(new PerkAttributeModifier(
                        Attributes.ATTACK_KNOCKBACK,
                        AttributeModifier.Operation.ADD_VALUE,
                        level * 0.5
                ));
            }

            @Override
            public List<PerkConsumer<?>> getEventConsumers(PerkInfo info) {
                return List.of(new PerkConsumer<>(
                        LivingDamageEvent.class,
                        false,
                        (event, consumer, perkInfo) -> {
                            eventTriggerCount++;
                        }
                ));
            }
        });

        COMBINED_A = BreaRegistries.PERKS.register(BreaLib.id("test_combined_a"), new Perk(PerkStackingType.SUM) {
            @Override
            public Collection<PerkAttributeModifier> getAttributeModifiers(LivingEntity entity, float level) {
                return List.of(new PerkAttributeModifier(
                        Attributes.ARMOR,
                        AttributeModifier.Operation.ADD_VALUE,
                        level * 3.0
                ));
            }

            @Override
            public List<PerkConsumer<?>> getEventConsumers(PerkInfo info) {
                return List.of(new PerkConsumer<>(
                        LivingDamageEvent.Pre.class,
                        false,
                        (event, consumer, perkInfo) -> {
                            eventTriggerCount++;
                        }
                ));
            }
        });

        COMBINED_B = BreaRegistries.PERKS.register(BreaLib.id("test_combined_b"), new Perk(PerkStackingType.MAX) {
            @Override
            public Collection<PerkAttributeModifier> getAttributeModifiers(LivingEntity entity, float level) {
                return List.of(new PerkAttributeModifier(
                        Attributes.ATTACK_DAMAGE,
                        AttributeModifier.Operation.ADD_VALUE,
                        level * 5.0
                ));
            }

            @Override
            public List<PerkConsumer<?>> getEventConsumers(PerkInfo info) {
                return List.of(new PerkConsumer<>(
                        LivingDamageEvent.Pre.class,
                        false,
                        (event, consumer, perkInfo) -> {
                            eventTriggerCount++;
                        }
                ));
            }
        });

        BreaRegistries.PERKS.freeze();
    }

    public static void resetFlags() {
        attachedCalled = false;
        detachedCalled = false;
        levelChangedCalled = false;
        oldLevelValue = 0f;
        newLevelValue = 0f;
        eventTriggerCount = 0;
        eventTriggerPerkLevel = 0f;
    }
}
