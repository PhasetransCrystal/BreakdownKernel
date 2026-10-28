package net.ptcrys.breakdown.api.material;

import net.ptcrys.breakdown.api.BreakdownApiPlugin;
import net.ptcrys.breakdown.api.material.common.form.BlockForm;
import net.ptcrys.breakdown.api.material.common.form.FluidForm;
import net.ptcrys.breakdown.api.material.common.form.ItemForm;
import net.ptcrys.breakdown.api.material.common.render.GtmMaterialBlockRender;
import net.ptcrys.breakdown.api.material.common.render.TintedTemplate;
import net.ptcrys.breakdown.api.vanilla.BuiltinBdApiCreativeTabs;
import net.ptcrys.topo.api.api.builtin.MaterialDomainRegistration;
import net.ptcrys.topo.api.material.form.FormDataUse;
import net.ptcrys.topo.api.material.form.MaterialForm;

import net.minecraft.world.level.block.Blocks;

import static net.ptcrys.breakdown.api.material.BuiltinBdApiFormDataTypes.*;
import static net.ptcrys.breakdown.api.material.BuiltinBdApiMaterialDataTypes.*;
import static net.ptcrys.breakdown.api.material.common.render.TintedTemplate.*;

/**
 * 瓦解核心内置材料形态(骨架):仅注册 Minecraft 原本就有的类型——
 * 矿石 / 粉 / 锭 / 粒 / 块 / 宝石。工业自定义构件(板/杆/齿轮等)不在此注册,
 * 由内容插件按需自定义。极简渲染为单层着色。
 */
public final class BuiltinBdApiMaterialForms {

    private static final MaterialDomainRegistration MATERIALS = BreakdownApiPlugin.INSTANCE.material();

    // ── 矿石方块 ────────────────────────────────────────────────────────────

    public static final MaterialForm ORE = MATERIALS.form("ore")
            .lang("Ore", "矿石")
            .strategy(BlockForm.builder()
                    .registryPath("%s_ore")
                    .displayName("%s Ore", "%s矿石")
                    .commonTag("ores")
                    .creativeTab(BuiltinBdApiCreativeTabs.MATERIALS.getKey())
                    .initialProperties(Blocks.STONE)
                    .render(TintedTemplate.block(
                            opaque(flat("block/material/ore_host_stone")),
                            translucent(tinted(PRIMARY_COLOR, "block/material/ore_overlay"))))
                    .build())
            .appliesToAll()
            .build();

    public static final MaterialForm DEEPSLATE_ORE = MATERIALS.form("deepslate_ore")
            .lang("Deepslate Ore", "深板岩矿石")
            .strategy(BlockForm.builder()
                    .registryPath("deepslate_%s_ore")
                    .displayName("Deepslate %s Ore", "深板岩%s矿石")
                    .commonTag("ores")
                    .creativeTab(BuiltinBdApiCreativeTabs.MATERIALS.getKey())
                    .initialProperties(Blocks.DEEPSLATE)
                    .render(TintedTemplate.block(
                            opaque(flat("block/material/ore_host_deepslate")),
                            translucent(tinted(PRIMARY_COLOR, "block/material/ore_overlay"))))
                    .build())
            .appliesToAll()
            .build();

    public static final MaterialForm NETHERRACK_ORE = MATERIALS.form("netherrack_ore")
            .lang("Netherrack Ore", "地狱岩矿石")
            .strategy(BlockForm.builder()
                    .registryPath("netherrack_%s_ore")
                    .displayName("Netherrack %s Ore", "地狱岩%s矿石")
                    .commonTag("ores")
                    .creativeTab(BuiltinBdApiCreativeTabs.MATERIALS.getKey())
                    .initialProperties(Blocks.NETHERRACK)
                    .render(TintedTemplate.block(
                            opaque(flat("block/material/ore_host_netherrack")),
                            translucent(tinted(PRIMARY_COLOR, "block/material/ore_overlay"))))
                    .build())
            .appliesToAll()
            .build();

    // ── 粉 ──────────────────────────────────────────────────────────────────

    public static final MaterialForm DUST = dustForm(
            "dust",
            "%s Dust",
            "%s粉",
            "粉",
            "%s_dust",
            "item/material/dust",
            "item/material/dust_secondary");

    // ── 基础产物 ─────────────────────────────────────────────────────────────

    public static final MaterialForm INGOT = MATERIALS.form("ingot")
            .lang("Ingot", "锭")
            .strategy(ItemForm.builder()
                    .registryPath("%s_ingot")
                    .displayName("%s Ingot", "%s锭")
                    .commonTag("ingots")
                    .data(AMOUNT.ingots(1))
                    .creativeTab(BuiltinBdApiCreativeTabs.MATERIALS.getKey())
                    .render(TintedTemplate.item(
                            tinted(PRIMARY_COLOR, "item/material/ingot"),
                            tinted(SECONDARY_COLOR, "item/material/ingot_secondary"),
                            flat("item/material/ingot_overlay")))
                    .build())
            .appliesToAll()
            .build();

    public static final MaterialForm NUGGET = MATERIALS.form("nugget")
            .lang("Nugget", "粒")
            .strategy(ItemForm.builder()
                    .registryPath("%s_nugget")
                    .displayName("%s Nugget", "%s粒")
                    .commonTag("nuggets")
                    .data(AMOUNT.ingotFraction(9))
                    .creativeTab(BuiltinBdApiCreativeTabs.MATERIALS.getKey())
                    .render(TintedTemplate.item(
                            tinted(PRIMARY_COLOR, "item/material/nugget"),
                            tinted(SECONDARY_COLOR, "item/material/nugget_secondary"),
                            flat("item/material/nugget_overlay")))
                    .build())
            .appliesToAll()
            .build();

    public static final MaterialForm BLOCK = MATERIALS.form("block")
            .lang("Block", "块")
            .strategy(BlockForm.builder()
                    .registryPath("%s_block")
                    .displayName("Block of %s", "%s块")
                    .commonTag("storage_blocks")
                    .data(AMOUNT.ingots(9))
                    .creativeTab(BuiltinBdApiCreativeTabs.MATERIALS.getKey())
                    .initialProperties(Blocks.IRON_BLOCK)
                    .render(new GtmMaterialBlockRender())
                    .build())
            .appliesToAll()
            .build();

    // ── 宝石 ─────────────────────────────────────────────────────────────────

    public static final MaterialForm GEM = MATERIALS.form("gem")
            .lang("Gem", "宝石")
            .strategy(ItemForm.builder()
                    .registryPath("%s_gem")
                    .displayName("%s Gem", "%s宝石")
                    .commonTag("gems")
                    .creativeTab(BuiltinBdApiCreativeTabs.MATERIALS.getKey())
                    .render(TintedTemplate.item(
                            tinted(PRIMARY_COLOR, "item/material/gem"),
                            tinted(SECONDARY_COLOR, "item/material/gem_secondary"),
                            flat("item/material/gem_overlay")))
                    .build())
            .appliesToAll()
            .build();

    // ── 流体(空壳)────────────────────────────────────────────────────────────

    /** 流体形态(空壳):元素等暂以流体登记,具体流体注册后续实现。 */
    public static final MaterialForm SOLUTION = MATERIALS.form("solution")
            .lang("Solution", "溶液")
            .strategy(FluidForm.builder()
                    .registryPath("%s_solution")
                    .displayName("%s Solution", "%s溶液")
                    .commonTag("solutions")
                    .creativeTab(BuiltinBdApiCreativeTabs.MATERIALS.getKey())
                    .texture("block/material/fluid")
                    .tint(PRIMARY_COLOR)
                    .properties(properties -> properties
                            .density(1200)
                            .viscosity(1600)
                            .temperature(300)
                            .motionScale(0.010D)
                            .canExtinguish(true))
                    .fluidProperties(properties -> properties
                            .slopeFindDistance(3)
                            .levelDecreasePerBlock(2)
                            .tickRate(8)
                            .explosionResistance(100.0F))
                    .build())
            .appliesToAll()
            .build();

    public static final MaterialForm GAS = MATERIALS.form("gas")
            .lang("Gas", "气体")
            .strategy(FluidForm.builder()
                    .registryPath("%s_gas")
                    .displayName("%s Gas", "%s气体")
                    .commonTag("gases")
                    .creativeTab(BuiltinBdApiCreativeTabs.MATERIALS.getKey())
                    .texture("block/material/fluid")
                    .tint(PRIMARY_COLOR)
                    .properties(properties -> properties
                            .density(-100)
                            .viscosity(100)
                            .temperature(295)
                            .motionScale(0.004D)
                            .canPushEntity(false)
                            .canSwim(false)
                            .canDrown(false)
                            .fallDistanceModifier(1.0F)
                            .pathType(null)
                            .adjacentPathType(null))
                    .fluidProperties(properties -> properties
                            .slopeFindDistance(6)
                            .levelDecreasePerBlock(1)
                            .tickRate(2)
                            .explosionResistance(1.0F))
                    .build())
            .appliesToAll()
            .build();

    private BuiltinBdApiMaterialForms() {}

    public static void init() {}

    private static MaterialForm dustForm(
                                         String id,
                                         String displayNameEn,
                                         String displayNameCn,
                                         String langCn,
                                         String registryPath,
                                         String primaryTexture,
                                         String secondaryTexture) {
        return MATERIALS.form(id)
                .appliesToAll()
                .lang(displayNameEn.replace("%s ", ""), langCn)
                .strategy(ItemForm.builder()
                        .registryPath(registryPath)
                        .displayName(displayNameEn, displayNameCn)
                        .commonTag(id + "s")
                        .creativeTab(BuiltinBdApiCreativeTabs.MATERIALS.getKey())
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
        return MATERIALS.form(id)
                .appliesToAll()
                .lang(langEn, langCn)
                .strategy(ItemForm.builder()
                        .registryPath(registryPath)
                        .displayName(displayNameEn, displayNameCn)
                        .commonTag(commonTag)
                        .data(amount)
                        .creativeTab(BuiltinBdApiCreativeTabs.MATERIALS.getKey())
                        .render(TintedTemplate.item(layers))
                        .build())
                .build();
    }
}
