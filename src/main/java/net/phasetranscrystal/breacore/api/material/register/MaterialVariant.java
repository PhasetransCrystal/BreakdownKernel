package net.phasetranscrystal.breacore.api.material.register;

import net.phasetranscrystal.brealib.util.FormattingUtil;

import net.phasetranscrystal.breacore.api.BreaApi;
import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistryCore;
import net.phasetranscrystal.breacore.api.tag.BreaTag;
import net.phasetranscrystal.registrylib.util.entry.RegistryEntry;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

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
import java.util.function.Supplier;

@Accessors(chain = true, fluent = true)
public class MaterialVariant {

    /// 材料变体全集
    public final static Map<String, MaterialVariant> VARIANTS = new HashMap<>();
    public static final Codec<MaterialVariant> CODEC = Codec.STRING.flatXmap(
            str -> Optional.ofNullable(get(str)).map(DataResult::success)
                    .orElseGet(() -> DataResult.error(() -> "Invalid MaterialVariant: " + str)),
            variant -> DataResult.success(variant.id));
    /// 空变体
    public static final MaterialVariant NULL = new MaterialVariant("null");
    /// 变体ID
    @Getter
    private final String id;
    /// 变体命名键
    @Setter
    @Getter
    public String langValue;
    /// 变体ID格式
    @Getter
    @Setter
    private String idPattern;

    protected final List<BreaTag> tags = new ArrayList<>();

    /// 变体材料量
    @Getter
    @Setter
    private long materialAmount = -1;
    /// 最大堆叠
    @Getter
    @Setter
    private int maxStackSize = 64;
    @Getter
    @Setter
    private Supplier<RegistryEntry<CreativeModeTab, ? extends CreativeModeTab>> itemCreativeTab = () -> null;
    private final List<RegisterAction> registerActionList = new ArrayList<>();
    private final List<RegisterCondition> registerConditionList = new ArrayList<>();

    private final Map<Material, Supplier<? extends ItemLike>[]> ignoredMaterials = new HashMap<>();
    private final Object2FloatMap<Material> materialAmounts = new Object2FloatOpenHashMap<>();

    public MaterialVariant(String id) {
        this.id = id;
        String lowerCaseUnder = FormattingUtil.toLowerCaseUnder(id);
        this.idPattern = "%s_" + lowerCaseUnder;
        this.langValue = "%s " + FormattingUtil.toEnglishName(lowerCaseUnder);
        VARIANTS.put(id, this);
    }

    public static MaterialVariant get(String variantId) {
        return VARIANTS.get(variantId);
    }

    public static MaterialVariant getVariant(String variantId) {
        return getVariant(variantId, null);
    }

    public static MaterialVariant getVariant(String variantId, @Nullable MaterialVariant replacement) {
        return VARIANTS.getOrDefault(variantId, replacement);
    }

    public static Collection<MaterialVariant> values() {
        return VARIANTS.values();
    }

    public boolean isEmpty() {
        return this == NULL;
    }

    public MaterialVariant addCondition(RegisterCondition... registerCondition) {
        registerConditionList.addAll(List.of(registerCondition));
        return this;
    }

    public MaterialVariant removeCondition(RegisterCondition... registerCondition) {
        registerConditionList.removeAll(List.of(registerCondition));
        return this;
    }

    public MaterialVariant addAction(RegisterAction... registerAction) {
        registerActionList.addAll(List.of(registerAction));
        return this;
    }

    public MaterialVariant removeAction(RegisterAction... registerAction) {
        registerActionList.removeAll(List.of(registerAction));
        return this;
    }

    public void register(BreaRegistryCore registrate, Material material) throws IllegalArgumentException {
        if (!registerConditionList.isEmpty()) {
            for (RegisterCondition condition : registerConditionList) {
                if (!condition.validate(material)) return;
            }
        }
        if (itemCreativeTab.get() != null)
            registrate.defaultCreativeTab(itemCreativeTab.get().getKey());
        for (RegisterAction action : registerActionList) {
            action.register(registrate, this, material);
        }
    }

    public String getUnlocalizedName() {
        return "variant." + FormattingUtil.toLowerCaseUnderscore(id);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var variant = (MaterialVariant) o;
        return id.equals(variant.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }

    @SuppressWarnings("unchecked")
    public TagKey<Item>[] getItemTags(@NotNull Material mat) {
        return tags.stream().filter(type -> !type.isParentTag()).map(type -> type.getTag(this, mat))
                .filter(Objects::nonNull)
                .toArray(TagKey[]::new);
    }

    public boolean isAmountModified(Material material) {
        return materialAmounts.containsKey(material);
    }

    public long getMaterialAmount(@NotNull Material material) {
        if (material.isNull() || !isAmountModified(material)) {
            return this.materialAmount;
        }
        return (long) (BreaApi.M * materialAmounts.getFloat(material));
    }
}
