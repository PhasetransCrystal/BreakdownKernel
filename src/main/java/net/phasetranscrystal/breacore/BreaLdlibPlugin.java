package net.phasetranscrystal.breacore;

import com.lowdragmc.lowdraglib2.plugin.ILDLibPlugin;
import com.lowdragmc.lowdraglib2.plugin.LDLibPlugin;

@LDLibPlugin
public class BreaLdlibPlugin implements ILDLibPlugin {

    @Override
    public void onLoad() {
        BreakdownCore.LOGGER.info("LDLib2 initialized");
    }
}
