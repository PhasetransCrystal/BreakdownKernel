package net.ptcrys.breakdown;

import net.ptcrys.breakdown.data.BreakdownPlugin;
import net.ptcrys.breakdown.data.blockEntities.BdBlockEntities;
import net.ptcrys.breakdown.data.blocks.BdBlocks;
import net.ptcrys.topo.api.api.plugin.TopoPlugins;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(BreaLib.Core_ID)
public class BreakdownKernal {

    public BreakdownKernal(ModContainer modContainer, IEventBus modEventBus) {
        TopoPlugins.register(BreakdownPlugin.INSTANCE);
        BdBlocks.init();
        BdBlockEntities.init();
    }
}
