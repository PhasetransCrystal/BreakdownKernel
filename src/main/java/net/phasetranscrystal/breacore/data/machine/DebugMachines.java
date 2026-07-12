package net.phasetranscrystal.breacore.data.machine;

import net.phasetranscrystal.breacore.common.data.BreaTags;
import net.phasetranscrystal.breacore.common.data.BreaTooltips;
import net.phasetranscrystal.breacore.common.machine.EmptyMachine;

import net.minecraft.network.chat.Component;

import static net.phasetranscrystal.breacore.common.BreaRegistration.*;
import static net.phasetranscrystal.breacore.common.data.BreaCreativeModeTabs.*;
import static net.phasetranscrystal.breacore.common.data.BreaMachines.*;

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
