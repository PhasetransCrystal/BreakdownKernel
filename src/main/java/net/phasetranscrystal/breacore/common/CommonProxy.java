package net.phasetranscrystal.breacore.common;

import net.phasetranscrystal.breacore.BreakdownCore;
import net.phasetranscrystal.breacore.api.BreaApi;
import net.phasetranscrystal.breacore.api.addon.AddonFinder;
import net.phasetranscrystal.breacore.api.addon.IBreaAddon;
import net.phasetranscrystal.breacore.api.material.event.PostMaterialEvent;
import net.phasetranscrystal.breacore.api.material.register.MaterialVariant;
import net.phasetranscrystal.breacore.api.material.registry.MaterialRegistry;
import net.phasetranscrystal.breacore.api.registry.BreaRegistries;
import net.phasetranscrystal.breacore.common.data.*;
import net.phasetranscrystal.breacore.config.ConfigHolder;
import net.phasetranscrystal.breacore.data.Datagen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import static net.phasetranscrystal.breacore.common.BreaRegistration.REGISTRATE;

public class CommonProxy {

    public CommonProxy() {
        var modBus = BreakdownCore.getModEventBus();
        BreaApi.materialManager = BreaRegistries.MATERIALS;
        ConfigHolder.init();
        modBus.register(CommonProxy.class);
        init();

        BreaRegistries.init(modBus);
        modBus.addListener(CommonProxy::modConstruct);
    }

    public static void init() {
        BreaTags.init();
        BreaCreativeModeTabs.init();

        initMaterials();

        BreaFluids.init();
        BreaBlocks.init();
        BreaBlockEntities.init();
        BreaEntityTypes.init();
        BreaMachines.init();

        BreaItems.init();

        AddonFinder.getAddonList().forEach(IBreaAddon::initComplete);

        BreaRegistrateDatagen.init();
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
        REGISTRATE.defaultCreativeTab(BreaCreativeModeTabs.MATERIAL_ITEM.getKey());
        for (var material : managerInternal) {
            for (var variant : MaterialVariant.values()) {
                variant.register(REGISTRATE, material);
            }
        }
    }

    private static void modConstruct(FMLConstructModEvent event) {
        Datagen.init();
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
