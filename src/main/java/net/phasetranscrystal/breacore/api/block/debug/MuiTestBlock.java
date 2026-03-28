package net.phasetranscrystal.breacore.api.block.debug;

import net.minecraft.world.level.block.Block;

import com.lowdragmc.lowdraglib2.gui.holder.IModularUIHolder;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import org.jetbrains.annotations.Nullable;

public class MuiTestBlock extends Block implements IModularUIHolder {

    public MuiTestBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public @Nullable ModularUI getModularUI() {
        return null;
    }
}
