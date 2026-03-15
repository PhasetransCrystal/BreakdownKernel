package net.phasetranscrystal.breacore.api.item;

import net.phasetranscrystal.brealib.BreaLib;

import net.phasetranscrystal.breacore.api.BreaApi;
import net.phasetranscrystal.breacore.api.material.Material;
import net.phasetranscrystal.breacore.api.material.property.DustProperty;
import net.phasetranscrystal.breacore.api.material.property.PropertyKey;
import net.phasetranscrystal.breacore.api.tag.TagPrefix;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class TagPrefixItem extends Item {

    public final TagPrefix tagPrefix;
    public final Material material;

    public TagPrefixItem(Properties properties, TagPrefix tagPrefix, Material material) {
        super(properties);
        this.tagPrefix = tagPrefix;
        this.material = material;
        if (BreaLib.isClientSide()) {
            // TagPrefixItemRenderer.create(this, tagPrefix.materialIconType(), material.getMaterialIconSet());
        }
    }

    public void onRegister() {}

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType, FuelValues fuelValues) {
        return getItemBurnTime();
    }

    public int getItemBurnTime() {
        DustProperty property = material.isNull() ? null : material.getProperty(PropertyKey.DUST);
        if (property != null)
            return (int) (property.getBurnTime() * tagPrefix.getMaterialAmount(material) / BreaApi.M);
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipComponents, isAdvanced);
        if (this.tagPrefix.tooltip() != null) {
            this.tagPrefix.tooltip().accept(material, tooltipComponents);
        }
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return tagPrefix.getLocalizedName(material);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, entity, slot);
    }
}
