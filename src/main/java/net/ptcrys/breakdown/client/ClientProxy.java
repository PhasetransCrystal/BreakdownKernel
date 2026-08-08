package net.ptcrys.breakdown.client;

import net.ptcrys.breakdown.common.CommonProxy;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        super();
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {}

    @SubscribeEvent
    public static void loadComplete(FMLLoadCompleteEvent event) {}

    @SubscribeEvent
    public static void registerDataPackRegistries(DataPackRegistryEvent.NewRegistry event) {}

    @SubscribeEvent
    public static void registerPackFinders(AddPackFindersEvent event) {}

    @SubscribeEvent
    public static void addValidBlocksToBETypes(BlockEntityTypeAddBlocksEvent event) {}
}
