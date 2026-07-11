package net.phasetranscrystal.breacore.data.lang;

import static net.phasetranscrystal.breacore.data.lang.LangHandler.addCNEN;

public class MachineLang {

    public static void init() {
        addCNEN("gui.tooltips.redstone_mode.enabled", "启用红石模式，机器接收红石信号", "Enable redstone mode, the machine will receive redstone signals");
        addCNEN("gui.tooltips.redstone_mode.disabled", "禁用红石模式，机器无视红石信号", "Disable redstone mode, the machine will ignore redstone signals");
    }
}
