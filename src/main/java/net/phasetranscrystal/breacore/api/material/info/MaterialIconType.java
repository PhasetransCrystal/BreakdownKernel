package net.phasetranscrystal.breacore.api.material.info;

import net.phasetranscrystal.brealib.BreaLib;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.lowdragmc.lowdraglib2.utils.ResourceHelper;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * 图标集类型
 * 待重构
 * 待美工
 *
 * @param name
 */
public record MaterialIconType(String name) {

    public static final Map<String, MaterialIconType> ICON_TYPES = new HashMap<>();

    private static final Table<MaterialIconType, MaterialIconSet, Identifier> ITEM_MODEL_CACHE = HashBasedTable
            .create();
    private static final Table<MaterialIconType, MaterialIconSet, Identifier> ITEM_TEXTURE_CACHE = HashBasedTable
            .create();
    private static final Table<MaterialIconType, MaterialIconSet, Identifier> ITEM_TEXTURE_CACHE_SECONDARY = HashBasedTable
            .create();
    private static final Table<MaterialIconType, MaterialIconSet, Identifier> BLOCK_MODEL_CACHE = HashBasedTable
            .create();
    private static final Table<MaterialIconType, MaterialIconSet, Identifier> BLOCK_TEXTURE_CACHE = HashBasedTable
            .create();
    private static final Table<MaterialIconType, MaterialIconSet, Identifier> BLOCK_TEXTURE_CACHE_SECONDARY = HashBasedTable
            .create();

    public MaterialIconType(String name) {
        this.name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
        Preconditions.checkArgument(!ICON_TYPES.containsKey(this.name),
                "MaterialIconType " + this.name + " already registered!");
        ICON_TYPES.put(this.name, this);
    }

    public static MaterialIconType getByName(String name) {
        return ICON_TYPES.get(name);
    }

    @Nullable
    public Identifier getBlockTexturePath(@NotNull MaterialIconSet materialIconSet, boolean doReadCache) {
        return getBlockTexturePath(materialIconSet, null, doReadCache);
    }

    @Nullable // Safe: only null on registration on fabric, and no "required" textures are resolved at that point.
    public Identifier getBlockTexturePath(@NotNull MaterialIconSet materialIconSet, String suffix,
                                          boolean doReadCache) {
        if (doReadCache) {
            if (suffix == null || suffix.isBlank()) {
                if (BLOCK_TEXTURE_CACHE.contains(this, materialIconSet))
                    return BLOCK_TEXTURE_CACHE.get(this, materialIconSet);
            } else {
                if (BLOCK_TEXTURE_CACHE_SECONDARY.contains(this, materialIconSet))
                    return BLOCK_TEXTURE_CACHE_SECONDARY.get(this, materialIconSet);
            }
        }

        suffix = Strings.isBlank(suffix) ? "" : "_" + suffix;

        MaterialIconSet iconSet = materialIconSet;
        // noinspection ConstantConditions
        if (!BreaLib.isClientSide() ||
                Minecraft.getInstance() == null ||
                Minecraft.getInstance().getResourceManager() == null)
            return null; // check minecraft for null for CI environments
        if (!iconSet.isRootIconset) {
            while (!iconSet.isRootIconset) {
                Identifier location = BreaLib.id(String.format("textures/gensource/block/%s/%s%s.png", iconSet.name, this.name, suffix));
                if (ResourceHelper.isResourceExist(location) || ResourceHelper.isResourceExistRaw(location))
                    break;
                iconSet = iconSet.parentIconset;
            }
        }

        Identifier location = BreaLib.id(String.format("textures/gensource/block/%s/%s%s.png", iconSet.name, this.name, suffix));
        if (!suffix.isEmpty() && !ResourceHelper.isResourceExist(location) &&
                !ResourceHelper.isResourceExistRaw(location)) {
            return null;
        }
        location = BreaLib.id(String.format("gensource/block/%s/%s%s", iconSet.name, this.name, suffix));
        if (suffix.isEmpty()) {
            BLOCK_TEXTURE_CACHE.put(this, materialIconSet, location);
        } else {
            BLOCK_TEXTURE_CACHE_SECONDARY.put(this, materialIconSet, location);
        }

        return location;
    }

    @NotNull
    public Identifier getBlockModelPath(@NotNull MaterialIconSet materialIconSet, boolean doReadCache) {
        if (doReadCache) {
            if (BLOCK_MODEL_CACHE.contains(this, materialIconSet)) {
                return BLOCK_MODEL_CACHE.get(this, materialIconSet);
            }
        }

        MaterialIconSet iconSet = materialIconSet;
        // noinspection ConstantConditions
        if (!iconSet.isRootIconset && BreaLib.isClientSide() && Minecraft.getInstance() != null &&
                Minecraft.getInstance().getResourceManager() != null) { // check minecraft for null for CI environments
            while (!iconSet.isRootIconset) {
                Identifier location = BreaLib.id(String.format("models/block/gensource/%s/%s.json", iconSet.name, this.name));
                if (ResourceHelper.isResourceExist(location) || ResourceHelper.isResourceExistRaw(location))
                    break;
                iconSet = iconSet.parentIconset;
            }
        }

        Identifier location = BreaLib.id(String.format("block/gensource/%s/%s", iconSet.name, this.name));
        ITEM_MODEL_CACHE.put(this, materialIconSet, location);

        return location;
    }

    @NotNull
    public Identifier getItemModelPath(@NotNull MaterialIconSet materialIconSet, boolean doReadCache) {
        if (doReadCache) {
            if (ITEM_MODEL_CACHE.contains(this, materialIconSet)) {
                return ITEM_MODEL_CACHE.get(this, materialIconSet);
            }
        }

        MaterialIconSet iconSet = materialIconSet;
        // noinspection ConstantConditions
        if (!iconSet.isRootIconset && BreaLib.isClientSide() && Minecraft.getInstance() != null &&
                Minecraft.getInstance().getResourceManager() != null) { // check minecraft for null for CI environments
            while (!iconSet.isRootIconset) {
                Identifier location = BreaLib.id(String.format("item/gensource/%s/%s.json", iconSet.name, this.name));
                if (ResourceHelper.isResourceExist(location) || ResourceHelper.isResourceExistRaw(location))
                    break;
                iconSet = iconSet.parentIconset;
            }
        }

        Identifier location = BreaLib.id(String.format("item/gensource/%s/%s", iconSet.name, this.name));
        ITEM_MODEL_CACHE.put(this, materialIconSet, location);

        return location;
    }

    @Nullable
    public Identifier getItemTexturePath(@NotNull MaterialIconSet materialIconSet, boolean doReadCache) {
        return getItemTexturePath(materialIconSet, null, doReadCache);
    }

    @Nullable
    public Identifier getItemTexturePath(@NotNull MaterialIconSet materialIconSet, String suffix,
                                         boolean doReadCache) {
        if (doReadCache) {
            if (suffix == null || suffix.isBlank()) {
                if (ITEM_TEXTURE_CACHE.contains(this, materialIconSet))
                    return ITEM_TEXTURE_CACHE.get(this, materialIconSet);
            } else {
                if (ITEM_TEXTURE_CACHE_SECONDARY.contains(this, materialIconSet))
                    return ITEM_TEXTURE_CACHE_SECONDARY.get(this, materialIconSet);
            }
        }

        suffix = suffix == null || suffix.isBlank() ? "" : "_" + suffix;

        MaterialIconSet iconSet = materialIconSet;
        // noinspection ConstantConditions
        if (!iconSet.isRootIconset && BreaLib.isClientSide() && Minecraft.getInstance() != null &&
                Minecraft.getInstance().getResourceManager() != null) { // check minecraft for null for CI environments
            while (!iconSet.isRootIconset) {
                Identifier location = BreaLib.id(String.format("textures/itemgensource//%s/%s%s.png", iconSet.name, this.name, suffix));
                if (ResourceHelper.isResourceExist(location) || ResourceHelper.isResourceExistRaw(location))
                    break;
                iconSet = iconSet.parentIconset;
            }
        }

        Identifier location = BreaLib.id(String.format("textures/item/gensource/%s/%s%s.png", iconSet.name, this.name, suffix));
        if (!suffix.isEmpty() && !ResourceHelper.isResourceExist(location) &&
                !ResourceHelper.isResourceExistRaw(location)) {
            return null;
        }
        location = BreaLib.id(String.format("item/gensource/%s/%s%s", iconSet.name, this.name, suffix));
        if (suffix.isEmpty()) {
            ITEM_TEXTURE_CACHE.put(this, materialIconSet, location);
        } else {
            ITEM_TEXTURE_CACHE_SECONDARY.put(this, materialIconSet, location);
        }

        return location;
    }

    @Override
    public @NotNull String toString() {
        return this.name;
    }
}
