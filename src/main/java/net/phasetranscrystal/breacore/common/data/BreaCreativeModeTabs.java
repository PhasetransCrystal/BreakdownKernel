package net.phasetranscrystal.breacore.common.data;

import net.phasetranscrystal.registrylib.util.entry.RegistryEntry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;

import java.util.Map;

import static net.phasetranscrystal.breacore.common.BreaRegistration.REGISTRATE;

public class BreaCreativeModeTabs {

    public static RegistryEntry<CreativeModeTab, CreativeModeTab> DEBUG_ITEMS;
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> CREATIVE_MODE_ITEMS;

    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_ITEM;
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_FLUID;

    public static RegistryEntry<CreativeModeTab, CreativeModeTab> DECORATION;

    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MACHINE;

    public static RegistryEntry<CreativeModeTab, CreativeModeTab> ITEM;
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> TOOL;
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> GENERAL_BLOCK;

    static {
        DEBUG_ITEMS = REGISTRATE.creativeTab("debug_items", "Brea | Debug", Map.of("zh_cn", "瓦解 | Debug"), builder -> builder
                .icon(Items.COMMAND_BLOCK::getDefaultInstance));
        CREATIVE_MODE_ITEMS = REGISTRATE.creativeTab("creative_mode_items", "Brea | Creative Mode", Map.of("zh_cn", "瓦解 | 创造"), builder -> builder
                .icon(Items.COMMAND_BLOCK::getDefaultInstance));
        MATERIAL_FLUID = REGISTRATE.creativeTab("material_fluid", "Brea | Fluid", Map.of("zh_cn", "瓦解 | 流体"), builder -> builder
                .icon(Items.COMMAND_BLOCK::getDefaultInstance));
        MATERIAL_ITEM = REGISTRATE.creativeTab("material_item", "Brea | Items", Map.of("zh_cn", "瓦解 | 材料"), builder -> builder
                .icon(Items.COMMAND_BLOCK::getDefaultInstance));
        GENERAL_BLOCK = REGISTRATE.creativeTab("material_block", "Brea | Blocks", Map.of("zh_cn", "瓦解 | 功能方块"), builder -> builder
                .icon(Items.COMMAND_BLOCK::getDefaultInstance));
        DECORATION = REGISTRATE.creativeTab("decoration", "Brea | Decoration", Map.of("zh_cn", "瓦解 | 美化"), builder -> builder
                .icon(Items.COMMAND_BLOCK::getDefaultInstance));
        TOOL = REGISTRATE.creativeTab("tool", "Brea | Tools", Map.of("zh_cn", "瓦解 | 工具"), builder -> builder
                .icon(Items.COMMAND_BLOCK::getDefaultInstance));
        MACHINE = REGISTRATE.creativeTab("machine", "Brea | Machines", Map.of("zh_cn", "瓦解 | 机器"), builder -> builder
                .icon(Items.COMMAND_BLOCK::getDefaultInstance));
        ITEM = REGISTRATE.creativeTab("item", "Brea | Items", Map.of("zh_cn", "瓦解 | 杂项"), builder -> builder
                .icon(Items.COMMAND_BLOCK::getDefaultInstance));
    }

    public static void init() {}
}
