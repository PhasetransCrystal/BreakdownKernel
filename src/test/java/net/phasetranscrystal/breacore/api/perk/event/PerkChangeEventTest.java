package net.phasetranscrystal.breacore.api.perk.event;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.phasetranscrystal.breacore.api.perk.Perk;
import net.phasetranscrystal.breacore.api.perk.PerkChangeType;
import net.phasetranscrystal.breacore.api.perk.PerkInfo;
import net.phasetranscrystal.brealib.BreaLib;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PerkChangeEventTest {

    @Test
    void perkChangeEventConstruction() {
        Perk perk = new Perk(BreaLib.id("test"), net.phasetranscrystal.breacore.api.perk.PerkStackingType.SUM) {};
        PerkInfo info = new PerkInfo(perk, 1.0f, Map.of());
        
        PerkChangeEvent event = new PerkChangeEvent(
            null, perk, PerkChangeType.ADD, 0f, 1.0f, info
        );

        assertEquals(perk, event.getPerk());
        assertEquals(PerkChangeType.ADD, event.getChangeType());
        assertEquals(0f, event.getOldLevel());
        assertEquals(1.0f, event.getNewLevel());
        assertEquals(info, event.getPerkInfo());
        assertNull(event.getEntity());
    }

    @Test
    void changeTypeEnumValues() {
        assertEquals(3, PerkChangeType.values().length);
        assertEquals(PerkChangeType.ADD, PerkChangeType.valueOf("ADD"));
        assertEquals(PerkChangeType.CHANGE, PerkChangeType.valueOf("CHANGE"));
        assertEquals(PerkChangeType.REMOVE, PerkChangeType.valueOf("REMOVE"));
    }
}
