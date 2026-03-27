package net.phasetranscrystal.breacore.common.registry;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.phasetranscrystal.breacore.api.eventdispatch.EventDistributor;

public class AttachmentTypeRegistry {
    public static void bootstrap() {
    }

    public static final RegistryEntry<AttachmentType<?>, AttachmentType<EventDistributor>> EVENT_DISTRIBUTOR =
            BreaRegistration.REGISTRATE.simple("event_distributor", NeoForgeRegistries.Keys.ATTACHMENT_TYPES, () -> AttachmentType.builder(EventDistributor::new).build());
}
