package net.phasetranscrystal.breacore.common.data;

import net.phasetranscrystal.registrylib.tooltip.SubNode;

import net.phasetranscrystal.breacore.api.annotation.DataGeneratorScanned;
import net.phasetranscrystal.breacore.api.annotation.RegisterLanguage;

import net.minecraft.network.chat.Component;

@DataGeneratorScanned
public class BreaTooltips {

    @RegisterLanguage(cn = "开发物品", en = "Debug")
    public static String Debug = "breacore.tooltip.debug";

    public static void init() {}

    public static SubNode basic(String key) {
        return new SubNode.Basic(Component.translatable(key));
    }

    public static SubNode basic(String key, int priority) {
        return new SubNode.Basic(Component.translatable(key), priority);
    }
}
