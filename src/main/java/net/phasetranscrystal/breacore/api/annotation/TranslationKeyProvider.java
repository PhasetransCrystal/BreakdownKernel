package net.phasetranscrystal.breacore.api.annotation;

import net.phasetranscrystal.brealib.BreaLib;

import net.phasetranscrystal.breacore.api.lang.CNEN;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public final class TranslationKeyProvider {

    public static final Object2ObjectOpenHashMap<String, CNEN> LANG = BreaLib.isDataGen() ? new Object2ObjectOpenHashMap<>() : null;

    public static String getTranslationKey(String var0, String var1) {
        return getTranslationKey(var0, var1, "breacore.lang");
    }

    public static String getTranslationKey(String var0, String var1, String var2) {
        String var3 = (var2 != null ? var2 : "") + "." + var1.hashCode();
        var3 = var3.replace("..", ".");
        if (LANG != null) {
            LANG.put(var3, new CNEN(var0, var1));
        }

        return var3;
    }
}
