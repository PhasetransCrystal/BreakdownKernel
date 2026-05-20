package net.phasetranscrystal.breacore.api.annotation;

public final class TranslationKeyProvider {

    public TranslationKeyProvider() {
        throw new UnsatisfiedLinkError("Not Impl");
    }

    public static native String getTranslationKey(String var0, String var1);

    public static native String getTranslationKey(String var0, String var1, String var2);
}
