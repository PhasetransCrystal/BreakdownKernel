package net.phasetranscrystal.breacore.common.event;

import net.phasetranscrystal.breacore.BreakdownCore;
import net.phasetranscrystal.breacore.api.perk.Perk;
import net.phasetranscrystal.breacore.api.perk.PerkAttachment;
import net.phasetranscrystal.breacore.api.perk.event.StackPerkProviderComponentChangeEvent;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

import java.util.Map;

@EventBusSubscriber(modid = BreakdownCore.MOD_ID)
public class LivingEquipmentChangeEventDispatcher {

    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        LivingEntity entity = event.getEntity();
        EquipmentSlot slot = event.getSlot();
        ItemStack oldStack = event.getFrom();
        ItemStack newStack = event.getTo();

        Map<Perk, Float> oldPerkStacks = PerkAttachment.collectPerkStacks(oldStack, slot);
        Map<Perk, Float> newPerkStacks = PerkAttachment.collectPerkStacks(newStack, slot);

        PerkAttachment.getOrCreate(entity).updateEquipment(entity, slot, newStack, oldPerkStacks, newPerkStacks);
    }

    @SubscribeEvent
    public static void onStackPerkProviderComponentChange(StackPerkProviderComponentChangeEvent event) {
        LivingEntity entity = event.getEntity();
        EquipmentSlot slot = event.getSlot();
        ItemStack oldStack = event.getOldStack();
        ItemStack newStack = event.getNewStack();

        if (ItemStack.matches(oldStack, newStack)) {
            return;
        }

        Map<Perk, Float> oldPerkStacks = PerkAttachment.collectPerkStacks(oldStack, slot);
        Map<Perk, Float> newPerkStacks = PerkAttachment.collectPerkStacks(newStack, slot);

        PerkAttachment.getOrCreate(entity).updateEquipment(entity, slot, newStack, oldPerkStacks, newPerkStacks);
    }
}
