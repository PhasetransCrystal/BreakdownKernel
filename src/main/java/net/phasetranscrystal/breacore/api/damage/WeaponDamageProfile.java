package net.phasetranscrystal.breacore.api.damage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.phasetranscrystal.breacore.api.magic.Element;

/**
 * 武器附带的伤害构建参数组件（可变）。
 */
@Getter
public final class WeaponDamageProfile {

    /** 默认：无元素，法术层命中率为 0，护甲两层作用比为 1，无敌帧为 0。 */
    public static final WeaponDamageProfile GEOGRAPHY_ONLY = new WeaponDamageProfile(Element.NONE, 0.0, 1.0, 1.0, 0);
    /** 预设：仅法术层命中（法术层命中率 1，其余两层作用比 0）。 */
    public static final WeaponDamageProfile MAGIC_ONLY = new WeaponDamageProfile(Element.ORI, 1.0, 0.0, 0.0, 0);
    /** 预设：三个命中/作用比全部为 0。 */
    public static final WeaponDamageProfile PENETRATE = new WeaponDamageProfile(Element.NONE, 0.0, 0.0, 0.0, 0);

    public static final Codec<WeaponDamageProfile> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Element.CODEC.optionalFieldOf("element", Element.NONE).forGetter(WeaponDamageProfile::getElement),
            Codec.DOUBLE.optionalFieldOf("spell_shield", 0.0).forGetter(WeaponDamageProfile::getSpellShieldHitRatio),
            Codec.DOUBLE.optionalFieldOf("hard_armor", 1.0).forGetter(WeaponDamageProfile::getHardArmorActionRatio),
            Codec.DOUBLE.optionalFieldOf("soft_armor", 1.0).forGetter(WeaponDamageProfile::getSoftArmorActionRatio),
            Codec.INT.optionalFieldOf("inv_ticks", 0).forGetter(WeaponDamageProfile::getInvulnerabilityTicks)
    ).apply(instance, WeaponDamageProfile::new));

    private Element element;
    private double spellShieldHitRatio;
    private double hardArmorActionRatio;
    private double softArmorActionRatio;
    private int invulnerabilityTicks;

    public WeaponDamageProfile(
            Element element,
            double spellShieldHitRatio,
            double hardArmorActionRatio,
            double softArmorActionRatio,
            int invulnerabilityTicks
    ) {
        this.element = element == null ? Element.NONE : element;
        this.spellShieldHitRatio = clampRatio(spellShieldHitRatio);
        this.hardArmorActionRatio = clampRatio(hardArmorActionRatio);
        this.softArmorActionRatio = clampRatio(softArmorActionRatio);
        this.invulnerabilityTicks = Math.max(0, invulnerabilityTicks);
    }

    public WeaponDamageProfile withElement(Element element) {
        return new WeaponDamageProfile(
                element,
                this.spellShieldHitRatio,
                this.hardArmorActionRatio,
                this.softArmorActionRatio,
                this.invulnerabilityTicks
        );
    }

    public void setElement(Element element) {
        this.element = element == null ? Element.NONE : element;
    }

    public void setSpellShieldHitRatio(double spellShieldHitRatio) {
        this.spellShieldHitRatio = clampRatio(spellShieldHitRatio);
    }

    public void setHardArmorActionRatio(double hardArmorActionRatio) {
        this.hardArmorActionRatio = clampRatio(hardArmorActionRatio);
    }

    public void setSoftArmorActionRatio(double softArmorActionRatio) {
        this.softArmorActionRatio = clampRatio(softArmorActionRatio);
    }

    public void setInvulnerabilityTicks(int invulnerabilityTicks) {
        this.invulnerabilityTicks = Math.max(0, invulnerabilityTicks);
    }

    private static double clampRatio(double ratio) {
        return Math.clamp(ratio, 0.0, 1.0);
    }
}
