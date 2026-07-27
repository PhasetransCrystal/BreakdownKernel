package net.ptcrys.breakdown.utils;

import net.minecraft.util.ARGB;

import it.unimi.dsi.fastutil.ints.IntIntPair;

public class GradientUtil {

    private GradientUtil() {}

    public static int argbToAbgr(int argb) {
        int r = argb >> 16 & 255;
        int b = argb & 255;
        return argb & -16711936 | b << 16 | r;
    }

    public static int argbToRgba(int argb) {
        return argb << 8 | argb >>> 24;
    }

    public static float[] getRGB(int color) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color >> 0 & 255) / 255.0F;
        return new float[] { r, g, b };
    }

    public static int multiplyBlendWithAlpha(int c1, int c2) {
        int x1 = c1 & 255;
        int y1 = (c1 & '\uff00') >> 8;
        int z1 = (c1 & 16711680) >> 16;
        int w1 = (c1 & -16777216) >> 24;
        int x2 = c2 & 255;
        int y2 = (c2 & '\uff00') >> 8;
        int z2 = (c2 & 16711680) >> 16;
        int w2 = (c2 & -16777216) >> 24;
        int x = x1 * x2 / 255;
        int y = y1 * y2 / 255;
        int z = z1 * z2 / 255;
        int w = w1 * w2 / 255;
        if (w1 == -1 && w2 == -1) {
            w = 255;
        }

        return w << 24 | z << 16 | y << 8 | x;
    }

    public static int blend(int c1, int c2, float ratio) {
        if (ratio > 1.0F) {
            ratio = 1.0F;
        } else if (ratio < 0.0F) {
            ratio = 0.0F;
        }

        float iRatio = 1.0F - ratio;
        int a1 = c1 >> 24 & 255;
        int r1 = (c1 & 16711680) >> 16;
        int g1 = (c1 & '\uff00') >> 8;
        int b1 = c1 & 255;
        int a2 = c2 >> 24 & 255;
        int r2 = (c2 & 16711680) >> 16;
        int g2 = (c2 & '\uff00') >> 8;
        int b2 = c2 & 255;
        int a = (int) ((float) a1 * iRatio + (float) a2 * ratio);
        int r = (int) ((float) r1 * iRatio + (float) r2 * ratio);
        int g = (int) ((float) g1 * iRatio + (float) g2 * ratio);
        int b = (int) ((float) b1 * iRatio + (float) b2 * ratio);
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static IntIntPair getGradient(int rgb, int luminanceDifference) {
        float[] hsl = RGBtoHSL(rgb);
        float[] upshade = new float[3];
        float[] downshade = new float[3];
        System.arraycopy(hsl, 0, upshade, 0, 3);
        System.arraycopy(hsl, 0, downshade, 0, 3);
        upshade[2] += (float) luminanceDifference;
        if (upshade[2] > 100.0F) {
            upshade[2] = 100.0F;
        }

        downshade[2] -= (float) luminanceDifference;
        if (downshade[2] < 0.0F) {
            downshade[2] = 0.0F;
        }

        int upshadeRgb = toRGB(upshade);
        int downshadeRgb = toRGB(downshade);
        return IntIntPair.of(downshadeRgb, upshadeRgb);
    }

    public static float[] RGBtoHSL(int rgbColor) {
        float r = (float) (rgbColor >> 16 & 255) / 255.0F;
        float g = (float) (rgbColor >> 8 & 255) / 255.0F;
        float b = (float) (rgbColor >> 0 & 255) / 255.0F;
        float min = Math.min(r, Math.min(g, b));
        float max = Math.max(r, Math.max(g, b));
        float h = 0.0F;
        if (max == min) {
            h = 0.0F;
        } else if (max == r) {
            h = (60.0F * (g - b) / (max - min) + 360.0F) % 360.0F;
        } else if (max == g) {
            h = 60.0F * (b - r) / (max - min) + 120.0F;
        } else if (max == b) {
            h = 60.0F * (r - g) / (max - min) + 240.0F;
        }

        float l = (max + min) / 2.0F;
        float s;
        if (max == min) {
            s = 0.0F;
        } else if (l <= 0.5F) {
            s = (max - min) / (max + min);
        } else {
            s = (max - min) / (2.0F - max - min);
        }

        return new float[] { h, s * 100.0F, l * 100.0F };
    }

    public static int toRGB(float[] hsv) {
        return toRGB(hsv[0], hsv[1], hsv[2]);
    }

    public static int toRGB(float h, float s, float l) {
        h %= 360.0F;
        h /= 360.0F;
        s /= 100.0F;
        l /= 100.0F;
        float q;
        if (l < 0.5F) {
            q = l * (1.0F + s);
        } else {
            q = l + s - s * l;
        }

        float p = 2.0F * l - q;
        int r = (int) (Math.max(0.0F, hueToRGB(p, q, h + 0.33333334F)) * 255.0F);
        int g = (int) (Math.max(0.0F, hueToRGB(p, q, h)) * 255.0F);
        int b = (int) (Math.max(0.0F, hueToRGB(p, q, h - 0.33333334F)) * 255.0F);
        return ARGB.color(255, r, g, b);
    }

    private static float hueToRGB(float p, float q, float h) {
        if (h < 0.0F) {
            ++h;
        }

        if (h > 1.0F) {
            --h;
        }

        if (6.0F * h < 1.0F) {
            return p + (q - p) * 6.0F * h;
        } else if (2.0F * h < 1.0F) {
            return q;
        } else {
            return 3.0F * h < 2.0F ? p + (q - p) * 6.0F * (0.6666667F - h) : p;
        }
    }
}
