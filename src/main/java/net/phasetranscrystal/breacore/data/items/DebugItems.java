package net.phasetranscrystal.breacore.data.items;

import net.phasetranscrystal.brealib.BreaLib;

import net.phasetranscrystal.breacore.BreakdownCore;
import net.phasetranscrystal.breacore.common.data.BreaCreativeModeTabs;
import net.phasetranscrystal.registrylib.tooltip.SubNode;
import net.phasetranscrystal.registrylib.tooltip.TooltipRegistry;
import net.phasetranscrystal.registrylib.util.entry.ItemEntry;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import static net.phasetranscrystal.breacore.common.BreaRegistration.REGISTRATE;

public class DebugItems {

    static {
        REGISTRATE.defaultCreativeTab(BreaCreativeModeTabs.DEBUG_ITEMS.getKey());
    }
    static {
        REGISTRATE.defaultCreativeTab(BreaCreativeModeTabs.ITEM.getKey());
    }

    public static ItemEntry<Item> TestItem = REGISTRATE.item("test_item")
            .addTooltip((collector, stack) -> {
                var box1 = TooltipRegistry.rootNode(BreaLib.id("test_item").toString(), 1, true);
                collector.node(box1, new SubNode.Basic(Component.literal("§bDetailed Information"), 0));
                var box2 = TooltipRegistry.rootNode(BreakdownCore.MOD_ID + ":detail_box2", 1, true);
                collector.node(box2, new SubNode.Basic(Component.literal("§7Fire resistant"), 10));
                var box3 = TooltipRegistry.rootNode(BreakdownCore.MOD_ID + ":detail_box3", 9, true);
                for (int i = 1; i <= 28; i++) {
                    collector.node(box3, new SubNode.Basic(Component.literal("§7Arcane calibration line §f" + i), 10 + i));
                }
            })
            .register();

    public static void init() {}
}
