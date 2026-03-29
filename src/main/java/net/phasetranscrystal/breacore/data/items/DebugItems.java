package net.phasetranscrystal.breacore.data.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.phasetranscrystal.breacore.api.items.debug.PerkDebugItem;
import net.phasetranscrystal.breacore.data.misc.BreaCreativeModeTabs;

import com.tterrag.registrate.util.entry.ItemEntry;

import static net.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;
import static net.phasetranscrystal.breacore.data.items.BreaItems.*;
import static net.phasetranscrystal.breacore.data.tags.CustomTags.DEBUG_ITEMS;

public class DebugItems {

    public static final ItemEntry<PerkDebugItem> PERK_DEBUG = REGISTRATE
            .item("perk_debug", PerkDebugItem::new)
            .lang("Entity Perk Debug Item")
            .register();

    static {
        REGISTRATE.creativeModeTab(() -> BreaCreativeModeTabs.DEBUG_ITEMS);
    }

    public static void init() {
    }
}
