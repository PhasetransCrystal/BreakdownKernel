package net.phasetranscrystal.breacore.data.materials.material;

import net.phasetranscrystal.brealib.BreaLib;

import net.phasetranscrystal.breacore.api.material.MarkerMaterial;

import net.minecraft.world.item.DyeColor;

import com.google.common.collect.HashBiMap;

public class MarkerMaterials {

    /**
     * 无分类的标记材料
     */
    public static final MarkerMaterial Empty = new MarkerMaterial(BreaLib.id("empty"));

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void register() {
        Color.Colorless.toString();
        Empty.toString();
    }

    /**
     * 颜色材料
     */
    public static class Color {

        /**
         * 只能通过直接指定使用
         * 表示 TagPrefix 上没有颜色
         * 通常作为颜色前缀的默认值
         */
        public static final MarkerMaterial Colorless = new MarkerMaterial(BreaLib.id("colorless"));

        public static final MarkerMaterial White = new MarkerMaterial(BreaLib.id("white"));
        public static final MarkerMaterial Orange = new MarkerMaterial(BreaLib.id("orange"));
        public static final MarkerMaterial Magenta = new MarkerMaterial(BreaLib.id("magenta"));
        public static final MarkerMaterial LightBlue = new MarkerMaterial(BreaLib.id("light_blue"));
        public static final MarkerMaterial Yellow = new MarkerMaterial(BreaLib.id("yellow"));
        public static final MarkerMaterial Lime = new MarkerMaterial(BreaLib.id("lime"));
        public static final MarkerMaterial Pink = new MarkerMaterial(BreaLib.id("pink"));
        public static final MarkerMaterial Gray = new MarkerMaterial(BreaLib.id("gray"));
        public static final MarkerMaterial LightGray = new MarkerMaterial(BreaLib.id("light_gray"));
        public static final MarkerMaterial Cyan = new MarkerMaterial(BreaLib.id("cyan"));
        public static final MarkerMaterial Purple = new MarkerMaterial(BreaLib.id("purple"));
        public static final MarkerMaterial Blue = new MarkerMaterial(BreaLib.id("blue"));
        public static final MarkerMaterial Brown = new MarkerMaterial(BreaLib.id("brown"));
        public static final MarkerMaterial Green = new MarkerMaterial(BreaLib.id("green"));
        public static final MarkerMaterial Red = new MarkerMaterial(BreaLib.id("red"));
        public static final MarkerMaterial Black = new MarkerMaterial(BreaLib.id("black"));
        /**
         * 包含所有可能颜色值的数组（不包含无色！）
         */
        public static final MarkerMaterial[] VALUES = new MarkerMaterial[] {
                White, Orange, Magenta, LightBlue, Yellow, Lime, Pink, Gray, LightGray, Cyan, Purple, Blue, Brown,
                Green, Red, Black
        };
        /**
         * 包含 MC DyeColor 与颜色标记材料之间的关联映射
         */
        public static final HashBiMap<DyeColor, MarkerMaterial> COLORS = HashBiMap.create();

        static {
            for (var color : DyeColor.values()) {
                COLORS.put(color, Color.valueOf(color.getName()));
            }
        }

        /**
         * 通过颜色名称获取颜色材料
         * 名称格式与 DyeColor 相同
         */
        public static MarkerMaterial valueOf(String string) {
            for (MarkerMaterial color : VALUES) {
                if (color.getName().equals(string)) {
                    return color;
                }
            }
            return null;
        }
    }
}
