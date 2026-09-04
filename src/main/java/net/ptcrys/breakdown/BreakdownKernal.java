package net.ptcrys.breakdown;

import net.ptcrys.breakdown.api.BreakdownApiPlugin;
import net.ptcrys.breakdown.data.BreakdownKernalPlugin;
import net.ptcrys.topo.api.api.plugin.TopoPlugins;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

/**
 * 瓦解核心入口:注册两个插件——
 * {@link BreakdownApiPlugin}(骨架构件:材料形态/配方类型/数据类型等)与
 * {@link BreakdownKernalPlugin}(具体内容:材料/机器等产品表)。
 */
@Mod(BreaLib.Core_ID)
public class BreakdownKernal {

    public BreakdownKernal(ModContainer modContainer, IEventBus modEventBus) {
        TopoPlugins.register(BreakdownApiPlugin.INSTANCE);
        TopoPlugins.register(BreakdownKernalPlugin.INSTANCE);
    }
}
