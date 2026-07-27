package net.ptcrys.breakdown;

import com.lowdragmc.lowdraglib2.plugin.ILDLibPlugin;
import com.lowdragmc.lowdraglib2.plugin.LDLibPlugin;

@LDLibPlugin
public class BreaLdlibPlugin implements ILDLibPlugin {

    @Override
    public void onLoad() {
        BreakdownKernal.LOGGER.info("LDLib2 initialized");
    }
}
