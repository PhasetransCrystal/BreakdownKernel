package net.ptcrys.breakdown.data.material;

import net.ptcrys.breakdown.data.BreakdownPlugin;
import net.ptcrys.breakdown.data.vanilla.BdCreativeTabs;
import net.ptcrys.topo.api.api.builtin.MaterialDomainRegistration;
import net.ptcrys.topo.api.material.form.FormDataUse;
import net.ptcrys.topo.api.material.form.MaterialForm;
import net.ptcrys.topo.data.material.common.form.ItemForm;
import net.ptcrys.topo.data.material.common.render.TintedTemplate;

import static net.ptcrys.topo.data.material.BuiltinTopoFormDataTypes.AMOUNT;
import static net.ptcrys.topo.data.material.BuiltinTopoMaterialDataTypes.*;
import static net.ptcrys.topo.data.material.common.render.TintedTemplate.*;

public class BdMaterialForms {

    private static final MaterialDomainRegistration MATERIAL = BreakdownPlugin.INSTANCE.material();
    public static final MaterialForm DUST = dustForm(
            "dust",
            "%s Dust",
            "%s粉",
            "粉",
            "%s_dust",
            "item/material/dust",
            "item/material/dust_secondary");

    public static final MaterialForm PLATE = componentForm(
            "plate",
            "Plate",
            "板材",
            "%s Plate",
            "%s板材",
            "%s_plate",
            "plates",
            AMOUNT.ingots(1),
            "item/material/plate",
            "item/material/plate_secondary",
            "item/material/plate_overlay");

    public static final MaterialForm ROD = componentForm(
            "rod",
            "Rod",
            "杆",
            "%s Rod",
            "%s杆",
            "%s_rod",
            "rods",
            AMOUNT.ingotFraction(2),
            "item/material/rod",
            "item/material/rod_secondary",
            null);

    private BdMaterialForms() {}

    public static void init() {}

    private static MaterialForm dustForm(
                                         String id,
                                         String displayNameEn,
                                         String displayNameCn,
                                         String langCn,
                                         String registryPath,
                                         String primaryTexture,
                                         String secondaryTexture) {
        return MATERIAL.form(id)
                .lang(displayNameEn.replace("%s ", ""), langCn)
                .strategy(ItemForm.builder()
                        .registryPath(registryPath)
                        .displayName(displayNameEn, displayNameCn)
                        .commonTag(id + "s")
                        .creativeTab(BdCreativeTabs.MATERIAL_ITEM.getKey())
                        .render(TintedTemplate.item(
                                tinted(PRIMARY_COLOR, primaryTexture),
                                tinted(SECONDARY_COLOR, secondaryTexture)))
                        .build())
                .build();
    }

    private static MaterialForm componentForm(
                                              String id,
                                              String langEn,
                                              String langCn,
                                              String displayNameEn,
                                              String displayNameCn,
                                              String registryPath,
                                              String commonTag,
                                              FormDataUse<Integer> amount,
                                              String primaryTexture,
                                              String secondaryTexture,
                                              String overlayTexture) {
        TintedTemplate.Layer primary = tinted(PRIMARY_COLOR, primaryTexture);
        TintedTemplate.Layer secondary = tinted(SECONDARY_COLOR, secondaryTexture);
        TintedTemplate.Layer[] layers = overlayTexture == null ? new TintedTemplate.Layer[] { primary, secondary } : new TintedTemplate.Layer[] { primary, secondary, flat(overlayTexture) };
        return MATERIAL.form(id)
                .lang(langEn, langCn)
                .strategy(ItemForm.builder()
                        .registryPath(registryPath)
                        .displayName(displayNameEn, displayNameCn)
                        .commonTag(commonTag)
                        .data(amount)
                        .creativeTab(BdCreativeTabs.MATERIAL_ITEM.getKey())
                        .render(TintedTemplate.item(layers))
                        .build())
                .build();
    }
}
