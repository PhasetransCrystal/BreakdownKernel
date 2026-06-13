package net.phasetranscrystal.breacore.common.data;

import net.phasetranscrystal.breacore.BreakdownCore;
import net.phasetranscrystal.breacore.api.eventdispatch.EventDistributor;
import net.phasetranscrystal.breacore.api.perk.PerkAttachment;
import net.phasetranscrystal.registrylib.util.entry.AttachmentTypeEntry;

public class BreaAttachmentTypes {

    public static void init() {}

    public static final AttachmentTypeEntry<EventDistributor> EVENT_DISTRIBUTOR = BreakdownCore.REGISTRATE.attachmentType("event_distributor", EventDistributor::new).register();

    public static final AttachmentTypeEntry<PerkAttachment> PERK_CONTROLLER = BreakdownCore.REGISTRATE.attachmentType("perk_controller", PerkAttachment::new).register();
}
