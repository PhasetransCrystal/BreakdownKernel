package net.phasetranscrystal.breacore.common.event;

import net.phasetranscrystal.breacore.common.registry.DataComponentRegistry;

import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

@EventBusSubscriber
public class EquipForgeEventHandler {

    @SubscribeEvent
    public static void onItemAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty() || !stack.has(DataComponentRegistry.EQUIP_FORGE_DATA)) {
            return;
        }

        stack.get(DataComponentRegistry.EQUIP_FORGE_DATA).compute().forEach(modif -> modif.put(event));
    }
}
