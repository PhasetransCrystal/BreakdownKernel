package net.phasetranscrystal.breacore.data.materials.material;

import net.phasetranscrystal.brealib.BreaLib;

import net.phasetranscrystal.breacore.api.material.registry.MaterialBuilder;

import static net.phasetranscrystal.breacore.api.material.info.MaterialFlags.*;
import static net.phasetranscrystal.breacore.data.materials.BreaMaterialIconSet.*;
import static net.phasetranscrystal.breacore.data.materials.BreaMaterials.*;

public class OrganicChemistryMaterials {

    public static void register() {
        Sugar = new MaterialBuilder(BreaLib.id("sugar"))
                .gem(1)
                .color(0xFFFFFF).secondaryColor(0x545468).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 6, Hydrogen, 12, Oxygen, 6)
                .buildAndRegister();
    }
}
