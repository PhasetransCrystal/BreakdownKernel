package net.phasetranscrystal.breacore.client;

import net.phasetranscrystal.breacore.client.datagen.TextureCreater;
import net.phasetranscrystal.breacore.common.CommonProxy;

public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        super();
        TextureCreater.init();
    }
}
