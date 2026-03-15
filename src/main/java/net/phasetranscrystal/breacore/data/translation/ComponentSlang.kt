package net.phasetranscrystal.breacore.data.translation

import net.phasetranscrystal.breacore.api.lang.initialize
import net.phasetranscrystal.breacore.api.lang.toLiteralSupplier
import net.phasetranscrystal.breacore.api.lang.translatedTo
import net.phasetranscrystal.breacore.api.misc.AutoInitialize

object ComponentSlang : AutoInitialize<ComponentSlang>() {
    // ****** 量词 ****** //
    val Infinite = ("无限" translatedTo "Infinite").initialize()

    // ****** 符号 ****** //
    val right = "✔".toLiteralSupplier()
    val wrong = "✘".toLiteralSupplier()
}
