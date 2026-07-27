package net.ptcrys.breakdown.api.annotation;

import net.ptcrys.breakdown.BreaLib;
import net.ptcrys.breakdown.api.lang.CNEN;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public final class TranslationKeyProvider {

    public static final Object2ObjectOpenHashMap<String, CNEN> LANG = BreaLib.isDataGen() ? new Object2ObjectOpenHashMap<>() : null;

    public static String getTranslationKey(String cn, String en) {
        return getTranslationKey(cn, en, "breakdown.lang");
    }

    public static String getTranslationKey(String cn, String en, String prefix) {
        String var3 = (prefix != null ? prefix : "") + "." + en.hashCode();
        var3 = var3.replace("..", ".");
        if (LANG != null) {
            LANG.put(var3, new CNEN(cn, en));
        }

        return var3;
    }
}
