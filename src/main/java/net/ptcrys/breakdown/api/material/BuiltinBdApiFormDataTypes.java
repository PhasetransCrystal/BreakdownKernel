package net.ptcrys.breakdown.api.material;

import net.ptcrys.breakdown.api.BreakdownApiPlugin;
import net.ptcrys.breakdown.api.material.common.form.FormAmountDataType;
import net.ptcrys.topo.api.api.builtin.MaterialDomainRegistration;

/** 瓦解核心内置表单载荷类型(骨架):形态声明可携带的类型化参数全集。 */
public final class BuiltinBdApiFormDataTypes {

    private static final MaterialDomainRegistration MATERIALS = BreakdownApiPlugin.INSTANCE.material();

    /** 单件物品的材料数量,以每锭 72 单位计;仅直接单位换算形态声明。 */
    public static final FormAmountDataType AMOUNT = MATERIALS.formDataType(
            "amount", new FormAmountDataType(MATERIALS.id("amount")));

    private BuiltinBdApiFormDataTypes() {}

    public static void init() {}
}
