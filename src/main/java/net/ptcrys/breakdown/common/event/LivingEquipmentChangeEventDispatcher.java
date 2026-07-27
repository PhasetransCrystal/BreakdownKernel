package net.ptcrys.breakdown.common.event;

import net.ptcrys.breakdown.BreakdownKernal;
import net.ptcrys.breakdown.api.perk.event.StackPerkProviderComponentChangeEvent;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

@EventBusSubscriber(modid = BreakdownKernal.MOD_ID)
public class LivingEquipmentChangeEventDispatcher {

    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        /*
         * LivingEntity entity = event.getEntity();
         * EquipmentSlot slot = event.getSlot();
         * ItemStack oldStack = event.getFrom();
         * ItemStack newStack = event.getTo();
         * 
         * Map<Perk, Float> oldPerkStacks = PerkAttachment.collectPerkStacks(oldStack, slot);
         * Map<Perk, Float> newPerkStacks = PerkAttachment.collectPerkStacks(newStack, slot);
         * 
         * PerkAttachment.getOrCreate(entity).updateEquipment(entity, slot, newStack, oldPerkStacks, newPerkStacks);
         */
    }

    @SubscribeEvent
    public static void onStackPerkProviderComponentChange(StackPerkProviderComponentChangeEvent event) {
        /*
         * LivingEntity entity = event.getEntity();
         * EquipmentSlot slot = event.getSlot();
         * ItemStack oldStack = event.getOldStack();
         * ItemStack newStack = event.getNewStack();
         * 
         * if (ItemStack.matches(oldStack, newStack)) {
         * return;
         * }
         * 
         * Map<Perk, Float> oldPerkStacks = PerkAttachment.collectPerkStacks(oldStack, slot);
         * Map<Perk, Float> newPerkStacks = PerkAttachment.collectPerkStacks(newStack, slot);
         * 
         * PerkAttachment.getOrCreate(entity).updateEquipment(entity, slot, newStack, oldPerkStacks, newPerkStacks);
         */
    }
}
