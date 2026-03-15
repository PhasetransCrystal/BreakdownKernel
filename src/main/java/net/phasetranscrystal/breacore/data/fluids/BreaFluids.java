package net.phasetranscrystal.breacore.data.fluids;

import net.phasetranscrystal.brealib.BreaLib;

import net.phasetranscrystal.breacore.api.BreaApi;
import net.phasetranscrystal.breacore.api.fluid.potion.PotionFluid;
import net.phasetranscrystal.breacore.api.fluid.store.FluidStorageKeys;
import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.material.property.PropertyKey;
import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistrate;
import net.phasetranscrystal.breacore.data.materials.BreaMaterials;
import net.phasetranscrystal.breacore.data.misc.BreaCreativeModeTabs;
import net.phasetranscrystal.breacore.data.tags.CustomTags;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForgeMod;

import com.tterrag.registrate.util.entry.FluidEntry;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static net.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;

public class BreaFluids {

    @SuppressWarnings("UnstableApiUsage")
    public static final FluidEntry<PotionFluid> POTION = REGISTRATE
            .fluid("potion", BreaLib.id("block/fluids/fluid.potion"), BreaLib.id("block/fluids/fluid.potion"),
                    PotionFluid.PotionFluidType::new, PotionFluid::new)
            .lang("Potion")
            .source(PotionFluid::new).noBlock().noBucket()
            .tag(CustomTags.POTION_FLUIDS)
            .register();

    public static void init() {
        // Register fluids for non-materials
        handleNonMaterialFluids(BreaMaterials.Water, Fluids.WATER);
        handleNonMaterialFluids(BreaMaterials.Lava, Fluids.LAVA);
        handleNonMaterialFluids(BreaMaterials.Milk, NeoForgeMod.MILK);
        NeoForgeMod.enableMilkFluid();

        // register fluids for materials
        REGISTRATE.creativeModeTab(() -> BreaCreativeModeTabs.MATERIAL_FLUID);
        for (var material : BreaApi.materialManager) {
            var fluidProperty = material.getProperty(PropertyKey.FLUID);

            if (fluidProperty != null) {
                BreaRegistrate registrate = BreaRegistrate.createIgnoringListenerErrors(material.getModid());
                fluidProperty.registerFluids(material, registrate);
            }
        }
    }

    public static void handleNonMaterialFluids(@NotNull Material material, @NotNull Fluid fluid) {
        handleNonMaterialFluids(material, () -> fluid);
    }

    public static void handleNonMaterialFluids(@NotNull Material material, @NotNull Supplier<Fluid> fluid) {
        var property = material.getProperty(PropertyKey.FLUID);
        property.getStorage().store(FluidStorageKeys.LIQUID, fluid, null);
    }
}
