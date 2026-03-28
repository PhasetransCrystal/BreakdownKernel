package net.phasetranscrystal.breacore.common.registry;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.phasetranscrystal.breacore.api.perk.RecordPerkProvider;

public class DataComponentRegistry {
    public static void bootstrap(){}

    public static final RegistryEntry<DataComponentType<?>, DataComponentType<RecordPerkProvider>> PERK_RECORD_PROVIDER =
            BreaRegistration.REGISTRATE.simple("perk_record_provider", Registries.DATA_COMPONENT_TYPE, () -> DataComponentType.<RecordPerkProvider>builder().persistent(RecordPerkProvider.CODEC).build());

}
