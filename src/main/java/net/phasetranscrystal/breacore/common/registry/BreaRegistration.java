package net.phasetranscrystal.breacore.common.registry;

import net.phasetranscrystal.breacore.BreakdownCore;
import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistrate;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class BreaRegistration {

    public static final BreaRegistrate REGISTRATE = BreaRegistrate.create(BreakdownCore.MOD_ID);

    static {
        REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
    }

    private BreaRegistration() {}
}
