package net.ptcrys.breakdown.common.data;

import net.ptcrys.breakdown.api.eventdispatch.EventDistributor;
import net.ptcrys.breakdown.api.perk.PerkAttachment;
import net.ptcrys.breakdown.common.BreaRegistration;
import net.ptcrys.registrylib.util.entry.AttachmentTypeEntry;

public class BreaAttachmentTypes {

    public static void init() {}

    public static final AttachmentTypeEntry<EventDistributor> EVENT_DISTRIBUTOR = BreaRegistration.REGISTRATE.attachmentType("event_distributor", EventDistributor::new).register();

    public static final AttachmentTypeEntry<PerkAttachment> PERK_CONTROLLER = BreaRegistration.REGISTRATE.attachmentType("perk_controller", PerkAttachment::new).register();
}
