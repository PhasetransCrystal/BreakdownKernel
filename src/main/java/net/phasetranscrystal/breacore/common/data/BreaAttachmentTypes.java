package net.phasetranscrystal.breacore.common.data;

import net.phasetranscrystal.registrylib.util.entry.AttachmentTypeEntry;

import net.phasetranscrystal.breacore.api.eventdispatch.EventDistributor;
import net.phasetranscrystal.breacore.api.perk.PerkAttachment;
import net.phasetranscrystal.breacore.common.BreaRegistration;

public class BreaAttachmentTypes {

    public static void init() {}

    public static final AttachmentTypeEntry<EventDistributor> EVENT_DISTRIBUTOR = BreaRegistration.REGISTRATE.attachmentType("event_distributor", EventDistributor::new).register();

    public static final AttachmentTypeEntry<PerkAttachment> PERK_CONTROLLER = BreaRegistration.REGISTRATE.attachmentType("perk_controller", PerkAttachment::new).register();
}
