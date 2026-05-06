package net.phasetranscrystal.breacore.api.damage;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.phasetranscrystal.breacore.api.event.EntityKillEvent;

@EventBusSubscriber
public class Test {
    @SubscribeEvent
    public static void forTest(EntityKillEvent.Post event){
        System.out.println(event.toString());
    }
}
