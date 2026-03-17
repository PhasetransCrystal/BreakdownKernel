package net.phasetranscrystal.breacore.data.fluids;

import net.phasetranscrystal.brealib.api.registry.registrate.BreaFluidTypeExtensions;

import net.phasetranscrystal.breacore.api.BreaApi;
import net.phasetranscrystal.breacore.api.fluid.MaterialFluid;
import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.material.attributes.AttributeType;
import net.phasetranscrystal.breacore.api.material.variants.MaterialVariant;
import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistrate;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

import com.tterrag.registrate.AbstractRegistrate;
import org.jetbrains.annotations.NotNull;

import static net.phasetranscrystal.breacore.common.registry.BreaRegistration.REGISTRATE;
import static net.phasetranscrystal.breacore.data.misc.BreaCreativeModeTabs.MATERIAL_FLUID;

public class MaterialFluids {

    public static void init() {
        REGISTRATE.creativeModeTab(() -> MATERIAL_FLUID);
        for (var variant : MaterialVariant.values()) {
            if (variant.doGenerateFluid()) {
                for (var mat : BreaApi.materialManager) {
                    var registrate = BreaRegistrate.createIgnoringListenerErrors(mat.getModId());
                    if (variant.doGenerateFluid(mat)) {
                        generateMaterialFluid(variant, mat, registrate);
                    }
                }
            }
        }
    }

    private static void generateMaterialFluid(MaterialVariant variant, Material mat, BreaRegistrate registrate) {
        var fluid_id = variant.idPattern().formatted(mat.getName());
        var fluid = registrate.fluid(fluid_id, BreaFluidTypeExtensions.FLUID_LIQUID_STILL, BreaFluidTypeExtensions.FLUID_LIQUID_FLOWING,
                p -> makeFluidType(registrate, p, mat, variant.langValue),
                p -> new MaterialFluid.Flowing(variant, mat, p))
                .source(p -> new MaterialFluid.Source(variant, mat, p));
        var fluidAttr = mat.getAttribute(AttributeType.FLUID);
        if (!fluidAttr.isWithBucket()) fluid.noBucket();
        if (!fluidAttr.isWithBlock()) fluid.noBlock();
        fluid.register();
    }

    private static FluidType makeFluidType(AbstractRegistrate<?> owner, FluidType.Properties properties,
                                           Material material, String langKey) {
        properties.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY);
        return new FluidType(properties) {

            @Override
            public @NotNull String getDescriptionId() {
                return material.getUnlocalizedName();
            }

            @Override
            public @NotNull Component getDescription() {
                return Component.translatable(langKey, material.getLocalizedName());
            }

            @Override
            public @NotNull Component getDescription(@NotNull FluidStack stack) {
                return this.getDescription();
            }
        };
    }
}
