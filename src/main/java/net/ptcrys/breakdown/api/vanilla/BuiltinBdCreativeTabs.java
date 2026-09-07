package net.ptcrys.breakdown.api.vanilla;

import net.ptcrys.breakdown.BreaLib;
import net.ptcrys.registrylib.util.entry.RegistryEntry;

import net.minecraft.world.item.CreativeModeTab;

import java.util.Map;

/**
 * 瓦解核心内置创造物品栏(骨架):所有由 {@code BuiltinBdMaterialForms} 铸造的材料物品/方块统一
 * 收纳于 {@link #MATERIALS}。具体内容插件可在自己的领域注册中创建专属标签。
 */
public final class BuiltinBdCreativeTabs {

    /** 材料物品/方块标签;标题 lang key 为 {@code itemGroup.breakdown.material}。 */
    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIALS = BreaLib.REGISTRY.creativeTab(
            "materials",
            "Breakdown | Materials",
            Map.of("zh_cn", "瓦解 | 材料"));

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MACHINES = BreaLib.REGISTRY.creativeTab(
            "machines",
            "Breakdown | machines",
            Map.of("zh_cn", "瓦解 | 机器"));

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> EQUIPMENT = BreaLib.REGISTRY.creativeTab(
            "equipment",
            "Breakdown | equipments",
            Map.of("zh_cn", "瓦解 | 装备"));

    private BuiltinBdCreativeTabs() {}

    /** 触发类加载,使标签在材料注册驱动运行前完成注册。 */
    public static void init() {}
}
