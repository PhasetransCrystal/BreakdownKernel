package net.ptcrys.breakdown.data;

import net.ptcrys.breakdown.BreaLib;
import net.ptcrys.breakdown.api.registry.registrate.BreaRegistryCore;
import net.ptcrys.breakdown.data.lang.LangHandler;
import net.ptcrys.registrylib.datagen.ProviderType;

import static net.ptcrys.breakdown.common.BreaRegistration.REGISTRATE;

public class Datagen {

    public static void init() {
        if (BreaLib.isDataGen()) {
            REGISTRATE.addDataGenerator(ProviderType.LANG, LangHandler::enInitialize);
            REGISTRATE.addDataGenerator(BreaRegistryCore.LANG_ZH_CN, LangHandler::cnInitialize);
        }
    }
}
