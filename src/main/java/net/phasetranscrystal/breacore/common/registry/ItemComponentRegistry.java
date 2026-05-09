package net.phasetranscrystal.breacore.common.registry;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.phasetranscrystal.breacore.api.damage.WeaponDamageProfile;

public final class ItemComponentRegistry {

    public static void bootstrap() {
        // 触发类加载
    }

    public static final RegistryEntry<DataComponentType<?>, DataComponentType<WeaponDamageProfile>> WEAPON_DAMAGE_PROFILE =
            BreaRegistration.REGISTRATE.simple(
                    "weapon_damage_profile",
                    Registries.DATA_COMPONENT_TYPE,
                    () -> DataComponentType.<WeaponDamageProfile>builder()
                            .persistent(WeaponDamageProfile.CODEC)
                            .build()
            );

    private ItemComponentRegistry() {}
}
