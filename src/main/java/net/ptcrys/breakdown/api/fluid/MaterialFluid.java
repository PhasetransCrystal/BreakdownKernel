package net.ptcrys.breakdown.api.fluid;

import net.ptcrys.breakdown.api.material.Material;
import net.ptcrys.breakdown.api.material.register.MaterialVariant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public abstract class MaterialFluid extends BaseFlowingFluid {

    @Getter
    private final MaterialVariant variant;
    @Getter
    private final Material material;

    public MaterialFluid(MaterialVariant variant, Material mat, Properties properties) {
        super(properties);
        this.variant = variant;
        this.material = mat;
    }

    @Override
    protected boolean canBeReplacedWith(net.minecraft.world.level.material.FluidState state, BlockGetter level,
                                        BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !isSame(fluid);
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 10;
    }

    @Override
    protected int getSlopeFindDistance(LevelReader level) {
        return 4;
    }

    @Override
    protected int getDropOff(LevelReader level) {
        return 1;
    }

    @Override
    public boolean isSame(Fluid fluid) {
        boolean still = this.getSource() == fluid;
        boolean flowing = this.getFlowing() == fluid;
        return still || flowing;
    }

    public static class Source extends MaterialFluid {

        public Source(MaterialVariant variant, Material mat, Properties properties) {
            super(variant, mat, properties);
        }

        @Override
        public int getAmount(net.minecraft.world.level.material.FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(net.minecraft.world.level.material.FluidState state) {
            return true;
        }
    }

    public static class Flowing extends MaterialFluid {

        public Flowing(MaterialVariant variant, Material mat, Properties properties) {
            super(variant, mat, properties);
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.@NotNull Builder<Fluid, net.minecraft.world.level.material.FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(net.minecraft.world.level.material.FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(net.minecraft.world.level.material.FluidState state) {
            return false;
        }
    }
}
