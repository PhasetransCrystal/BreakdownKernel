package net.phasetranscrystal.breacore.utils;

import net.minecraft.ChatFormatting;

public final class StringUtils {

    public static final String EMPTY = "";

    private StringUtils() {
        throw new UnsatisfiedLinkError("Not Impl");
    }

    public static native String numberToChinese(int var0);

    public static native String[] decompose(String var0);

    private static native String[] ㅤࣨࣳࣻ(char var0, String var1);

    public static native String[] lastDecompose(char var0, String var1);

    public static native boolean containsWithWildcard(String[] var0, String var1);

    public static native String full_color(String var0);

    public static native String dark_purplish_red(String var0);

    public static native String white_blue(String var0);

    public static native String purplish_red(String var0);

    public static native String golden(String var0);

    public static native String dark_green(String var0);

    private static native String ㅤࣨࣳࣻ(String var0, ChatFormatting[] var1, double var2);
}
