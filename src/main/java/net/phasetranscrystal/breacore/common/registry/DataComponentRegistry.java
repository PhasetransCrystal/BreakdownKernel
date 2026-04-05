package net.phasetranscrystal.breacore.common.registry;

import net.phasetranscrystal.breacore.api.equipforge.data.EquipForgeData;
import net.phasetranscrystal.breacore.api.perk.RecordPerkProvider;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;

import com.tterrag.registrate.util.entry.RegistryEntry;

public class DataComponentRegistry {

    public static void bootstrap() {}

    public static final RegistryEntry<DataComponentType<?>, DataComponentType<RecordPerkProvider>> PERK_RECORD_PROVIDER = BreaRegistration.REGISTRATE.simple("perk_record_provider", Registries.DATA_COMPONENT_TYPE, () -> DataComponentType.<RecordPerkProvider>builder().persistent(RecordPerkProvider.CODEC).build());

    public static final RegistryEntry<DataComponentType<?>, DataComponentType<RecordPerkProvider>> PERK_RECORD_PROVIDER4TEST = BreaRegistration.REGISTRATE.simple("perk_record_provider4test", Registries.DATA_COMPONENT_TYPE, () -> DataComponentType.<RecordPerkProvider>builder().persistent(RecordPerkProvider.CODEC).build());

    public static final RegistryEntry<DataComponentType<?>, DataComponentType<EquipForgeData>> EQUIP_FORGE_DATA = BreaRegistration.REGISTRATE.simple("equip_forge_data", Registries.DATA_COMPONENT_TYPE, () -> DataComponentType.<EquipForgeData>builder().persistent(EquipForgeData.CODEC).build());
}
