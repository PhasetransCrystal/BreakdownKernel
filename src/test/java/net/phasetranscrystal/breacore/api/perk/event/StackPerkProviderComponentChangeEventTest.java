package net.phasetranscrystal.breacore.api.perk.event;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StackPerkProviderComponentChangeEventTest {

    @Test
    void eventConstruction() {
        ItemStack oldStack = new ItemStack(Items.DIAMOND_HELMET);
        ItemStack newStack = new ItemStack(Items.GOLDEN_HELMET);
        
        StackPerkProviderComponentChangeEvent event = new StackPerkProviderComponentChangeEvent(
            null, EquipmentSlot.HEAD, oldStack, newStack
        );

        assertEquals(EquipmentSlot.HEAD, event.getSlot());
        assertEquals(oldStack, event.getOldStack());
        assertEquals(newStack, event.getNewStack());
    }

    @Test
    void emptyToItemTransition() {
        ItemStack newStack = new ItemStack(Items.DIAMOND_HELMET);
        
        StackPerkProviderComponentChangeEvent event = new StackPerkProviderComponentChangeEvent(
            null, EquipmentSlot.HEAD, ItemStack.EMPTY, newStack
        );

        assertTrue(event.getOldStack().isEmpty());
        assertFalse(event.getNewStack().isEmpty());
    }

    @Test
    void itemToEmptyTransition() {
        ItemStack oldStack = new ItemStack(Items.DIAMOND_HELMET);
        
        StackPerkProviderComponentChangeEvent event = new StackPerkProviderComponentChangeEvent(
            null, EquipmentSlot.HEAD, oldStack, ItemStack.EMPTY
        );

        assertFalse(event.getOldStack().isEmpty());
        assertTrue(event.getNewStack().isEmpty());
    }

    @Test
    void differentSlots() {
        ItemStack helmet = new ItemStack(Items.DIAMOND_HELMET);
        ItemStack chestplate = new ItemStack(Items.DIAMOND_CHESTPLATE);
        
        StackPerkProviderComponentChangeEvent headEvent = new StackPerkProviderComponentChangeEvent(
            null, EquipmentSlot.HEAD, ItemStack.EMPTY, helmet
        );
        
        StackPerkProviderComponentChangeEvent chestEvent = new StackPerkProviderComponentChangeEvent(
            null, EquipmentSlot.CHEST, ItemStack.EMPTY, chestplate
        );

        assertEquals(EquipmentSlot.HEAD, headEvent.getSlot());
        assertEquals(EquipmentSlot.CHEST, chestEvent.getSlot());
    }
}
