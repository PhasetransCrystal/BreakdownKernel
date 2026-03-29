package net.phasetranscrystal.breacore.api.perk.client;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.AttributeTooltipContext;
import net.neoforged.neoforge.event.AddAttributeTooltipsEvent;

import java.util.List;

@EventBusSubscriber
public class PerkTooltipEvents {

    @SubscribeEvent
    public static void onAddAttributeTooltips(AddAttributeTooltipsEvent event) {
        ItemStack stack = event.getStack();
        if (stack.isEmpty()) {
            return;
        }

        List<PerkTooltipHelper.PerkTooltipEntry> entries = PerkTooltipHelper.getPerkEntries(stack);
        if (entries.isEmpty()) {
            return;
        }

        AttributeTooltipContext ctx = event.getContext();
        boolean advancedMode = ctx.flag().isAdvanced();

        Component header = Component.translatable("breacore.quench.tooltip.equipped_header")
                .withStyle(net.minecraft.ChatFormatting.GRAY);
        event.addTooltipLines(header);

        for (PerkTooltipHelper.PerkTooltipEntry entry : entries) {
            Component perkName = Component.translatable(entry.perkNameKey());
            Component levelText = Component.translatable("breacore.quench.tooltip.level_format", entry.level())
                    .withStyle(net.minecraft.ChatFormatting.GOLD);

            MutableComponent line = Component.empty()
                    .append("  ")
                    .append(perkName)
                    .append(" ")
                    .append(levelText);

            if (advancedMode) {
                Component perkId = Component.literal(" (" + entry.perkId() + ")")
                        .withStyle(net.minecraft.ChatFormatting.DARK_GRAY);
                line.append(perkId);
            }

            event.addTooltipLines(line);
        }
    }
}
