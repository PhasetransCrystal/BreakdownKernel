package net.phasetranscrystal.breacore.common.registry;

import net.phasetranscrystal.breacore.api.eventdispatch.EventDistributor;
import net.phasetranscrystal.breacore.api.perk.PerkAttachment;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import com.tterrag.registrate.util.entry.RegistryEntry;

public class AttachmentTypeRegistry {

    public static void bootstrap() {}

    public static final RegistryEntry<AttachmentType<?>, AttachmentType<EventDistributor>> EVENT_DISTRIBUTOR = BreaRegistration.REGISTRATE.simple("event_distributor", NeoForgeRegistries.Keys.ATTACHMENT_TYPES, () -> AttachmentType.builder(EventDistributor::new).build());

    public static final RegistryEntry<AttachmentType<?>, AttachmentType<PerkAttachment>> PERK_CONTROLLER = BreaRegistration.REGISTRATE.simple("perk_controller", NeoForgeRegistries.Keys.ATTACHMENT_TYPES, () -> AttachmentType.builder(PerkAttachment::new).build());
}
