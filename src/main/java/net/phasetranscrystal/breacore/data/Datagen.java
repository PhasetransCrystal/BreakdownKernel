package net.phasetranscrystal.breacore.data;

import net.phasetranscrystal.brealib.BreaLib;

import net.phasetranscrystal.registrylib.datagen.ProviderType;

import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistryCore;
import net.phasetranscrystal.breacore.data.lang.LangHandler;

import static net.phasetranscrystal.breacore.common.BreaRegistration.REGISTRATE;

public class Datagen {

    public static void init() {
        if (BreaLib.isDataGen()) {
            REGISTRATE.addDataGenerator(ProviderType.LANG, LangHandler::enInitialize);
            REGISTRATE.addDataGenerator(BreaRegistryCore.LANG_ZH_CN, LangHandler::cnInitialize);
        }
    }
}
