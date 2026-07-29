package net.ptcrys.breakdown;

import net.ptcrys.breakdown.client.ClientProxy;
import net.ptcrys.breakdown.common.CommonProxy;
import net.ptcrys.registrylib.util.DistExecutor;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(BreaLib.Core_ID)
public class BreakdownKernal {

    public BreakdownKernal(ModContainer modContainer, IEventBus modEventBus) {
        BreaLib.modContainer = modContainer;
        BreaLib.modEventBus = modEventBus;
        DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }
}
