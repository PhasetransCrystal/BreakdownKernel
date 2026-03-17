package net.phasetranscrystal.breacore.data.items;

import net.phasetranscrystal.breacore.api.BreaApi;
import net.phasetranscrystal.breacore.api.item.MaterialItem;
import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.material.variants.MaterialVariant;
import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistrate;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;

import static net.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;
import static net.phasetranscrystal.breacore.data.misc.BreaCreativeModeTabs.*;

public class MaterialItems {

    // Reference Tables
    public static Table<MaterialVariant, Material, ItemEntry<MaterialItem>> MATERIAL_ITEMS;
    // Reference Table Builders
    static ImmutableTable.Builder<MaterialVariant, Material, ItemEntry<MaterialItem>> MATERIAL_ITEMS_BUILDER = ImmutableTable
            .builder();

    public static void init() {
        REGISTRATE.creativeModeTab(() -> MATERIAL_ITEM);
        for (var variant : MaterialVariant.values()) {
            if (variant.doGenerateItem()) {
                for (var mat : BreaApi.materialManager) {
                    var registrate = BreaRegistrate.createIgnoringListenerErrors(mat.getModId());
                    if (variant.doGenerateItem(mat)) {
                        generateMaterialItem(variant, mat, registrate);
                    }
                }
            }
        }
        MATERIAL_ITEMS = MATERIAL_ITEMS_BUILDER.build();
    }

    private static void generateMaterialItem(MaterialVariant variant, Material mat, BreaRegistrate registrate) {
        var item = registrate.item(variant.idPattern().formatted(mat.getName()), prop -> new MaterialItem(variant, mat, prop))
                .onRegister(MaterialItem::onRegister)
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .properties(p -> p.stacksTo(variant.maxStackSize()))
                .register();
        MATERIAL_ITEMS_BUILDER.put(variant, mat, item);
    }
}
