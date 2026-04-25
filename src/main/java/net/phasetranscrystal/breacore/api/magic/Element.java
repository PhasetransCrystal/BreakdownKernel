package net.phasetranscrystal.breacore.api.magic;

public enum Element {
    METAL("metal"),
    PLANT("plant"),
    WATER("water"),
    FIRE("fire"),
    GROUND("ground"),
    WIND("wind"),
    ICE("ice"),
    BLOOD("blood"),
    SILK("silk"),
    ELECTRICITY("electricity"),
    LIGHT("light"),
    DARK("dark"),
    ORI("ori"),
    NONE("none");

    public final String id;

    Element(String id) {
        this.id = id;
    }
}
