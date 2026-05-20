package net.phasetranscrystal.breacore.api.damage;

import net.phasetranscrystal.breacore.api.equipment.EntityShieldAttachment;
import net.phasetranscrystal.breacore.api.magic.Element;
import net.phasetranscrystal.breacore.common.registry.AttributeRegistry;
import net.phasetranscrystal.breacore.utils.AttributeHelper;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.CommonHooks;

import com.tterrag.registrate.util.entry.RegistryEntry;

import java.util.List;

/**
 * 通用的护甲上下文默认实现，可在未接入正式攻击流程前直接用于计算测试。
 */
public final class SimpleDamageArmorContext implements DamageArmorContext {

    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[] {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    private final Entity rootAttacker;
    private final Entity directAttacker;
    private final LivingEntity victim;
    private final ItemStack weapon;
    private final EntityShieldAttachment shieldAttachment;

    private final double hardArmorValue;
    private final double softArmorValue;
    private final double criticalDamageReduction;

    /**
     * @param rootAttacker   根源攻击实体（如发射者）
     * @param directAttacker 直接攻击实体（如弹射物本体）
     * @param victim         受击实体
     * @param weapon         使用的武器物品
     */
    public SimpleDamageArmorContext(
                                    Entity rootAttacker,
                                    Entity directAttacker,
                                    LivingEntity victim,
                                    ItemStack weapon) {
        this.rootAttacker = rootAttacker;
        this.directAttacker = directAttacker;
        this.victim = victim;
        this.weapon = weapon;

        this.shieldAttachment = EntityShieldAttachment.getOrNull(victim);
        this.hardArmorValue = AttributeHelper.getValueOrDefault(victim, Attributes.ARMOR);
        this.softArmorValue = AttributeHelper.getValueOrDefault(victim, Attributes.ARMOR_TOUGHNESS);
        this.criticalDamageReduction = AttributeHelper.getValueOrDefault(victim, AttributeRegistry.CRITICAL_DAMAGE_REDUCING);
    }

    @Override
    public Entity getRootAttacker() {
        return rootAttacker;
    }

    @Override
    public Entity getDirectAttacker() {
        return directAttacker;
    }

    @Override
    public LivingEntity getVictim() {
        return victim;
    }

    @Override
    public ItemStack getWeapon() {
        return weapon;
    }

    @Override
    public double getSpellShieldHealth() {
        return shieldAttachment == null ? 0.0 : shieldAttachment.getCurrentShieldHealth();
    }

    @Override
    public void setSpellShieldHealth(double health) {
        double nextHealth = Math.max(0.0, health);

        if (shieldAttachment != null) {
            shieldAttachment.setShieldHealth(nextHealth);
        }
    }

    @Override
    public double getSpellShieldSturdiness() {
        return shieldAttachment == null ? 0.0 : shieldAttachment.getSpellSturdiness();
    }

    @Override
    public double getSpellShieldDurability() {
        return shieldAttachment == null ? 0.0 : shieldAttachment.getSpellDurability();
    }

    @Override
    public void setSpellShieldDurability(double durability) {
        double nextDurability = Math.max(0.0, durability);

        if (shieldAttachment == null) {
            return;
        }

        shieldAttachment.setShieldDurability(nextDurability);
    }

    @Override
    public double getHardArmorValue() {
        return hardArmorValue;
    }

    @Override
    public double getSoftArmorValue() {
        return softArmorValue;
    }

    @Override
    public double getCriticalDamageReduction() {
        return criticalDamageReduction;
    }

    @Override
    public double getElementResistance(Element element) {
        RegistryEntry<Attribute, Attribute> attribute = AttributeRegistry.getSpellDamageResistanceAttribute(element);
        if (attribute == null) {
            return 0.0;
        }
        return AttributeHelper.getValueOrDefault(victim, attribute);
    }

    @Override
    public double applyArmorDurabilityLoss(BreaDamageSource damageSource, double armorDurabilityLoss) {
        if (armorDurabilityLoss <= 0.0) {
            return 0.0;
        }

        double remaining = applyGlobalChestInsert(armorDurabilityLoss);
        if (remaining <= 0.0) {
            return 0.0;
        }

        List<EquipmentSlot> effectiveArmorSlots = resolveEffectiveArmorSlots();
        if (effectiveArmorSlots.isEmpty()) {
            return 0.0;
        }

        double splitLoss = remaining / effectiveArmorSlots.size();
        CommonHooks.onArmorHurt(
                damageSource,
                effectiveArmorSlots.toArray(EquipmentSlot[]::new),
                (float) splitLoss,
                victim);
        return remaining;
    }

    private double applyGlobalChestInsert(double armorDurabilityLoss) {
        // TODO 胸甲插板全局生效逻辑：若存在插板，优先消耗插板并减少 remaining。
        return armorDurabilityLoss;
    }

    private List<EquipmentSlot> resolveEffectiveArmorSlots() {
        return List.of(ARMOR_SLOTS).stream()
                .filter(slot -> {
                    ItemStack armorStack = victim.getItemBySlot(slot);
                    return !armorStack.isEmpty() && !isArmorBroken(armorStack, slot);
                })
                .toList();
    }

    private boolean isArmorBroken(ItemStack armorStack, EquipmentSlot slot) {
        // TODO 对接你的护甲损坏状态系统：为 true 时该护甲不再损耗耐久，也不计入有效护甲件数。
        return false;
    }
}
