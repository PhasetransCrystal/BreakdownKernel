package net.phasetranscrystal.breacore.api.perk;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.phasetranscrystal.breacore.api.registry.BreaRegistries;
import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistrate;
import net.phasetranscrystal.breacore.common.registry.BreaRegistration;
import net.phasetranscrystal.breacore.common.registry.DataComponentRegistry;
import net.phasetranscrystal.brealib.BreaLib;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IPerkProviderTest {

    ItemStack emptyStack;
    ItemStack diamondHelmet;

    static Perk testPerk;
    static Perk testPerk2;
    static {
        BreaRegistries.PERKS.unfreeze(true);
        testPerk = BreaRegistries.PERKS.register(BreaLib.id("test"), new Perk(BreaLib.id("test"), PerkStackingType.SUM));
        testPerk2 = BreaRegistries.PERKS.register(BreaLib.id("test2"), new Perk(BreaLib.id("test2"), PerkStackingType.SUM));
        BreaRegistries.PERKS.freeze();
    }
    public static final RegistryEntry<DataComponentType<?>, DataComponentType<RecordPerkProvider>> PERK_RECORD_PROVIDER_2 =
            BreaRegistration.REGISTRATE.simple("perk_record_provider_2", Registries.DATA_COMPONENT_TYPE, () -> DataComponentType.<RecordPerkProvider>builder().persistent(RecordPerkProvider.CODEC).build());

    @BeforeEach
    void setup() {
        emptyStack = ItemStack.EMPTY;
        diamondHelmet = new ItemStack(Items.DIAMOND_HELMET);
    }

    @Test
    void collectPerkStacksEmptyStack() {
        Map<Perk, Float> result = PerkAttachment.collectPerkStacks(emptyStack, EquipmentSlot.HEAD);
        assertTrue(result.isEmpty());
    }

    @Test
    void collectPerkStacksStackWithoutProvider() {
        Map<Perk, Float> result = PerkAttachment.collectPerkStacks(diamondHelmet, EquipmentSlot.HEAD);
        assertTrue(result.isEmpty());
    }

    @Test
    void collectPerkStacksWithProvider() {
        RecordPerkProvider provider = new RecordPerkProvider(EquipmentSlotGroup.HEAD,testPerk, 1.0f);
        diamondHelmet.set(DataComponentRegistry.PERK_RECORD_PROVIDER.get(),provider);

        Map<Perk, Float> result = PerkAttachment.collectPerkStacks(diamondHelmet, EquipmentSlot.HEAD);
        
        assertFalse(result.isEmpty());
        assertEquals(1.0f, result.get(testPerk));
    }

    @Test
    void collectPerkStacksMultipleProviders() {
        RecordPerkProvider provider1 = new RecordPerkProvider(EquipmentSlotGroup.HEAD, testPerk, 1.0f);
        RecordPerkProvider provider2 = new RecordPerkProvider(EquipmentSlotGroup.HEAD, testPerk2, 2.0f);
        
        diamondHelmet.set(DataComponentRegistry.PERK_RECORD_PROVIDER.get(), provider1);
        diamondHelmet.set(PERK_RECORD_PROVIDER_2.get(), provider2);

        Map<Perk, Float> result = PerkAttachment.collectPerkStacks(diamondHelmet, EquipmentSlot.HEAD);
        
        assertEquals(2, result.size());
        assertEquals(1.0f, result.get(testPerk));
        assertEquals(2.0f, result.get(testPerk2));
    }
}
