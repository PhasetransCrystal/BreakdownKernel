package net.ptcrys.breakdown.api.tag;

import net.ptcrys.breakdown.api.material.Material;
import net.ptcrys.breakdown.api.material.register.MaterialVariant;
import net.ptcrys.breakdown.utils.FormattingUtil;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public class BreaTag {

    private final String tagPath;
    @Getter
    private boolean isParentTag = false;
    private BiFunction<MaterialVariant, Material, TagKey<Item>> formatter;
    private Predicate<Material> filter;

    private BreaTag(String tagPath) {
        this.tagPath = tagPath;
    }

    /**
     * Create a tag with a specified path, with the "default" formatter, meaning
     * that there is 1 "%s" format character in the path, intended for the Material name.
     */
    public static BreaTag withDefaultFormatter(String tagPath, boolean isVanilla) {
        BreaTag type = new BreaTag(tagPath);
        type.formatter = (prefix, mat) -> TagUtil.createItemTag(type.tagPath.formatted(mat.getName()), isVanilla);
        return type;
    }

    /**
     * Create a tag with a specified path, with the "default" formatter, meaning
     * that there is 2 "%s" format characters in the path, with the first being the
     * prefix name, and the second being the material name.
     */
    public static BreaTag withPrefixFormatter(String tagPath) {
        BreaTag type = new BreaTag(tagPath);
        type.formatter = (prefix, mat) -> TagUtil.createItemTag(
                type.tagPath.formatted(FormattingUtil.toLowerCaseUnderscore(prefix.langValue), mat.getName()));
        return type;
    }

    /**
     * Create a tag with a specified path, with the "default" formatter, meaning
     * that there is 1 "%s" format character in the path, intended for the prefix name.
     */
    public static BreaTag withPrefixOnlyFormatter(String tagPath) {
        BreaTag type = new BreaTag(tagPath);
        type.formatter = (prefix, mat) -> TagUtil
                .createItemTag(type.tagPath.formatted(FormattingUtil.toLowerCaseUnderscore(prefix.langValue)));
        type.isParentTag = true;
        return type;
    }

    public static BreaTag withNoFormatter(String tagPath, boolean isVanilla) {
        BreaTag type = new BreaTag(tagPath);
        type.formatter = (prefix, material) -> TagUtil.createItemTag(type.tagPath, isVanilla);
        type.isParentTag = true;
        return type;
    }

    public static BreaTag withCustomFormatter(String tagPath, BiFunction<MaterialVariant, Material, TagKey<Item>> formatter) {
        BreaTag type = new BreaTag(tagPath);
        type.formatter = formatter;
        return type;
    }

    public static BreaTag withCustomFilter(String tagPath, boolean isVanilla, Predicate<Material> filter) {
        BreaTag type = new BreaTag(tagPath);
        type.filter = filter;
        type.formatter = (prefix, material) -> TagUtil.createItemTag(type.tagPath, isVanilla);
        return type;
    }

    public TagKey<Item> getTag(MaterialVariant prefix, @NotNull Material material) {
        if (filter != null && !material.isNull() && !filter.test(material)) return null;
        return formatter.apply(prefix, material);
    }
}
