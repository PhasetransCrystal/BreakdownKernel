package net.ptcrys.breakdown.common;

import net.ptcrys.breakdown.BreaLib;
import net.ptcrys.breakdown.api.BreaApi;
import net.ptcrys.breakdown.api.addon.AddonFinder;
import net.ptcrys.breakdown.api.addon.IBreaAddon;
import net.ptcrys.breakdown.api.material.event.PostMaterialEvent;
import net.ptcrys.breakdown.api.material.register.MaterialVariant;
import net.ptcrys.breakdown.api.material.registry.MaterialRegistry;
import net.ptcrys.breakdown.api.misc.AutoInitializeImpl;
import net.ptcrys.breakdown.api.registry.BreaRegistries;
import net.ptcrys.breakdown.api.registry.ScanningClass;
import net.ptcrys.breakdown.common.data.*;
import net.ptcrys.breakdown.config.ConfigHolder;
import net.ptcrys.breakdown.data.Datagen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import static net.ptcrys.breakdown.common.BreaRegistration.MATERIAL_REGISTRATE;
import static net.ptcrys.breakdown.common.BreaRegistration.REGISTRATE;

public class CommonProxy {

    public CommonProxy() {
        var modBus = BreaLib.getModEventBus();
        BreaApi.materialManager = BreaRegistries.MATERIALS;
        ConfigHolder.init();
        ScanningClass.init();
        modBus.register(CommonProxy.class);
        init();

        BreaRegistries.init(modBus);
        modBus.addListener(CommonProxy::modConstruct);
    }

    public static void init() {
        BreaTooltips.init();
        AutoInitializeImpl.INSTANCE.originInit();
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
        BreaLib.LOGGER.info("Registering Materials");
        BreaMaterials.init();
        managerInternal.setFallbackMaterial(BreaLib.Core_ID, BreaMaterials.Actinium);
        BreaLib.LOGGER.info("Registering addon Materials");
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
                variant.register(MATERIAL_REGISTRATE, material);
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
