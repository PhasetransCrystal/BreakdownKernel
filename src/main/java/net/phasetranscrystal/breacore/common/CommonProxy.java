package net.phasetranscrystal.breacore.common;

import net.phasetranscrystal.breacore.BreakdownCore;
import net.phasetranscrystal.breacore.api.BreaApi;
import net.phasetranscrystal.breacore.api.addon.AddonFinder;
import net.phasetranscrystal.breacore.api.addon.IBreaAddon;
import net.phasetranscrystal.breacore.api.material.event.PostMaterialEvent;
import net.phasetranscrystal.breacore.api.material.registry.MaterialRegistry;
import net.phasetranscrystal.breacore.api.registry.BreaRegistries;
import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistrate;
import net.phasetranscrystal.breacore.data.blockentity.BreaBlockEntities;
import net.phasetranscrystal.breacore.data.blocks.BreaBlocks;
import net.phasetranscrystal.breacore.data.datagen.BreaRegistrateDatagen;
import net.phasetranscrystal.breacore.data.entity.BreaEntityTypes;
import net.phasetranscrystal.breacore.data.fluids.BreaFluids;
import net.phasetranscrystal.breacore.data.items.BreaItems;
import net.phasetranscrystal.breacore.data.machine.BreaMachines;
import net.phasetranscrystal.breacore.data.materials.BreaElements;
import net.phasetranscrystal.breacore.data.materials.BreaMaterials;
import net.phasetranscrystal.breacore.data.materials.MaterialVariants;
import net.phasetranscrystal.breacore.data.misc.BreaCreativeModeTabs;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public class CommonProxy {

    public CommonProxy() {
        var modBus = BreakdownCore.getModEventBus();
        BreaApi.materialManager = BreaRegistries.MATERIALS;
        modBus.register(CommonProxy.class);
        init();

        BreaRegistries.init(modBus);
    }

    public static void init() {
        initMaterials();

        BreaFluids.init();
        BreaCreativeModeTabs.init();
        BreaBlocks.init();
        BreaEntityTypes.init();
        BreaBlockEntities.init();
        BreaMachines.init();

        BreaItems.init();

        AddonFinder.getAddonList().forEach(IBreaAddon::initComplete);

        BreaRegistrateDatagen.init();
        // Register all oldmaterial manager registries, for materials with mod ids.
        BreaApi.materialManager.getUsedNamespaces().forEach(namespace -> {
            // Force the oldmaterial lang generator to be at index 0, so that addons' lang generators can override it.
            BreaRegistrate registrate = BreaRegistrate.createIgnoringListenerErrors(namespace);
            ModList.get().getModContainerById(namespace).map(ModContainer::getEventBus).ifPresent(registrate::registerEventListeners);
        });
    }

    private static void initMaterials() {
        BreaElements.init();
        MaterialRegistry managerInternal = (MaterialRegistry) BreaApi.materialManager;
        managerInternal.unfreezeRegistries();
        BreakdownCore.LOGGER.info("Registering Materials");
        BreaMaterials.init();
        managerInternal.setFallbackMaterial(BreakdownCore.MOD_ID, BreaMaterials.Actinium);
        BreakdownCore.LOGGER.info("Registering addon Materials");
        BreaApi.postRegisterEvent(BreaRegistries.MATERIALS);
        // Fire Post-Material event, intended for when Materials need to be iterated over in-full before freezing
        // Block entirely new Materials from being added in the Post event
        managerInternal.closeRegistries();
        ModLoader.postEventWrapContainerInModOrder(new PostMaterialEvent());

        // Freeze Material Registry before processing Items, Blocks, and Fluids
        managerInternal.freezeRegistries();
        /* End Material Registration */
        MaterialVariants.init();
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
