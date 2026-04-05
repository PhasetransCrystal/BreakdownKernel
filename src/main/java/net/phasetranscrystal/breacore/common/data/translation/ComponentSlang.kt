package net.phasetranscrystal.breacore.common.data.translation

import net.phasetranscrystal.breacore.api.lang.ComponentSupplier
import net.phasetranscrystal.breacore.api.lang.initialize
import net.phasetranscrystal.breacore.api.lang.toLiteralSupplier
import net.phasetranscrystal.breacore.api.lang.translatedTo
import net.phasetranscrystal.breacore.api.misc.AutoInitialize

object ComponentSlang : AutoInitialize<ComponentSlang>() {
    // ****** 量词 ****** //
    val Infinite = ("无限" translatedTo "Infinite").rainbow().initialize()

    // ****** 符号 ****** //
    val right = "✔".toLiteralSupplier()
    val wrong = "✘".toLiteralSupplier()

    // ****** 格式 ****** //
    val Bar = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}- ".toLiteralSupplier().gold() }.initialize()
    val Plus = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}+ ".toLiteralSupplier().gold() }.initialize()
    val Asterisk = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}* ".toLiteralSupplier().gold() }.initialize()
    val Tab = { tab: Int -> "  ".repeat(tab).toLiteralSupplier() }.initialize()
    val Star = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}⭐ ".toLiteralSupplier().gold() }.initialize()
    val Circle = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}৹ ".toLiteralSupplier().gold() }.initialize()
    val Warning =
        { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}⚠ ".toLiteralSupplier().red().bold() }.initialize()
    val OutTopic =
        { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}# ".toLiteralSupplier().gray() }.initialize()
    val Right = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}✔ ".toLiteralSupplier().gold() }.initialize()
    val Wrong = { tab: Int -> "${"  ".repeat((tab - 1).coerceAtLeast(0))}✘ ".toLiteralSupplier().red() }.initialize()
    val LegendSignalWrapper = { other: ComponentSupplier ->
        "111".toLiteralSupplier().obfuscated().scrollFullColor()
            .underline() + " ".toLiteralSupplier() + other + " ".toLiteralSupplier() + "111".toLiteralSupplier()
            .obfuscated().scrollFullColor().underline()
    }

    // ****** 单位 ****** //
    val Temperature =
        { temp: Int -> ("温度: " translatedTo "Temperature: ") + (temp.toLiteralSupplier()).gold().bold() }.initialize()
    val Capacity = { capacity: String ->
        ("容量: " translatedTo "Capacity: ") + (capacity.toLiteralSupplier()).white().bold()
    }.initialize()

    // ****** 常用话术 ****** //
    val RecommendedToUse =
        { other: ComponentSupplier -> (("推荐使用" translatedTo "Recommended to use ") + other.gold()).aqua() }.initialize()
    val RecommendedUseAs =
        { other: ComponentSupplier -> (("推荐用于" translatedTo "Recommended use it to ") + other.gold()).aqua() }.initialize()
}
