package net.ptcrys.breakdown.api.machine.multiblock;

import net.ptcrys.breakdown.api.BreakdownApiPlugin;
import net.ptcrys.breakdown.api.machine.BuiltinBdMachineUiLang;
import net.ptcrys.topo.api.api.builtin.MachineDomainRegistration;
import net.ptcrys.topo.api.api.lang.LangKey;
import net.ptcrys.topo.api.machine.multiblock.ui.PropertyDisplay;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

import java.util.Map;
import java.util.Objects;

/**
 * Builtin {@link PropertyDisplay} handles via {@link BreakdownApiPlugin#machine()}. Passes catalog
 * {@link LangKey}s; domain converts to components (code-style §3.12.3).
 */
public final class BuiltinBdPropertyDisplays {

    private static final MachineDomainRegistration MACHINE = BreakdownApiPlugin.INSTANCE.machine();

    /** Both vanilla facing properties (6-way and horizontal) under one "Facing" label. */
    public static final PropertyDisplay FACING = MACHINE.propertyEnum(
            "facing",
            BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_FACING,
            Map.of(
                    Direction.NORTH, BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_FACING_NORTH,
                    Direction.SOUTH, BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_FACING_SOUTH,
                    Direction.EAST, BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_FACING_EAST,
                    Direction.WEST, BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_FACING_WEST,
                    Direction.UP, BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_FACING_UP,
                    Direction.DOWN, BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_FACING_DOWN),
            BlockStateProperties.FACING,
            BlockStateProperties.HORIZONTAL_FACING);

    public static final PropertyDisplay HALF = MACHINE.propertyEnum(
            "half",
            BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_HALF,
            Map.of(
                    Half.TOP, BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_HALF_TOP,
                    Half.BOTTOM, BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_HALF_BOTTOM),
            BlockStateProperties.HALF);

    public static final PropertyDisplay STAIR_SHAPE = MACHINE.propertyEnum(
            "stair_shape",
            BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_STAIR_SHAPE,
            Map.of(
                    StairsShape.STRAIGHT, BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_STAIR_SHAPE_STRAIGHT,
                    StairsShape.INNER_LEFT, BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_STAIR_SHAPE_INNER_LEFT,
                    StairsShape.INNER_RIGHT, BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_STAIR_SHAPE_INNER_RIGHT,
                    StairsShape.OUTER_LEFT, BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_STAIR_SHAPE_OUTER_LEFT,
                    StairsShape.OUTER_RIGHT, BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_STAIR_SHAPE_OUTER_RIGHT),
            BlockStateProperties.STAIRS_SHAPE);

    public static final PropertyDisplay AXIS = MACHINE.propertyEnum(
            "axis",
            BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_AXIS,
            Map.of(
                    Direction.Axis.X, BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_AXIS_X,
                    Direction.Axis.Y, BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_AXIS_Y,
                    Direction.Axis.Z, BuiltinBdMachineUiLang.UI_MULTIBLOCK_STATE_AXIS_Z),
            BlockStateProperties.AXIS);

    private BuiltinBdPropertyDisplays() {}

    public static void init() {
        Objects.requireNonNull(AXIS);
    }
}
