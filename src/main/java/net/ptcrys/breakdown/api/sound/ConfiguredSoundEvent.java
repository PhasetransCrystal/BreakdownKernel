package net.ptcrys.breakdown.api.sound;

import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public record ConfiguredSoundEvent(Supplier<SoundEvent> event, float volume, float pitch) {}
