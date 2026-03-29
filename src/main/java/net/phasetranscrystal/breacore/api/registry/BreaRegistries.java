package net.phasetranscrystal.breacore.api.registry;

import net.phasetranscrystal.brealib.BreaLib;

import net.phasetranscrystal.breacore.api.material.Element;
import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.material.registry.MaterialRegistry;
import net.phasetranscrystal.breacore.api.perk.Perk;
import net.phasetranscrystal.breacore.api.sound.SoundEntry;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

public class BreaRegistries {

    public static final Identifier ROOT_REGISTRY_NAME = BreaLib.id("root");
    public static final BreaRegistry<BreaRegistry<?>> ROOT = new BreaRegistry<>(ROOT_REGISTRY_NAME);
    // TODO ResourceKey
    public static final ResourceKey<Registry<Material>> MATERIAL_KEY = makeRegistryKey(BreaLib.id("material"));
    public static final ResourceKey<Registry<Element>> ELEMENT_KEY = makeRegistryKey(BreaLib.id("element"));
    public static final BreaRegistry<Element> ELEMENTS = new BreaRegistry<>(ELEMENT_KEY);
    public static final MaterialRegistry MATERIALS = new MaterialRegistry(MATERIAL_KEY);

    public static final ResourceKey<Registry<SoundEntry>> SOUND_KEY = makeRegistryKey(BreaLib.id("sound"));
    public static final BreaRegistry<SoundEntry> SOUNDS = new BreaRegistry<>(SOUND_KEY);

    public static final ResourceKey<Registry<Perk>> PERK_KEY = makeRegistryKey(BreaLib.id("perk"));
    public static final BreaRegistry<Perk> PERKS = new BreaRegistry<>(PERK_KEY);

    public static <T> ResourceKey<Registry<T>> makeRegistryKey(Identifier registryId) {
        return ResourceKey.createRegistryKey(registryId);
    }

    private static final Table<Registry<?>, Identifier, Object> TO_REGISTER = HashBasedTable.create();
    private static final RegistryAccess BLANK = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
    private static RegistryAccess FROZEN = BLANK;

    public static <V, T extends V> T register(Registry<V> registry, Identifier name, T value) {
        TO_REGISTER.put(registry, name, value);
        return value;
    }

    // ignore the generics and hope the registered objects are still correctly typed :3
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void actuallyRegister(RegisterEvent event) {
        for (Registry reg : TO_REGISTER.rowKeySet()) {
            event.register(reg.key(), helper -> {
                TO_REGISTER.row(reg).forEach(helper::register);
            });
        }
    }

    public static void init(IEventBus eventBus) {
        Consumer<RegisterEvent> actuallyRegister = BreaRegistries::actuallyRegister;
        eventBus.addListener(actuallyRegister);
    }

    /**
     * You shouldn't call it, you should probably not even look at it just to be extra safe
     *
     * @param registryAccess the new value to set to the frozen registry access
     */
    @ApiStatus.Internal
    public static void updateFrozenRegistry(RegistryAccess registryAccess) {
        FROZEN = registryAccess;
    }

    public static RegistryAccess builtinRegistry() {
        if (FROZEN == BLANK && BreaLib.isClientThread()) {
            if (Minecraft.getInstance().getConnection() != null) {
                return Minecraft.getInstance().getConnection().registryAccess();
            }
        }
        return FROZEN;
    }
}
