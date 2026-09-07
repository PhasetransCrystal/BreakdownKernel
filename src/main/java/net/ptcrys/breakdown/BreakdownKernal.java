package net.ptcrys.breakdown;

import net.ptcrys.breakdown.api.BreakdownApiPlugin;
import net.ptcrys.breakdown.api.equipment.EquipmentRuntimeBindings;
import net.ptcrys.breakdown.api.machine.MachineRuntimeBindings;
import net.ptcrys.breakdown.api.material.MaterialRuntimeBindings;
import net.ptcrys.breakdown.config.ConfigHolder;
import net.ptcrys.breakdown.data.BreakdownKernalPlugin;
import net.ptcrys.topo.api.api.plugin.TopoPlugins;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * 瓦解核心入口:注册两个插件——
 * {@link BreakdownApiPlugin}(骨架构件:材料形态/配方类型/数据类型等)与
 * {@link BreakdownKernalPlugin}(具体内容:材料/机器等产品表)。
 */
@Mod(BreaLib.Core_ID)
public class BreakdownKernal {

    public BreakdownKernal(ModContainer modContainer, IEventBus modEventBus) {
        ConfigHolder.init();
        TopoPlugins.register(BreakdownApiPlugin.INSTANCE);
        TopoPlugins.register(BreakdownKernalPlugin.INSTANCE);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onFMLCommonSetup(FMLCommonSetupEvent event) {
        // 必须先于 PipeSpecTooltips:它的 setup 任务会 freeze ItemTooltipUis,enqueueWork 按提交序执行。
        EquipmentRuntimeBindings.register(event);
        MaterialRuntimeBindings.register(event);
        MachineRuntimeBindings.register(event);
    }
}
