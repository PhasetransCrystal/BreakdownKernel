package net.phasetranscrystal.breacore.api.perk;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public record PerkInfo(
                       Perk perk,
                       float level,
                       Map<EquipmentSlot, ItemStack> itemStacks) {

    public ItemStack getItemStack(EquipmentSlot slot) {
        return itemStacks.get(slot);
    }
}
