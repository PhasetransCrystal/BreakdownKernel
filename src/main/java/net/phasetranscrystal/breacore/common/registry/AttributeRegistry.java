package net.phasetranscrystal.breacore.common.registry;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.phasetranscrystal.breacore.api.magic.Element;

import java.util.EnumMap;
import java.util.Map;

/**
 * BreaCore 属性注册。
 *
 * <p>说明：</p>
 * <ul>
 *     <li>硬质护甲/软质护甲直接使用原版 ARMOR / ARMOR_TOUGHNESS，不重复注册。</li>
 * </ul>
 */
public final class AttributeRegistry {

    public static final Map<Element, RegistryEntry<Attribute, Attribute>> SPELL_DAMAGE_AMPLIFICATION_BY_ELEMENT =
            new EnumMap<>(Element.class);
    public static final Map<Element, RegistryEntry<Attribute, Attribute>> SPELL_DAMAGE_RESISTANCE_BY_ELEMENT =
            new EnumMap<>(Element.class);

    static {
        for (Element element : Element.values()) {
            if (element == Element.NONE) {
                continue;
            }
            SPELL_DAMAGE_AMPLIFICATION_BY_ELEMENT.put(
                    element,
                    register(
                            "spell_damage_amplification_" + element.id,
                            0,
                            -1,
                            1024
                    )
            );
            SPELL_DAMAGE_RESISTANCE_BY_ELEMENT.put(
                    element,
                    register(
                            "spell_damage_resistance_" + element.id,
                            0,
                            -1024,
                            1
                    )
            );
        }
    }

    public static void bootstrap() {
        // 触发类加载，确保静态注册项加入 Registrate。
    }

    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        addPlayerAttribute(event, MAGIC_SHIELD_HEALTH);
        addPlayerAttribute(event, SUPER_ABILITY_CHARGING_SPEED);
        addPlayerAttribute(event, SUPER_ABILITY_TIME_FACTOR);
        addPlayerAttribute(event, SUPER_ABILITY_STRENGTH_FACTOR);
        addPlayerAttribute(event, SUPER_ABILITY_EFFECT_RANGE);
        addPlayerAttribute(event, OCCUPATION_SKILL_CHARGING_SPEED);
        addPlayerAttribute(event, OCCUPATION_SKILL_STRENGTH_FACTOR);
        addPlayerAttribute(event, CRITICAL_DAMAGE_REDUCING);
        addPlayerAttribute(event, CRITICAL_HIT);
        addPlayerAttribute(event, CRITICAL_DAMAGE);
        addPlayerAttribute(event, DAMAGE_ATTENUATION_START_DISTANCE);
        addPlayerAttribute(event, DAMAGE_ATTENUATION_END_DISTANCE);
        addPlayerAttribute(event, SHOOTING_SPEED);
        addPlayerAttribute(event, DISPERSION_RATE);
        addPlayerAttribute(event, STABILITY);
        addPlayerAttribute(event, HURTING_STABILITY);
        addPlayerAttribute(event, MAGNIFICATION);
        addPlayerAttribute(event, PREPARING_TIME);
        addPlayerAttribute(event, AIMING_TIME);
        addPlayerAttribute(event, RELOADING_TIME);
        addPlayerAttribute(event, PAYLOAD_CAPACITY);
        addPlayerAttribute(event, CHARGING_TIME_FACTOR);
        addPlayerAttribute(event, HARD_ARMOR_PENETRATION_VALUE);
        addPlayerAttribute(event, SOFT_ARMOR_PENETRATION_VALUE);
        addPlayerAttribute(event, DEBUFF_ACCUMULATION_RATE);
        addPlayerAttribute(event, DEBUFF_FADING_SPEED);
        addPlayerAttribute(event, DEBUFF_INTENSITY_FACTOR);
        addPlayerAttribute(event, DEBUFF_TIME_FACTOR);
        addPlayerAttribute(event, BUFF_INTENSITY_FACTOR);
        addPlayerAttribute(event, BUFF_TIME_FACTOR);
        addPlayerAttribute(event, NATURAL_RECOVERY);
        addPlayerAttribute(event, RECOVERY_FACTOR);
        addPlayerAttribute(event, ITEM_USING_SPEED_FACTOR);
        addPlayerAttribute(event, MAGICAL_SHIELD_RECOVERY);
        addPlayerElementalAttributes(event);
    }

    private static void addPlayerElementalAttributes(EntityAttributeModificationEvent event) {
        SPELL_DAMAGE_AMPLIFICATION_BY_ELEMENT.values().forEach(attribute -> addPlayerAttribute(event, attribute));
        SPELL_DAMAGE_RESISTANCE_BY_ELEMENT.values().forEach(attribute -> addPlayerAttribute(event, attribute));
    }

    private static void addPlayerAttribute(
            EntityAttributeModificationEvent event,
            RegistryEntry<Attribute, Attribute> attribute
    ) {
        event.add(EntityType.PLAYER, attribute);
    }

    // ========= 基础 =========

    /** 法术护盾上限。 */
    public static final RegistryEntry<Attribute, Attribute> MAGIC_SHIELD_HEALTH = register(
            "magic_shield_health", 0.0, 0.0, Float.MAX_VALUE
    );

    // ========= 技能 / 超能 =========

    /** 超能充能速度因子（乘数，默认 1）。 */
    public static final RegistryEntry<Attribute, Attribute> SUPER_ABILITY_CHARGING_SPEED = registerFactor(
            "super_ability_charging_speed"
    );

    /** 超能持续时间因子（乘数，默认 1）。 */
    public static final RegistryEntry<Attribute, Attribute> SUPER_ABILITY_TIME_FACTOR = registerFactor(
            "super_ability_time_factor"
    );

    /** 超能强度因子（乘数，默认 1）。 */
    public static final RegistryEntry<Attribute, Attribute> SUPER_ABILITY_STRENGTH_FACTOR = registerFactor(
            "super_ability_strength_factor"
    );

    /** 超能效果范围因子（乘数，默认 1）。 */
    public static final RegistryEntry<Attribute, Attribute> SUPER_ABILITY_EFFECT_RANGE = registerFactor(
            "super_ability_effect_range"
    );

    // ========= 技能 / 职业 =========

    /** 职业技能充能速度因子（乘数，默认 1）。 */
    public static final RegistryEntry<Attribute, Attribute> OCCUPATION_SKILL_CHARGING_SPEED = registerFactor(
            "occupation_skill_charging_speed"
    );

    /** 职业强度因子（乘数，默认 1）。 */
    public static final RegistryEntry<Attribute, Attribute> OCCUPATION_SKILL_STRENGTH_FACTOR = registerFactor(
            "occupation_skill_strength_factor"
    );

    // ========= 防御 =========

    /** 暴击伤害减免 */
    public static final RegistryEntry<Attribute, Attribute> CRITICAL_DAMAGE_REDUCING = register(
            "critical_damage_reducing", 0.0, -1024, 1024
    );

    // ========= 攻击 =========

    /** 暴击率。 */
    public static final RegistryEntry<Attribute, Attribute> CRITICAL_HIT = register(
            "critical_hit", 0.0, 0.0, 1.0
    );

    /** 暴击伤害百分比。 */
    public static final RegistryEntry<Attribute, Attribute> CRITICAL_DAMAGE = register(
            "critical_damage", 0.5, 0.0, 1024
    );

    // ========= 攻击 / 枪械 =========

    /** 伤害衰减起始距离。 */
    public static final RegistryEntry<Attribute, Attribute> DAMAGE_ATTENUATION_START_DISTANCE = register(
            "damage_attenuation_start_distance", 0.0, 0.0, 1024
    );
    /** 伤害衰减终止距离。 */
    public static final RegistryEntry<Attribute, Attribute> DAMAGE_ATTENUATION_END_DISTANCE = register(
            "damage_attenuation_end_distance", 0.0, 0.0, 1024
    );
    /** 射击速度（发/分钟）。 */
    public static final RegistryEntry<Attribute, Attribute> SHOOTING_SPEED = register(
            "shooting_speed", 0.0, 0.0, 1200
    );
    /** 散布率。 */
    public static final RegistryEntry<Attribute, Attribute> DISPERSION_RATE = register(
            "dispersion_rate", 0.0, 0.0, 1
    );
    /** 稳定性。 */
    public static final RegistryEntry<Attribute, Attribute> STABILITY = register(
            "stability", 0.0, 0.0, 1
    );
    /** 受击稳定性（乘数，默认 1）。 */
    public static final RegistryEntry<Attribute, Attribute> HURTING_STABILITY = register(
            "hurting_stability", 0.0, 0.0, 1
    );
    /** 放大倍率。 */
    public static final RegistryEntry<Attribute, Attribute> MAGNIFICATION = register(
            "magnification", 1.0, 0.0, 100.0
    );
    /** 准备时间。 */
    public static final RegistryEntry<Attribute, Attribute> PREPARING_TIME = register(
            "preparing_time", 0.0, 0.0, 1200
    );
    /** 瞄准时间。 */
    public static final RegistryEntry<Attribute, Attribute> AIMING_TIME = register(
            "aiming_time", 0.0, 0.0, 1200
    );
    /** 装填时间。 */
    public static final RegistryEntry<Attribute, Attribute> RELOADING_TIME = register(
            "reloading_time", 0.0, 0.0, 1200
    );
    /** 载弹量。 */
    public static final RegistryEntry<Attribute, Attribute> PAYLOAD_CAPACITY = register(
            "payload_capacity", 1.0, 1.0, 1024
    );
    /** 蓄力时间因子（乘数，默认 1）。 */
    public static final RegistryEntry<Attribute, Attribute> CHARGING_TIME_FACTOR = registerFactor(
            "charging_time_factor"
    );

    // ========= 攻击 / 伤害参数 =========

    /** 硬甲穿甲值。 */
    public static final RegistryEntry<Attribute, Attribute> HARD_ARMOR_PENETRATION_VALUE = register(
            "hard_armor_penetration_value", 0, 0.0, Double.MAX_VALUE
    );
    /** 软甲穿甲值。 */
    public static final RegistryEntry<Attribute, Attribute> SOFT_ARMOR_PENETRATION_VALUE = register(
            "soft_armor_penetration_value", 0, 0.0, Double.MAX_VALUE
    );

    // ========= 状态 =========

    /** 负面状态积累率因子（乘数，默认 1）。 */
    public static final RegistryEntry<Attribute, Attribute> DEBUFF_ACCUMULATION_RATE = registerFactor(
            "debuff_accumulation_rate"
    );
    /** 负面状态消退速度因子（乘数，默认 1）。 */
    public static final RegistryEntry<Attribute, Attribute> DEBUFF_FADING_SPEED = registerFactor(
            "debuff_fading_speed"
    );
    /** 负面状态强度因子（乘数，默认 1）。 */
    public static final RegistryEntry<Attribute, Attribute> DEBUFF_INTENSITY_FACTOR = registerFactor(
            "debuff_intensity_factor"
    );
    /** 负面状态时间因子（乘数，默认 1）。 */
    public static final RegistryEntry<Attribute, Attribute> DEBUFF_TIME_FACTOR = registerFactor(
            "debuff_time_factor"
    );
    /** 正面状态强度因子（乘数，默认 1）。 */
    public static final RegistryEntry<Attribute, Attribute> BUFF_INTENSITY_FACTOR = registerFactor(
            "buff_intensity_factor"
    );
    /** 正面状态时间因子（乘数，默认 1）。 */
    public static final RegistryEntry<Attribute, Attribute> BUFF_TIME_FACTOR = registerFactor(
            "buff_time_factor"
    );

    // ========= 恢复 =========

    /** 自然恢复。 */
    public static final RegistryEntry<Attribute, Attribute> NATURAL_RECOVERY = register(
            "natural_recovery", 0.0, 0.0, Float.MAX_VALUE
    );
    /** 恢复因子（乘数，默认 1）。 */
    public static final RegistryEntry<Attribute, Attribute> RECOVERY_FACTOR = registerFactor(
            "recovery_factor"
    );
    /** 物品使用速度因子（乘数，默认 1）。 */
    public static final RegistryEntry<Attribute, Attribute> ITEM_USING_SPEED_FACTOR = registerFactor(
            "item_using_speed_factor"
    );
    /** 法术护盾恢复。 */
    public static final RegistryEntry<Attribute, Attribute> MAGICAL_SHIELD_RECOVERY = register(
            "magical_shield_recovery", 0.0, 0.0, Float.MAX_VALUE
    );

    public static RegistryEntry<Attribute, Attribute> getSpellDamageAmplificationAttribute(Element element) {
        if (element == null || element == Element.NONE) {
            return null;
        }
        return SPELL_DAMAGE_AMPLIFICATION_BY_ELEMENT.get(element);
    }

    public static RegistryEntry<Attribute, Attribute> getSpellDamageResistanceAttribute(Element element) {
        if (element == null || element == Element.NONE) {
            return null;
        }
        return SPELL_DAMAGE_RESISTANCE_BY_ELEMENT.get(element);
    }

    // ========= 原版属性说明（不重复注册） =========
    // 硬质护甲（盔甲值） -> Attributes.ARMOR
    // 软质护甲（盔甲韧性） -> Attributes.ARMOR_TOUGHNESS

    private static RegistryEntry<Attribute, Attribute> register(
            String id,
            double defaultValue,
            double min,
            double max
    ) {
        return BreaRegistration.REGISTRATE.simple(
                id,
                Registries.ATTRIBUTE,
                () -> new RangedAttribute("attribute.breacore." + id, defaultValue, min, max)
                        .setSyncable(true)
        );
    }

    private static RegistryEntry<Attribute, Attribute> registerFactor(String id) {
        return register(id, 1.0, 0.0, 1024.0);
    }
}
