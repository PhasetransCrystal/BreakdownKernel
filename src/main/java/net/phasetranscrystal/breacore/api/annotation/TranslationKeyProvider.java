package net.phasetranscrystal.breacore.api.annotation;

import net.phasetranscrystal.breacore.api.lang.CNEN;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public final class TranslationKeyProvider {

    public static final Object2ObjectOpenHashMap<String, CNEN> LANG;

    public TranslationKeyProvider() {
        throw new UnsatisfiedLinkError("Not Impl");
    }

    public static native String getTranslationKey(String var0, String var1);

    public static native String getTranslationKey(String var0, String var1, String var2);
}
