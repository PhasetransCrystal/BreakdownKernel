package net.ptcrys.breakdown.common.machine;

import net.ptcrys.breakdown.api.blockentity.IMachineBlockEntity;
import net.ptcrys.breakdown.api.machine.MetaMachine;
import net.ptcrys.breakdown.api.machine.feature.ISimpleUIMachine;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;

public class EmptyMachine extends MetaMachine implements ISimpleUIMachine {

    public EmptyMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public ModularUI createUI(BlockUIMenuType.BlockUIHolder holder) {
        return ModularUI.of(UI.empty(), holder.player);
    }
}
