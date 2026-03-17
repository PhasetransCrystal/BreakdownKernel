package net.phasetranscrystal.breacore.api.addon;

import net.phasetranscrystal.breacore.api.registry.registrate.BreaRegistrate;

@SuppressWarnings("unused")
public interface IBreaAddon {

    /**
     * @return this addon's BreaRegistrate instance.
     */
    BreaRegistrate getRegistrate();

    /**
     * This runs after BreakdownCore has set up it's content. Set up BreakdownCore loading-dependent (but NOT ones
     * dependent on
     *
     * @apiNote DO NOT REGISTER ANY OF YOUR OWN CONTENT HERE, AS IF YOU DO, IT'LL REGISTER AS IF BreakdownCore
     *          REGISTERED IT
     *          AND YOUR DATAGEN AND EVENTS WILL <b><i>NOT</i></b> WORK AS EXPECTED, IF AT ALL.
     */
    void initComplete();

    void addElement();

    void addMaterial();
}
