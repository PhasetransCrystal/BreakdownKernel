package net.ptcrys.breakdown.data.vanilla;

import net.ptcrys.breakdown.BreaLib;
import net.ptcrys.registrylib.util.entry.RegistryEntry;

import net.minecraft.world.item.CreativeModeTab;

import java.util.Map;

public final class BdCreativeTabs {

    public static RegistryEntry<CreativeModeTab, CreativeModeTab> DEBUG_ITEMS = BreaLib.REGISTRY.creativeTab("debug_items", "Brea | Debug", Map.of("zh_cn", "瓦解 | Debug"));
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> CREATIVE_MODE_ITEMS = BreaLib.REGISTRY.creativeTab("creative_mode_items", "Brea | Creative Mode", Map.of("zh_cn", "瓦解 | 创造"));

    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_ITEM = BreaLib.REGISTRY.creativeTab("material_item", "Brea | Items", Map.of("zh_cn", "瓦解 | 材料"));
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_FLUID = BreaLib.REGISTRY.creativeTab("material_fluid", "Brea | Fluid", Map.of("zh_cn", "瓦解 | 流体"));

    public static RegistryEntry<CreativeModeTab, CreativeModeTab> DECORATION = BreaLib.REGISTRY.creativeTab("decoration", "Brea | Decoration", Map.of("zh_cn", "瓦解 | 美化"));

    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MACHINE = BreaLib.REGISTRY.creativeTab("machine", "Brea | Machines", Map.of("zh_cn", "瓦解 | 机器"));
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> FUNCTIONAL = BreaLib.REGISTRY.creativeTab("functional", "Brea | Functional", Map.of("zh_cn", "瓦解 | 功能"));

    public static RegistryEntry<CreativeModeTab, CreativeModeTab> TOOL = BreaLib.REGISTRY.creativeTab("tool", "Brea | Tools", Map.of("zh_cn", "瓦解 | 工具"));
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> ITEM = BreaLib.REGISTRY.creativeTab("item", "Brea | Items", Map.of("zh_cn", "瓦解 | 杂项"));

    private BdCreativeTabs() {}

    public static void init() {}
}
