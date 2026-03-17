package net.phasetranscrystal.breacore.api.material.info;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MaterialIconSet {

    public static final Map<String, MaterialIconSet> ICON_SETS = new HashMap<>();
    public static final MaterialIconSet DEFAULT = new MaterialIconSet("default");
    public static final MaterialIconSet FLUID = new MaterialIconSet("fluid", DEFAULT);

    private static int idCounter = 0;
    public final String name;
    public final int id;
    public final boolean isRootIconset;

    /**
     * 父图标集。如果{@link MaterialIconSet#isRootIconset}为true，则可以为null，
     * 否则必须为非null。
     */
    public final MaterialIconSet parentIconset;

    public MaterialIconSet(String name) {
        this(name, MaterialIconSet.DEFAULT);
    }

    public MaterialIconSet(String name, MaterialIconSet parentIconset) {
        this(name, parentIconset, false);
    }

    private MaterialIconSet(String name, MaterialIconSet parentIconset, boolean isRootIconset) {
        this.name = name.toLowerCase(Locale.ENGLISH);
        Preconditions.checkArgument(!ICON_SETS.containsKey(this.name),
                "MaterialIconSet " + this.name + " 已注册！");
        this.id = idCounter++;
        this.isRootIconset = isRootIconset;
        this.parentIconset = parentIconset;
        ICON_SETS.put(name, this);
    }

    public static MaterialIconSet getByName(@NotNull String name) {
        return ICON_SETS.get(name.toLowerCase(Locale.ENGLISH));
    }

    @Override
    public String toString() {
        return name;
    }
}
