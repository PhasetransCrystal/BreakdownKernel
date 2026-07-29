package net.ptcrys.breakdown.data.machine;

import net.ptcrys.breakdown.common.data.BreaTags;
import net.ptcrys.breakdown.common.data.BreaTooltips;
import net.ptcrys.breakdown.common.machine.EmptyMachine;

import net.minecraft.network.chat.Component;

import static net.ptcrys.breakdown.common.BreaRegistration.*;
import static net.ptcrys.breakdown.common.data.BreaCreativeModeTabs.*;
import static net.ptcrys.breakdown.common.data.BreaMachines.*;

public class DebugMachines {

    public static void init() {
        REGISTRATE.defaultCreativeTab(DEBUG_ITEMS.getKey());
        TestMachine = REGISTRATE.machine("test_machine", EmptyMachine::new)
                .langValue("Test Machine")
                .itemBuilder(b -> b.addTag(BreaTags.DEBUG_ITEM))
                .blockBuilder(b -> b.addTag(BreaTags.DEBUG_BLOCK))
                .addTooltip(Component.translatable(BreaTooltips.Debug))
                .register();
    }
}
