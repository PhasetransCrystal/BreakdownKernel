package net.ptcrys.breakdown.data.material;

import net.ptcrys.breakdown.data.BreakdownPlugin;
import net.ptcrys.topo.api.api.builtin.MaterialDomainRegistration;
import net.ptcrys.topo.api.material.Material;
import net.ptcrys.topo.api.material.MaterialRegistry;
import net.ptcrys.topo.api.material.form.MaterialForm;
import net.ptcrys.topo.data.material.BuiltinTopoMaterialForms;

import static net.ptcrys.topo.data.material.BuiltinTopoMaterialDataTypes.*;

public class BdMaterials {

    private static final MaterialDomainRegistration MATERIAL = BreakdownPlugin.INSTANCE.material();
    /** Stone / deepslate host ores (overworld). */
    private static final MaterialForm[] OVERWORLD_ORES = {
            BuiltinTopoMaterialForms.ORE,
            BuiltinTopoMaterialForms.DEEPSLATE_ORE
    };

    /** Extra nether host ore (composes onto overworld metal set). */
    private static final MaterialForm[] NETHER_ORE = {
            BuiltinTopoMaterialForms.NETHERRACK_ORE
    };
    /** Ore-processing intermediates (dust / slurry ladder + rare element). */
    private static final MaterialForm[] ORE_PROCESSING = {
            BuiltinTopoMaterialForms.DUST,
            BuiltinTopoMaterialForms.CRUDE_DUST,
            BuiltinTopoMaterialForms.PURIFIED_DUST,
            BuiltinTopoMaterialForms.TINY_DUST,
            BuiltinTopoMaterialForms.SLURRY,
            BuiltinTopoMaterialForms.NEUTRALIZED_SLURRY,
            BuiltinTopoMaterialForms.HIGH_PURITY_DUST,
            BuiltinTopoMaterialForms.ACTIVATED_DUST,
            BuiltinTopoMaterialForms.RESONANT_DUST,
            BuiltinTopoMaterialForms.RARE_ELEMENT
    };

    /** Bulk metal stock. */
    private static final MaterialForm[] METAL_STOCK = {
            BuiltinTopoMaterialForms.INGOT,
            BuiltinTopoMaterialForms.NUGGET,
            BuiltinTopoMaterialForms.BLOCK
    };
    /** Cold-formed / machined metal parts. */
    private static final MaterialForm[] METAL_PARTS = {
            BuiltinTopoMaterialForms.PLATE,
            BuiltinTopoMaterialForms.ROD,
            BuiltinTopoMaterialForms.GEAR,
            BuiltinTopoMaterialForms.BOLT,
            BuiltinTopoMaterialForms.FRAME,
            BuiltinTopoMaterialForms.ROTOR,
            BuiltinTopoMaterialForms.PLATE_DENSE
    };

    /** Cable tier ladder. */
    private static final MaterialForm[] WIRE_TIERS = {
            BuiltinTopoMaterialForms.WIRE_1X,
            BuiltinTopoMaterialForms.WIRE_2X,
            BuiltinTopoMaterialForms.WIRE_4X,
            BuiltinTopoMaterialForms.WIRE_8X,
            BuiltinTopoMaterialForms.WIRE_16X
    };
    /** Full metal form set without nether ore. */
    private static final MaterialForm[] METAL_FORMS = forms(
            OVERWORLD_ORES, ORE_PROCESSING, METAL_STOCK, METAL_PARTS, WIRE_TIERS);

    /** Full metal form set including netherrack ore. */
    private static final MaterialForm[] NETHER_METAL_FORMS = forms(
            OVERWORLD_ORES, NETHER_ORE, ORE_PROCESSING, METAL_STOCK, METAL_PARTS, WIRE_TIERS);

    public static final Material TIN = fullNetherMetal("tin", "Tin", "锡", 0xC9D3D8, 0x8EA0A8, 119);
    public static final Material LEAD = fullNetherMetal("lead", "Lead", "铅", 0x5E617B, 0x363A52, 207);
    public static final Material NICKEL = fullMetal("nickel", "Nickel", "镍", 0xD7C996, 0x9C8F63, 59);
    public static final Material ZINC = fullMetal("zinc", "Zinc", "锌", 0xC7D6D2, 0x8A9D98, 65);
    public static final Material URANIUM = fullNetherMetal("uranium", "Uranium", "铀", 0x61C853, 0x2D6B2E, 238);

    private BdMaterials() {}

    public static void init() {}

    private static Material fullMetal(String id, String en, String cn, int primary, int secondary, int mass) {
        return material(id, en, cn, primary, secondary, mass)
                .forms(METAL_FORMS)
                .build();
    }

    private static Material fullNetherMetal(String id, String en, String cn, int primary, int secondary, int mass) {
        return material(id, en, cn, primary, secondary, mass)
                .forms(NETHER_METAL_FORMS)
                .build();
    }

    private static Material simple(
                                   String id,
                                   String en,
                                   String cn,
                                   int primary,
                                   int secondary,
                                   int mass,
                                   MaterialForm... forms) {
        return material(id, en, cn, primary, secondary, mass)
                .forms(forms)
                .build();
    }

    /** Concatenate form packs in declaration order (single source packs, no duplicated matrices). */
    private static MaterialForm[] forms(MaterialForm[]... packs) {
        int total = 0;
        for (MaterialForm[] pack : packs) {
            total += pack.length;
        }
        MaterialForm[] out = new MaterialForm[total];
        int offset = 0;
        for (MaterialForm[] pack : packs) {
            System.arraycopy(pack, 0, out, offset, pack.length);
            offset += pack.length;
        }
        return out;
    }

    private static MaterialRegistry.Builder material(
                                                     String id,
                                                     String en,
                                                     String cn,
                                                     int primary,
                                                     int secondary,
                                                     int mass) {
        return MATERIAL.material(id)
                .lang(en, cn)
                .data(PRIMARY_COLOR.rgb(primary))
                .data(SECONDARY_COLOR.rgb(secondary))
                .data(MASS.mass(mass));
    }
}
