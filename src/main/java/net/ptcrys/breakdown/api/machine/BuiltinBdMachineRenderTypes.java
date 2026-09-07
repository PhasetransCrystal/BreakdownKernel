package net.ptcrys.breakdown.api.machine;

import net.ptcrys.breakdown.api.BreakdownApiPlugin;
import net.ptcrys.breakdown.api.machine.common.render.*;
import net.ptcrys.topo.api.api.builtin.MachineDomainRegistration;

/** Builtin machine block render types via {@link BreakdownApiPlugin#machine()}. */
public final class BuiltinBdMachineRenderTypes {

    private static final MachineDomainRegistration MACHINE = BreakdownApiPlugin.INSTANCE.machine();

    public static final ShellMachineRenderType SHELL = MACHINE.renderType(
            "shell",
            new ShellMachineRenderType(MACHINE.id("shell")),
            new ShellMachineRenderType.Strategy());

    public static final ShellOverlayMachineRenderType SHELL_OVERLAY = MACHINE.renderType(
            "shell_overlay",
            new ShellOverlayMachineRenderType(MACHINE.id("shell_overlay")),
            new ShellOverlayMachineRenderType.Strategy());

    public static final FormedActiveMachineRenderType FORMED_ACTIVE = MACHINE.renderType(
            "formed_active",
            new FormedActiveMachineRenderType(MACHINE.id("formed_active")),
            new FormedActiveMachineRenderType.Strategy());

    public static final CtmMachineRenderType CTM_MACHINE = MACHINE.renderType(
            "ctm_machine",
            new CtmMachineRenderType(MACHINE.id("ctm_machine")),
            new CtmMachineRenderType.Strategy());

    public static final CtmHatchRenderType CTM_HATCH = MACHINE.renderType(
            "ctm_hatch",
            new CtmHatchRenderType(MACHINE.id("ctm_hatch")),
            new CtmHatchRenderType.Strategy());

    private BuiltinBdMachineRenderTypes() {}

    public static void init() {}
}
