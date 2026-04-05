package net.phasetranscrystal.breacore.api.perk.test;

import net.phasetranscrystal.breacore.BreakdownCore;
import net.phasetranscrystal.breacore.api.perk.PerkHelper;
import net.phasetranscrystal.breacore.api.perk.RecordPerkProvider;
import net.phasetranscrystal.breacore.common.registry.BreaRegistration;
import net.phasetranscrystal.breacore.common.registry.DataComponentRegistry;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

import com.tterrag.registrate.util.entry.ItemEntry;

import java.util.function.Consumer;

public class TestPerkItems {

    public static final ItemEntry<QueryPerkItem> QUERY_PERK_ITEM = BreaRegistration.REGISTRATE
            .item("query_perk_item", QueryPerkItem::new).register();

    public static final ItemEntry<ClearPerkItem> CLEAR_PERK_ITEM = BreaRegistration.REGISTRATE
            .item("clear_perk_item", ClearPerkItem::new).register();

    public static void bootstrap() {
        BreakdownCore.getModEventBus().register(TestPerkItems.class);
    }

    @SubscribeEvent
    public static void onModifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        event.modify(Items.DIAMOND_HELMET, builder -> builder.set(
                DataComponentRegistry.PERK_RECORD_PROVIDER.get(),
                new RecordPerkProvider(EquipmentSlotGroup.HEAD, TestPerks.SUM, 1.0f)));

        event.modify(Items.DIAMOND_CHESTPLATE, builder -> builder.set(
                DataComponentRegistry.PERK_RECORD_PROVIDER.get(),
                new RecordPerkProvider(EquipmentSlotGroup.CHEST, TestPerks.SUM, 3.0f)));

        event.modify(Items.DIAMOND_LEGGINGS, builder -> builder.set(
                DataComponentRegistry.PERK_RECORD_PROVIDER.get(),
                new RecordPerkProvider(EquipmentSlotGroup.LEGS, TestPerks.MAX, 5.0f)));

        event.modify(Items.DIAMOND_BOOTS, builder -> builder.set(
                DataComponentRegistry.PERK_RECORD_PROVIDER.get(),
                new RecordPerkProvider(EquipmentSlotGroup.FEET, TestPerks.MAX, 8.0f)));

        event.modify(Items.SHIELD, builder -> builder.set(
                DataComponentRegistry.PERK_RECORD_PROVIDER.get(),
                new RecordPerkProvider(EquipmentSlotGroup.OFFHAND, TestPerks.AVERAGE, 2.0f)));

        event.modify(Items.GOLDEN_HELMET, builder -> builder.set(
                DataComponentRegistry.PERK_RECORD_PROVIDER.get(),
                new RecordPerkProvider(EquipmentSlotGroup.HEAD, TestPerks.ARMOR_PERK, 5.0f)));

        event.modify(Items.GOLDEN_BOOTS, builder -> builder.set(
                DataComponentRegistry.PERK_RECORD_PROVIDER.get(),
                new RecordPerkProvider(EquipmentSlotGroup.FEET, TestPerks.SPEED_PERK, 3.0f)));

        event.modify(Items.GOLDEN_CHESTPLATE, builder -> builder.set(
                DataComponentRegistry.PERK_RECORD_PROVIDER.get(),
                new RecordPerkProvider(EquipmentSlotGroup.CHEST, TestPerks.HEALTH_PERK, 2.0f)));

        event.modify(Items.IRON_HELMET, builder -> builder.set(
                DataComponentRegistry.PERK_RECORD_PROVIDER.get(),
                new RecordPerkProvider(EquipmentSlotGroup.HEAD, TestPerks.EVENT_PERK, 1.0f)));

        event.modify(Items.IRON_CHESTPLATE, builder -> builder.set(
                DataComponentRegistry.PERK_RECORD_PROVIDER.get(),
                new RecordPerkProvider(EquipmentSlotGroup.CHEST, TestPerks.COMBO_PERK, 2.0f)));

        event.modify(Items.IRON_LEGGINGS, builder -> builder.set(
                DataComponentRegistry.PERK_RECORD_PROVIDER.get(),
                new RecordPerkProvider(EquipmentSlotGroup.LEGS, TestPerks.COMBO_PERK_2, 1.0f)));

        event.modify(Items.NETHERITE_HELMET, builder -> builder.set(
                DataComponentRegistry.PERK_RECORD_PROVIDER.get(),
                new RecordPerkProvider(EquipmentSlotGroup.HEAD, TestPerks.COMBINED_A, 2.0f)));

        event.modify(Items.NETHERITE_CHESTPLATE, builder -> builder.set(
                DataComponentRegistry.PERK_RECORD_PROVIDER.get(),
                new RecordPerkProvider(EquipmentSlotGroup.CHEST, TestPerks.COMBINED_B, 3.0f)));

        event.modify(Items.DIAMOND_SWORD, builder -> builder.set(
                DataComponentRegistry.PERK_RECORD_PROVIDER.get(),
                new RecordPerkProvider(EquipmentSlotGroup.MAINHAND,
                        TestPerks.SUM, 1.0f,
                        TestPerks.COMBO_PERK, 1.0f,
                        TestPerks.COMBINED_A, 1.0f)));
    }

    public static class QueryPerkItem extends Item {

        public QueryPerkItem(Properties properties) {
            super(properties);
        }

        @Override
        public InteractionResult use(Level level, Player player, InteractionHand hand) {
            if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
                StringBuilder sb = new StringBuilder();
                sb.append("=== Perk Test Values ===\n");
                sb.append("attachedCalled: ").append(TestPerks.attachedCalled).append("\n");
                sb.append("detachedCalled: ").append(TestPerks.detachedCalled).append("\n");
                sb.append("levelChangedCalled: ").append(TestPerks.levelChangedCalled).append("\n");
                sb.append("oldLevelValue: ").append(TestPerks.oldLevelValue).append("\n");
                sb.append("newLevelValue: ").append(TestPerks.newLevelValue).append("\n");
                sb.append("eventTriggerCount: ").append(TestPerks.eventTriggerCount).append("\n");
                sb.append("eventTriggerPerkLevel: ").append(TestPerks.eventTriggerPerkLevel).append("\n");
                sb.append("Perk Levels:\n");
                for (var entry : PerkHelper.getAllPerkLevels(serverPlayer).entrySet()) {
                    sb.append("  ").append(entry.getKey().getId()).append(": ").append(entry.getValue()).append("\n");
                }

                serverPlayer.sendSystemMessage(Component.literal(sb.toString()));
            }
            return InteractionResult.SUCCESS;
        }

        @Override
        public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
            builder.accept(Component.literal("Right click: Query perk test values"));
        }
    }

    public static class ClearPerkItem extends Item {

        public ClearPerkItem(Properties properties) {
            super(properties);
        }

        @Override
        public InteractionResult use(Level level, Player player, InteractionHand hand) {
            if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
                TestPerks.resetFlags();
                serverPlayer.sendSystemMessage(Component.literal("Perk test values cleared!"));
            }
            return InteractionResult.SUCCESS;
        }

        @Override
        public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
            builder.accept(Component.literal("Right click: Clear perk test values"));
        }
    }
}
