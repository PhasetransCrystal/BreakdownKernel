package net.ptcrys.breakdown.api.machine.multiblock;

import net.ptcrys.breakdown.api.BreakdownApiPlugin;
import net.ptcrys.breakdown.api.machine.BuiltinBdApiMachineUiLang;
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
public final class BuiltinBdApiPropertyDisplays {

    private static final MachineDomainRegistration MACHINE = BreakdownApiPlugin.INSTANCE.machine();

    /** Both vanilla facing properties (6-way and horizontal) under one "Facing" label. */
    public static final PropertyDisplay FACING = MACHINE.propertyEnum(
            "facing",
            BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_FACING,
            Map.of(
                    Direction.NORTH, BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_FACING_NORTH,
                    Direction.SOUTH, BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_FACING_SOUTH,
                    Direction.EAST, BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_FACING_EAST,
                    Direction.WEST, BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_FACING_WEST,
                    Direction.UP, BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_FACING_UP,
                    Direction.DOWN, BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_FACING_DOWN),
            BlockStateProperties.FACING,
            BlockStateProperties.HORIZONTAL_FACING);

    public static final PropertyDisplay HALF = MACHINE.propertyEnum(
            "half",
            BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_HALF,
            Map.of(
                    Half.TOP, BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_HALF_TOP,
                    Half.BOTTOM, BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_HALF_BOTTOM),
            BlockStateProperties.HALF);

    public static final PropertyDisplay STAIR_SHAPE = MACHINE.propertyEnum(
            "stair_shape",
            BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_STAIR_SHAPE,
            Map.of(
                    StairsShape.STRAIGHT, BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_STAIR_SHAPE_STRAIGHT,
                    StairsShape.INNER_LEFT, BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_STAIR_SHAPE_INNER_LEFT,
                    StairsShape.INNER_RIGHT, BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_STAIR_SHAPE_INNER_RIGHT,
                    StairsShape.OUTER_LEFT, BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_STAIR_SHAPE_OUTER_LEFT,
                    StairsShape.OUTER_RIGHT, BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_STAIR_SHAPE_OUTER_RIGHT),
            BlockStateProperties.STAIRS_SHAPE);

    public static final PropertyDisplay AXIS = MACHINE.propertyEnum(
            "axis",
            BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_AXIS,
            Map.of(
                    Direction.Axis.X, BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_AXIS_X,
                    Direction.Axis.Y, BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_AXIS_Y,
                    Direction.Axis.Z, BuiltinBdApiMachineUiLang.UI_MULTIBLOCK_STATE_AXIS_Z),
            BlockStateProperties.AXIS);

    private BuiltinBdApiPropertyDisplays() {}

    public static void init() {
        Objects.requireNonNull(AXIS);
    }
}
