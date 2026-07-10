package net.phasetranscrystal.breacore.common.machine;

import net.phasetranscrystal.breacore.api.blockentity.IMachineBlockEntity;
import net.phasetranscrystal.breacore.api.machine.MetaMachine;
import net.phasetranscrystal.breacore.api.machine.feature.IUIMachine;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;

public class EmptyMachine extends MetaMachine implements IUIMachine {

    public EmptyMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public ModularUI createUI(BlockUIMenuType.BlockUIHolder holder) {
        return null;
    }
}
