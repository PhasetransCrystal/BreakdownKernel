package net.phasetranscrystal.breacore;

import net.phasetranscrystal.brealib.BreaLib;
import net.phasetranscrystal.brealib.util.DistExecutor;

import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistryCore;
import net.phasetranscrystal.breacore.client.ClientProxy;
import net.phasetranscrystal.breacore.common.CommonProxy;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import lombok.Getter;
import org.apache.logging.log4j.Logger;

@Mod(BreakdownCore.MOD_ID)
public class BreakdownCore {

    public static final String MOD_ID = BreaLib.Core_ID;
    public static final Logger LOGGER = BreaLib.getLogger("Core");
    @Getter
    private static ModContainer modContainer;
    @Getter
    private static IEventBus modEventBus;
    /**
     * 共享的注册核心。 使用 {@link BreaRegistryCore} 而非普通 {@link
     * net.phasetranscrystal.registrylib.RegistryCore}， 使得每个 builder 链上可直接调用 {@code .langCn("中文名")}。
     */
    public static final BreaRegistryCore REGISTRATE = BreaRegistryCore.create(MOD_ID);

    public BreakdownCore(ModContainer modContainer, IEventBus modEventBus) {
        BreakdownCore.modContainer = modContainer;
        BreakdownCore.modEventBus = modEventBus;
        DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }
}
