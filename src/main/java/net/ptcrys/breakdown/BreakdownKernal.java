package net.ptcrys.breakdown;

import net.ptcrys.breakdown.client.ClientProxy;
import net.ptcrys.breakdown.common.CommonProxy;
import net.ptcrys.registrylib.util.DistExecutor;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import lombok.Getter;
import org.apache.logging.log4j.Logger;

@Mod(BreakdownKernal.MOD_ID)
public class BreakdownKernal {

    public static final String MOD_ID = "breakdown";
    public static final String NAME = "瓦解";
    public static final Logger LOGGER = BreaLib.getLogger("Kernal");
    @Getter
    private static ModContainer modContainer;
    @Getter
    private static IEventBus modEventBus;

    public BreakdownKernal(ModContainer modContainer, IEventBus modEventBus) {
        BreakdownKernal.modContainer = modContainer;
        BreakdownKernal.modEventBus = modEventBus;
        DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }
}
