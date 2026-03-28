package net.phasetranscrystal.breacore.api.perk.event;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

import lombok.Getter;

@Getter
public class StackPerkProviderComponentChangeEvent extends Event {

    private final LivingEntity entity;
    private final EquipmentSlot slot;
    private final ItemStack oldStack;
    private final ItemStack newStack;

    public StackPerkProviderComponentChangeEvent(LivingEntity entity, EquipmentSlot slot, ItemStack oldStack, ItemStack newStack) {
        this.entity = entity;
        this.slot = slot;
        this.oldStack = oldStack;
        this.newStack = newStack;
    }
}
