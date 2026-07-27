package net.ptcrys.breakdown.api.perk;

import net.ptcrys.breakdown.api.eventdispatch.EventConsumer;

import net.neoforged.bus.api.Event;

import org.apache.commons.lang3.function.TriConsumer;

public record PerkConsumer<T extends Event>(
                                            Class<T> eventType,
                                            boolean runWhenCancelled,
                                            TriConsumer<T, EventConsumer<T>, PerkInfo> triConsumer) {}
