package net.phasetranscrystal.breacore.utils;

import net.minecraft.ChatFormatting;

import java.util.regex.Pattern;

public final class StringUtils {

    public static final String EMPTY = "";
    private static final String[] ChineseNumbers = new String[] { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };

    private StringUtils() {}

    public static String numberToChinese(int var0) {
        if (var0 >= 0 && var0 <= 9) {
            return ChineseNumbers[var0];
        } else {
            throw new IllegalArgumentException("Number must be between 0 and 9");
        }
    }

    public static String[] decompose(String var0) {
        return internalDecompose(':', var0);
    }

    private static String[] internalDecompose(char var0, String var1) {
        String[] var2 = new String[] { var1, "" };
        int var3 = var1.indexOf(var0);
        if (var3 >= 0) {
            var2[1] = var1.substring(var3 + 1);
            if (var3 >= 1) {
                var2[0] = var1.substring(0, var3);
            }
        }

        return var2;
    }

    public static String[] lastDecompose(char var0, String var1) {
        String[] var2 = new String[] { var1, "" };
        int var3 = var1.lastIndexOf(var0);
        if (var3 >= 0) {
            var2[1] = var1.substring(var3 + 1);
            var2[0] = var1.substring(0, var3);
        }

        return var2;
    }

    public static boolean containsWithWildcard(String[] var0, String var1) {
        for (String var5 : var0) {
            if (Pattern.matches(var5, var1)) {
                return true;
            }
        }

        return false;
    }

    public static String full_color(String var0) {
        return internalDecompose(var0, new ChatFormatting[] { ChatFormatting.RED, ChatFormatting.GOLD, ChatFormatting.YELLOW, ChatFormatting.GREEN, ChatFormatting.AQUA, ChatFormatting.BLUE, ChatFormatting.LIGHT_PURPLE }, (double) 80.0F);
    }

    public static String dark_purplish_red(String var0) {
        return internalDecompose(var0, new ChatFormatting[] { ChatFormatting.DARK_PURPLE, ChatFormatting.DARK_RED }, (double) 160.0F);
    }

    public static String white_blue(String var0) {
        return internalDecompose(var0, new ChatFormatting[] { ChatFormatting.BLUE, ChatFormatting.BLUE, ChatFormatting.BLUE, ChatFormatting.BLUE, ChatFormatting.WHITE, ChatFormatting.BLUE, ChatFormatting.WHITE, ChatFormatting.WHITE, ChatFormatting.BLUE, ChatFormatting.WHITE, ChatFormatting.WHITE, ChatFormatting.BLUE, ChatFormatting.RED, ChatFormatting.WHITE }, (double) 80.0F);
    }

    public static String purplish_red(String var0) {
        return internalDecompose(var0, new ChatFormatting[] { ChatFormatting.LIGHT_PURPLE, ChatFormatting.DARK_PURPLE }, (double) 160.0F);
    }

    public static String golden(String var0) {
        return internalDecompose(var0, new ChatFormatting[] { ChatFormatting.YELLOW, ChatFormatting.GOLD }, (double) 160.0F);
    }

    public static String dark_green(String var0) {
        return internalDecompose(var0, new ChatFormatting[] { ChatFormatting.GREEN, ChatFormatting.DARK_GREEN }, (double) 160.0F);
    }

    private static String internalDecompose(String var0, ChatFormatting[] var1, double var2) {
        StringBuilder var4 = new StringBuilder(var0.length() * 3);
        if (var2 <= (double) 0.0F) {
            var2 = 0.001;
        }

        int var5 = (int) Math.floor((double) (System.currentTimeMillis() & 16383L) / var2) % var1.length;

        for (int var6 = 0; var6 < var0.length(); ++var6) {
            char var7 = var0.charAt(var6);
            var4.append(var1[(var1.length + var6 - var5) % var1.length].toString());
            var4.append(var7);
        }

        return var4.toString();
    }
}
