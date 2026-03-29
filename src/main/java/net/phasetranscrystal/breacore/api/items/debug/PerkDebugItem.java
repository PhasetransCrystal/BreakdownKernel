package net.phasetranscrystal.breacore.api.items.debug;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.phasetranscrystal.breacore.api.perk.Perk;
import net.phasetranscrystal.breacore.api.perk.PerkAttachment;
import net.phasetranscrystal.breacore.api.perk.PerkInfo;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PerkDebugItem extends Item {

    private static final String KEY_PREFIX = "item.breacore.perk_debug.";

    public PerkDebugItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity target, InteractionHand usedHand) {
        if (player.level().isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        showPerkStatus(player, target);
        return InteractionResult.CONSUME;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (player.isCrouching()) {
            showPerkStatus(player, player);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    private void showPerkStatus(Player player, LivingEntity entity) {
        Map<Perk, PerkInfo> perkInfos = PerkAttachment.getAllPerkInfos(entity);
        Map<Perk, Float> perkLevels = PerkAttachment.getAllPerkLevels(entity);

        player.sendSystemMessage(Component.translatable(KEY_PREFIX + "title", getEntityName(entity))
                .withStyle(ChatFormatting.GOLD));

        if (perkInfos.isEmpty()) {
            player.sendSystemMessage(Component.translatable(KEY_PREFIX + "no_perks")
                    .withStyle(ChatFormatting.GRAY));
        } else {
            for (Map.Entry<Perk, PerkInfo> entry : perkInfos.entrySet()) {
                var perk = entry.getKey();
                var info = entry.getValue();
                float level = perkLevels.getOrDefault(perk, 0f);

                player.sendSystemMessage(Component.empty());
                player.sendSystemMessage(Component.translatable(KEY_PREFIX + "perk_id", perk.getId().toString())
                        .withStyle(ChatFormatting.YELLOW));
                player.sendSystemMessage(Component.translatable(KEY_PREFIX + "level", level)
                        .withStyle(ChatFormatting.GRAY));
                player.sendSystemMessage(Component.translatable(KEY_PREFIX + "stacking_type", perk.getStackingType().toString())
                        .withStyle(ChatFormatting.GRAY));

                var modifiers = perk.getAttributeModifiers(entity, level);
                if (!modifiers.isEmpty()) {
                    player.sendSystemMessage(Component.translatable(KEY_PREFIX + "attribute_modifiers")
                            .withStyle(ChatFormatting.GRAY));
                    for (var modifier : modifiers) {
                        String attrName = modifier.attribute().unwrapKey()
                                .map(k -> k.identifier().toString())
                                .orElse("unknown");
                        player.sendSystemMessage(Component.translatable(
                                KEY_PREFIX + "attribute_modifier_line",
                                attrName,
                                modifier.operation().toString(),
                                modifier.value()
                        ).withStyle(ChatFormatting.WHITE));
                    }
                }

                var consumers = perk.getEventConsumers(info);
                if (!consumers.isEmpty()) {
                    player.sendSystemMessage(Component.translatable(KEY_PREFIX + "event_consumers", consumers.size())
                            .withStyle(ChatFormatting.GRAY));
                }
            }
        }
    }

    private Component getEntityName(Entity entity) {
        if (entity instanceof Player player) {
            return player.getName();
        }
        return entity.getType().getDescription();
    }

    @Override
    public void appendHoverText(ItemStack itemStack, net.minecraft.world.item.Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, net.minecraft.world.item.TooltipFlag tooltipFlag) {
        builder.accept(Component.translatable(KEY_PREFIX + "tooltip.entity").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.translatable(KEY_PREFIX + "tooltip.player").withStyle(ChatFormatting.GRAY));
    }
}
