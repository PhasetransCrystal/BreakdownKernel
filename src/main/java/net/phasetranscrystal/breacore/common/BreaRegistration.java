package net.phasetranscrystal.breacore.common;

import net.phasetranscrystal.breacore.BreakdownCore;
import net.phasetranscrystal.breacore.api.registry.registrate.BreaGroup;
import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistryCore;
import net.phasetranscrystal.breacore.common.data.BreaTags;

import net.minecraft.world.item.CreativeModeTabs;

public class BreaRegistration {

    /**
     * 共享的注册核心。 使用 {@link BreaRegistryCore} 而非普通 {@link
     * net.phasetranscrystal.registrylib.RegistryCore}， 使得每个 builder 链上可直接调用 {@code .langCn("中文名")}。
     */
    public static final BreaRegistryCore REGISTRATE = BreaRegistryCore.create(BreakdownCore.MOD_ID);
    public static final BreaGroup DEBUG_REGISTRATE = (BreaGroup) REGISTRATE.group("debug")
            .addItemTag(BreaTags.DEBUG_ITEM)
            .tab(CreativeModeTabs.OP_BLOCKS)
            .build();
}
