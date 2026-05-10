package net.phasetranscrystal.breacore.api.equipment;

import lombok.Getter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.phasetranscrystal.breacore.common.registry.AttachmentTypeRegistry;

/**
 * 实体护盾附件：用于存储护盾物品、当前护盾值与法术抗性。
 */
@Getter
public class EntityShieldAttachment {

    public static final AttachmentType<EntityShieldAttachment> TYPE = AttachmentTypeRegistry.ENTITY_SHIELD.get();

    private ItemStack shieldItem = ItemStack.EMPTY;
    private double currentShieldHealth = 0.0;

    public static EntityShieldAttachment getOrCreate(LivingEntity entity) {
        return entity.getData(TYPE);
    }

    public static EntityShieldAttachment getOrNull(LivingEntity entity) {
        return entity.getExistingDataOrNull(TYPE);
    }

    public void setShieldItem(ItemStack shieldItem) {
        ItemStack oldShieldItem = this.shieldItem;
        this.shieldItem = shieldItem == null ? ItemStack.EMPTY : shieldItem;
        onShieldItemChanged(oldShieldItem, this.shieldItem);
    }

    public ItemStack takeShieldItem() {
        if (this.shieldItem.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack oldShieldItem = this.shieldItem;
        ItemStack stack = shieldItem;
        this.shieldItem = ItemStack.EMPTY;
        onShieldItemChanged(oldShieldItem, this.shieldItem);
        return stack;
    }

    public void setShieldHealth(double currentShieldHealth) {
        this.currentShieldHealth = Math.max(0.0, currentShieldHealth);
        //TODO 护盾掉血事件
    }

    public double getSpellSturdiness() {
        return 0; //TODO get from shield item
    }

    public double getSpellDurability() {
        return 0; //TODO
    }

    public void setShieldDurability(double shieldDurability) {
        // TODO 护盾系统接线点（属于护盾 attachment 领域，不应放在护甲上下文）：
        // 1) 根据护盾耐久变化计算护盾物品耐久损耗
        // 2) 根据损耗结果触发护盾破碎/失效逻辑
        // 3) 派发对应事件并同步客户端状态
    }

    private void onShieldItemChanged(ItemStack oldShieldItem, ItemStack newShieldItem) {
        // TODO 护盾物品变化后的接线入口：
        // 1) 刷新护盾相关 attribute
        // 2) 发布护盾更换/移除事件
        // 3) 对接护盾系统的持久化与同步
    }
}
