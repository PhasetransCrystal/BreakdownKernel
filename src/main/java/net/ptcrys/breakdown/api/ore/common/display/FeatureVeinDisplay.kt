package net.ptcrys.breakdown.api.ore.common.display

import net.ptcrys.topo.api.ore.OreVein
import net.ptcrys.topo.api.ore.display.OreVeinDisplay

import com.lowdragmc.lowdraglib2.gui.ui.UIElement

object FeatureVeinDisplay : OreVeinDisplay.Strategy {
    override fun buildPreview(vein: OreVein): UIElement = OreVeinPanels.build(vein)
}
