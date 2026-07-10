package net.phasetranscrystal.breacore.common.data;

import net.phasetranscrystal.registrylib.tooltip.SubNode;

import net.phasetranscrystal.breacore.api.machine.MachineDefinition;
import net.phasetranscrystal.breacore.common.machine.EmptyMachine;
import net.phasetranscrystal.breacore.data.machine.DebugMachines;

import net.minecraft.network.chat.Component;

import static net.phasetranscrystal.breacore.common.BreaRegistration.REGISTRATE;
import static net.phasetranscrystal.breacore.common.data.BreaCreativeModeTabs.MACHINE;

public class BreaMachines {

    static {
        REGISTRATE.defaultCreativeTab(MACHINE.getKey());
    }

    public static MachineDefinition TestMachine = REGISTRATE.machine("test_machine", EmptyMachine::new)
            .langValue("Test Machine")
            .itemBuilder(b -> b.addTag(BreaTags.DEBUG_ITEM))
            .blockBuilder(b -> b.addTag(BreaTags.DEBUG_BLOCK))
            .tooltips((collector, i) -> {
                collector.node(new SubNode.Basic(Component.literal("§aTier 1"), 0), true, false);
                collector.node(new SubNode.Basic(Component.literal("§7Tick interval: 20"), 10));
            })
            .register();

    public static void init() {
        DebugMachines.init();
    }
}
