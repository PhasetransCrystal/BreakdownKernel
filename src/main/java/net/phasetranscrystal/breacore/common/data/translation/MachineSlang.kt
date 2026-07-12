package net.phasetranscrystal.breacore.common.data.translation

import net.phasetranscrystal.breacore.api.lang.initialize
import net.phasetranscrystal.breacore.api.lang.toLiteralSupplier
import net.phasetranscrystal.breacore.api.lang.translatedTo
import net.phasetranscrystal.breacore.api.misc.AutoInitialize

object MachineSlang : AutoInitialize<MachineSlang>() {
    val Tier = { tier: String ->
        ("等级: " translatedTo "Tier: ") + (tier.toLiteralSupplier()).green().bold()
    }.initialize()
}
