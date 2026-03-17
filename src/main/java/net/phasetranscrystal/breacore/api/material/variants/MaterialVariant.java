package net.phasetranscrystal.breacore.api.material.variants;

import net.phasetranscrystal.brealib.util.FormattingUtil;
import net.phasetranscrystal.brealib.util.memoization.CacheMemoizer;

import net.phasetranscrystal.breacore.api.material.Material;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import com.google.common.collect.Table;
import com.lowdragmc.lowdraglib2.utils.LocalizationUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@Accessors(chain = true, fluent = true)
public class MaterialVariant {

    public final static Map<String, MaterialVariant> VARIANTS = new HashMap<>();
    public static final Codec<MaterialVariant> CODEC = Codec.STRING.flatXmap(
            str -> Optional.ofNullable(get(str)).map(DataResult::success)
                    .orElseGet(() -> DataResult.error(() -> "Invalid MaterialVariant: " + str)),
            variant -> DataResult.success(variant.name));
    public static final MaterialVariant NULL = new MaterialVariant("null");
    @Getter
    private final String name;
    private final Map<Material, Supplier<? extends ItemLike>[]> ignoredMaterials = new HashMap<>();
    private final Object2FloatMap<Material> materialAmounts = new Object2FloatOpenHashMap<>();
    @Setter
    @Getter
    public String langValue;
    @Getter
    @Setter
    private String idPattern;
    @Getter
    @Setter
    private long materialAmount = -1;
    @Setter
    private boolean generateItem;
    @Setter
    private boolean generateBlock;
    @Setter
    private boolean generateFluid;
    @Getter
    private BlockProperties blockProperties = new BlockProperties(UnaryOperator.identity());
    @Getter
    @Setter
    private @Nullable Predicate<Material> generationCondition;
    @Setter
    private Supplier<Table<MaterialVariant, Material, ? extends Supplier<? extends ItemLike>>> itemTable;
    @Getter
    @Setter
    private int maxStackSize = 64;

    public MaterialVariant(String name) {
        this.name = name;
        String lowerCaseUnder = FormattingUtil.toLowerCaseUnder(name);
        this.idPattern = "%s_" + lowerCaseUnder;
        this.langValue = "%s " + FormattingUtil.toEnglishName(lowerCaseUnder);
        VARIANTS.put(name, this);
    }

    public static MaterialVariant get(String variantName) {
        return VARIANTS.get(variantName);
    }

    public static MaterialVariant getVariant(String variantName) {
        return getVariant(variantName, null);
    }

    public static MaterialVariant getVariant(String variantName, @Nullable MaterialVariant replacement) {
        return VARIANTS.getOrDefault(variantName, replacement);
    }

    public static Collection<MaterialVariant> values() {
        return VARIANTS.values();
    }

    public boolean isEmpty() {
        return this == NULL;
    }

    public MaterialVariant blockProperties(UnaryOperator<BlockBehaviour.Properties> properties) {
        this.blockProperties = new BlockProperties(properties);
        return this;
    }

    public MaterialVariant blockProperties(BlockProperties properties) {
        this.blockProperties = properties;
        return this;
    }

    public boolean hasItemTable() {
        return itemTable != null;
    }

    @SuppressWarnings("unchecked")
    public Supplier<ItemLike> getItemFromTable(Material material) {
        return (Supplier<ItemLike>) itemTable.get().get(this, material);
    }

    public boolean doGenerateItem() {
        return generateItem;
    }

    public boolean doGenerateItem(Material material) {
        return generateItem && !isIgnored(material) &&
                (generationCondition == null || generationCondition.test(material)) ||
                (hasItemTable() && this.itemTable.get() != null && getItemFromTable(material) != null);
    }

    public boolean doGenerateBlock() {
        return generateBlock;
    }

    public boolean doGenerateBlock(Material material) {
        return generateBlock && !isIgnored(material) &&
                (generationCondition == null || generationCondition.test(material)) ||
                hasItemTable() && this.itemTable.get() != null && getItemFromTable(material) != null;
    }

    public boolean doGenerateFluid() {
        return generateFluid;
    }

    public boolean doGenerateFluid(Material material) {
        return generateFluid && !isIgnored(material) &&
                (generationCondition == null || generationCondition.test(material)) ||
                hasItemTable() && this.itemTable.get() != null && getItemFromTable(material) != null;
    }

    public String getUnlocalizedName() {
        return "variant." + FormattingUtil.toLowerCaseUnderscore(name);
    }

    public MutableComponent getLocalizedName(Material material) {
        return Component.translatable(getUnlocalizedName(material), material.getLocalizedName());
    }

    public String getUnlocalizedName(Material material) {
        String matSpecificKey = String.format("item.%s.%s", material.getModId(),
                this.idPattern.formatted(material.getName()));
        if (LocalizationUtils.exist(matSpecificKey)) {
            return matSpecificKey;
        }
        return getUnlocalizedName();
    }

    public boolean isIgnored(Material material) {
        return ignoredMaterials.containsKey(material);
    }

    @SafeVarargs
    public final void setIgnored(Material material, Supplier<? extends ItemLike>... items) {
        ignoredMaterials.put(material, items);
        if (items.length > 0) {
            // ItemMaterialData.registerMaterialEntries(Arrays.asList(items), this, material);
        }
    }

    @SuppressWarnings("unchecked")
    public void setIgnored(Material material, ItemLike... items) {
        // go through setIgnoredBlock to wrap if this is a block prefix
        if (this.doGenerateBlock()) {
            this.setIgnoredBlock(material,
                    Arrays.stream(items).filter(Block.class::isInstance).map(Block.class::cast).toArray(Block[]::new));
        } else {
            this.setIgnored(material,
                    Arrays.stream(items).map(item -> (Supplier<ItemLike>) () -> item).toArray(Supplier[]::new));
        }
    }

    @SuppressWarnings("unchecked")
    public void setIgnoredBlock(Material material, Block... items) {
        this.setIgnored(material, Arrays.stream(items).map(block -> CacheMemoizer.memoizeBlockSupplier(() -> block))
                .toArray(Supplier[]::new));
    }

    @SuppressWarnings("unchecked")
    public void setIgnored(Material material) {
        this.ignoredMaterials.put(material, new Supplier[0]);
    }

    public void removeIgnored(Material material) {
        ignoredMaterials.remove(material);
    }

    public Map<Material, Supplier<? extends ItemLike>[]> getIgnored() {
        return new HashMap<>(ignoredMaterials);
    }

    public boolean isAmountModified(Material material) {
        return materialAmounts.containsKey(material);
    }

    public void modifyMaterialAmount(@NotNull Material material, float amount) {
        materialAmounts.put(material, amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var variant = (MaterialVariant) o;
        return name.equals(variant.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    public record BlockProperties(UnaryOperator<BlockBehaviour.Properties> properties) {}
}
