package net.phasetranscrystal.breacore.api.damage;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.phasetranscrystal.breacore.common.registry.AttachmentTypeRegistry;

/**
 * 实体护盾附件：用于存储护盾物品、当前护盾值与法术抗性。
 */
public class EntityShieldAttachment {

    public static final AttachmentType<EntityShieldAttachment> TYPE = AttachmentTypeRegistry.ENTITY_SHIELD.get();

    private ItemStack shieldItem = ItemStack.EMPTY;
    private double currentShieldValue = 0.0;
    private double spellResistance = 0.0;

    public static EntityShieldAttachment getOrCreate(LivingEntity entity) {
        return entity.getData(TYPE);
    }

    public static EntityShieldAttachment getOrNull(LivingEntity entity) {
        return entity.getExistingDataOrNull(TYPE);
    }

    public ItemStack getShieldItem() {
        return shieldItem;
    }

    public void setShieldItem(ItemStack shieldItem) {
        this.shieldItem = shieldItem == null ? ItemStack.EMPTY : shieldItem;
    }

    public double getCurrentShieldValue() {
        return currentShieldValue;
    }

    public void setCurrentShieldValue(double currentShieldValue) {
        this.currentShieldValue = Math.max(0.0, currentShieldValue);
    }

    public double getSpellResistance() {
        return spellResistance;
    }

    public void setSpellResistance(double spellResistance) {
        this.spellResistance = Math.clamp(spellResistance, 0.0, 1.0);
    }
}
