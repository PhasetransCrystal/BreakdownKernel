package net.ptcrys.breakdown.common.data.translation

import net.ptcrys.breakdown.api.lang.initialize
import net.ptcrys.breakdown.api.lang.toLiteralSupplier
import net.ptcrys.breakdown.api.lang.translatedTo
import net.ptcrys.breakdown.api.misc.AutoInitialize

object MachineSlang : AutoInitialize<MachineSlang>() {
    val Tier = { tier: String ->
        ("等级: " translatedTo "Tier: ") + (tier.toLiteralSupplier()).green().bold()
    }.initialize()
}
