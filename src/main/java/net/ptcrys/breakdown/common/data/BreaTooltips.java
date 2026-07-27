package net.ptcrys.breakdown.common.data;

import net.ptcrys.breakdown.api.annotation.DataGeneratorScanned;
import net.ptcrys.breakdown.api.annotation.RegisterLanguage;
import net.ptcrys.registrylib.tooltip.SubNode;

import net.minecraft.network.chat.Component;

@DataGeneratorScanned
public class BreaTooltips {

    @RegisterLanguage(cn = "开发物品", en = "Debug")
    public static String Debug = "breakdown.tooltip.debug";

    public static void init() {}

    public static SubNode basic(String key) {
        return new SubNode.Basic(Component.translatable(key));
    }

    public static SubNode basic(String key, int priority) {
        return new SubNode.Basic(Component.translatable(key), priority);
    }
}
