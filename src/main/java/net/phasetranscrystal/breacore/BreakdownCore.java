package net.phasetranscrystal.breacore;

import net.phasetranscrystal.brealib.util.DistExecutor;

import net.phasetranscrystal.breacore.client.ClientProxy;
import net.phasetranscrystal.breacore.common.CommonProxy;

import net.neoforged.fml.common.Mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BreakdownCore.MOD_ID)
public class BreakdownCore {

    public static final String MOD_ID = "breacore";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public BreakdownCore() {
        DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }
}
