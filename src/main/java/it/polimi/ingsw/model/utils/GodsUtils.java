package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.exceptions.UnknownGodException;
import it.polimi.ingsw.model.gods.*;
import it.polimi.ingsw.view.gui.gods.AtlasGuiStrategy;
import it.polimi.ingsw.view.gui.gods.GodGuiDrawer;
import it.polimi.ingsw.view.gui.gods.GodsGuiStrategy;
import it.polimi.ingsw.view.gui.gods.ZeusGuiStrategy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Utility class which contains information about Gods and some static methods to handle them (for example, the God Factory Method).
 * <p>
 * Regarding the Gods implementation scalability, the necessary steps to add a new God to the game are:
 * 1) Add class and relative implementation in model.gods package
 * 2) Add INFO + Factory + GUI Factory Instantiation here
 */
public final class GodsUtils {

    private GodsUtils() {
    }

    /**
     * Keys to be used to retrieve Gods Info from maps returned by this class
     */
    public static final String GOD_NAME = "god_name";
    public static final String GOD_DESCRIPTION = "god_description";
    public static final String POWER_DESCRIPTION = "power_description";

    /**
     * Map representing the God Info (name, description, power description)
     */
    private static final Map<String, Map<String, String>> godsInfo;

    /**
     * Map representing an association between god name and a Supplier.
     * Thanks to the supplier, every time the god factory is requested, a new GodStrategy instance is created and returned.
     */
    private static final Map<String, Supplier<GodStrategy>> godsFactoryMap;

    /**
     * Map representing an association between god name and a Supplier.
     * Thanks to the supplier, every time the god factory is requested, a new GodGuiStrategy instance is created and returned.
     */
    private static final Map<String, Supplier<GodGuiDrawer>> godsGuiFactoryMap;


    static {
        /*
         * Gods info static initializations
         */
        Map<String, Map<String, String>> tmpGodsInfo = new HashMap<>();

        Map<String, String> godInfo;

        godInfo = new HashMap<>();
        godInfo.put(GOD_NAME, Apollo.NAME);
        godInfo.put(GOD_DESCRIPTION, Apollo.DESCRIPTION);
        godInfo.put(POWER_DESCRIPTION, Apollo.POWER_DESCRIPTION);
        tmpGodsInfo.put(Apollo.NAME.toLowerCase(), godInfo);

        godInfo = new HashMap<>();
        godInfo.put(GOD_NAME, Artemis.NAME);
        godInfo.put(GOD_DESCRIPTION, Artemis.DESCRIPTION);
        godInfo.put(POWER_DESCRIPTION, Artemis.POWER_DESCRIPTION);
        tmpGodsInfo.put(Artemis.NAME.toLowerCase(), godInfo);

        godInfo = new HashMap<>();
        godInfo.put(GOD_NAME, Athena.NAME);
        godInfo.put(GOD_DESCRIPTION, Athena.DESCRIPTION);
        godInfo.put(POWER_DESCRIPTION, Athena.POWER_DESCRIPTION);
        tmpGodsInfo.put(Athena.NAME.toLowerCase(), godInfo);

        godInfo = new HashMap<>();
        godInfo.put(GOD_NAME, Atlas.NAME);
        godInfo.put(GOD_DESCRIPTION, Atlas.DESCRIPTION);
        godInfo.put(POWER_DESCRIPTION, Atlas.POWER_DESCRIPTION);
        tmpGodsInfo.put(Atlas.NAME.toLowerCase(), godInfo);

        godInfo = new HashMap<>();
        godInfo.put(GOD_NAME, Demeter.NAME);
        godInfo.put(GOD_DESCRIPTION, Demeter.DESCRIPTION);
        godInfo.put(POWER_DESCRIPTION, Demeter.POWER_DESCRIPTION);
        tmpGodsInfo.put(Demeter.NAME.toLowerCase(), godInfo);

        godInfo = new HashMap<>();
        godInfo.put(GOD_NAME, Eros.NAME);
        godInfo.put(GOD_DESCRIPTION, Eros.DESCRIPTION);
        godInfo.put(POWER_DESCRIPTION, Eros.POWER_DESCRIPTION);
        tmpGodsInfo.put(Eros.NAME.toLowerCase(), godInfo);

        godInfo = new HashMap<>();
        godInfo.put(GOD_NAME, Hephaestus.NAME);
        godInfo.put(GOD_DESCRIPTION, Hephaestus.DESCRIPTION);
        godInfo.put(POWER_DESCRIPTION, Hephaestus.POWER_DESCRIPTION);
        tmpGodsInfo.put(Hephaestus.NAME.toLowerCase(), godInfo);

        godInfo = new HashMap<>();
        godInfo.put(GOD_NAME, Hera.NAME);
        godInfo.put(GOD_DESCRIPTION, Hera.DESCRIPTION);
        godInfo.put(POWER_DESCRIPTION, Hera.POWER_DESCRIPTION);
        tmpGodsInfo.put(Hera.NAME.toLowerCase(), godInfo);

        godInfo = new HashMap<>();
        godInfo.put(GOD_NAME, Hestia.NAME);
        godInfo.put(GOD_DESCRIPTION, Hestia.DESCRIPTION);
        godInfo.put(POWER_DESCRIPTION, Hestia.POWER_DESCRIPTION);
        tmpGodsInfo.put(Hestia.NAME.toLowerCase(), godInfo);

        godInfo = new HashMap<>();
        godInfo.put(GOD_NAME, Minotaur.NAME);
        godInfo.put(GOD_DESCRIPTION, Minotaur.DESCRIPTION);
        godInfo.put(POWER_DESCRIPTION, Minotaur.POWER_DESCRIPTION);
        tmpGodsInfo.put(Minotaur.NAME.toLowerCase(), godInfo);

        godInfo = new HashMap<>();
        godInfo.put(GOD_NAME, Pan.NAME);
        godInfo.put(GOD_DESCRIPTION, Pan.DESCRIPTION);
        godInfo.put(POWER_DESCRIPTION, Pan.POWER_DESCRIPTION);
        tmpGodsInfo.put(Pan.NAME.toLowerCase(), godInfo);

        godInfo = new HashMap<>();
        godInfo.put(GOD_NAME, Poseidon.NAME);
        godInfo.put(GOD_DESCRIPTION, Poseidon.DESCRIPTION);
        godInfo.put(POWER_DESCRIPTION, Poseidon.POWER_DESCRIPTION);
        tmpGodsInfo.put(Poseidon.NAME.toLowerCase(), godInfo);

        godInfo = new HashMap<>();
        godInfo.put(GOD_NAME, Prometheus.NAME);
        godInfo.put(GOD_DESCRIPTION, Prometheus.DESCRIPTION);
        godInfo.put(POWER_DESCRIPTION, Prometheus.POWER_DESCRIPTION);
        tmpGodsInfo.put(Prometheus.NAME.toLowerCase(), godInfo);

        godInfo = new HashMap<>();
        godInfo.put(GOD_NAME, Zeus.NAME);
        godInfo.put(GOD_DESCRIPTION, Zeus.DESCRIPTION);
        godInfo.put(POWER_DESCRIPTION, Zeus.POWER_DESCRIPTION);
        tmpGodsInfo.put(Zeus.NAME.toLowerCase(), godInfo);

        godsInfo = Collections.unmodifiableMap(tmpGodsInfo);

        /*
         * Gods factory static initializations
         */
        Map<String, Supplier<GodStrategy>> tmpGodsFactoryMap = new HashMap<>();

        tmpGodsFactoryMap.put(Apollo.NAME.toLowerCase(), Apollo::new);
        tmpGodsFactoryMap.put(Artemis.NAME.toLowerCase(), Artemis::new);
        tmpGodsFactoryMap.put(Athena.NAME.toLowerCase(), Athena::new);
        tmpGodsFactoryMap.put(Atlas.NAME.toLowerCase(), Atlas::new);
        tmpGodsFactoryMap.put(Demeter.NAME.toLowerCase(), Demeter::new);
        tmpGodsFactoryMap.put(Eros.NAME.toLowerCase(), Eros::new);
        tmpGodsFactoryMap.put(Hephaestus.NAME.toLowerCase(), Hephaestus::new);
        tmpGodsFactoryMap.put(Hera.NAME.toLowerCase(), Hera::new);
        tmpGodsFactoryMap.put(Hestia.NAME.toLowerCase(), Hestia::new);
        tmpGodsFactoryMap.put(Minotaur.NAME.toLowerCase(), Minotaur::new);
        tmpGodsFactoryMap.put(Pan.NAME.toLowerCase(), Pan::new);
        tmpGodsFactoryMap.put(Poseidon.NAME.toLowerCase(), Poseidon::new);
        tmpGodsFactoryMap.put(Prometheus.NAME.toLowerCase(), Prometheus::new);
        tmpGodsFactoryMap.put(Zeus.NAME.toLowerCase(), Zeus::new);


        godsFactoryMap = Collections.unmodifiableMap(tmpGodsFactoryMap);

        /*
         * Gods GUI factory static initializations
         */
        Map<String, Supplier<GodGuiDrawer>> tmpGodsGuiFactoryMap = new HashMap<>();

        tmpGodsGuiFactoryMap.put(Apollo.NAME.toLowerCase(), GodsGuiStrategy::new);
        tmpGodsGuiFactoryMap.put(Artemis.NAME.toLowerCase(), GodsGuiStrategy::new);
        tmpGodsGuiFactoryMap.put(Athena.NAME.toLowerCase(), GodsGuiStrategy::new);
        tmpGodsGuiFactoryMap.put(Demeter.NAME.toLowerCase(), GodsGuiStrategy::new);
        tmpGodsGuiFactoryMap.put(Eros.NAME.toLowerCase(), GodsGuiStrategy::new);
        tmpGodsGuiFactoryMap.put(Hephaestus.NAME.toLowerCase(), GodsGuiStrategy::new);
        tmpGodsGuiFactoryMap.put(Hera.NAME.toLowerCase(), GodsGuiStrategy::new);
        tmpGodsGuiFactoryMap.put(Hestia.NAME.toLowerCase(), GodsGuiStrategy::new);
        tmpGodsGuiFactoryMap.put(Minotaur.NAME.toLowerCase(), GodsGuiStrategy::new);
        tmpGodsGuiFactoryMap.put(Pan.NAME.toLowerCase(), GodsGuiStrategy::new);
        tmpGodsGuiFactoryMap.put(Poseidon.NAME.toLowerCase(), GodsGuiStrategy::new);
        tmpGodsGuiFactoryMap.put(Prometheus.NAME.toLowerCase(), GodsGuiStrategy::new);
        tmpGodsGuiFactoryMap.put(Atlas.NAME.toLowerCase(), AtlasGuiStrategy::new);
        tmpGodsGuiFactoryMap.put(Zeus.NAME.toLowerCase(), ZeusGuiStrategy::new);

        godsGuiFactoryMap = Collections.unmodifiableMap(tmpGodsGuiFactoryMap);
    }

    /**
     * Utility method used to check if the name typed by the Player is a valid god or not.
     *
     * @param godName the name that has to be tested
     * @return true if parameter is a valid god name, false otherwise
     */
    public static boolean isValidGod(String godName) {
        godName = godName.toLowerCase();
        return godsInfo.containsKey(godName);
    }

    /**
     * Utility method used to get Gods Info from god name (useful when a Player requests info about a god)
     *
     * @param godName the name of the God whose info are requested
     * @return the map representing the specific God Info (name, description, power description)
     * @throws UnknownGodException if the parameter is not a valid god name
     */
    public static Map<String, String> parseGodName(String godName) throws UnknownGodException {
        godName = godName.toLowerCase();
        if (godsInfo.containsKey(godName)) {
            return godsInfo.get(godName);
        }
        throw new UnknownGodException();
    }

    /**
     * Factory method used to create new {@link GodStrategy} instances
     *
     * @param godName the name of the God that have to be instantiated
     * @return the requested brand new GodStrategy instance
     * @throws UnknownGodException if the parameter is not a valid god name
     */
    public static GodStrategy godsFactory(String godName) throws UnknownGodException {
        godName = godName.toLowerCase();
        if (godsFactoryMap.containsKey(godName)) {
            return godsFactoryMap.get(godName).get();
        }

        throw new UnknownGodException();
    }

    /**
     * Global Gods Info getter
     *
     * @return the info of all Gods
     */
    public static Map<String, Map<String, String>> getGodsInfo() {
        return godsInfo;
    }

    /**
     * Factory method used to create new {@link GodGuiDrawer} instances
     *
     * @param godName the name of the God that have to be instantiated
     * @return the requested brand new GodGuiDrawer instance
     * @throws UnknownGodException if the parameter is not a valid god name
     */
    public static GodGuiDrawer godsGuiFactory(String godName) throws UnknownGodException {
        godName = godName.toLowerCase();
        if (godsFactoryMap.containsKey(godName)) {
            return godsGuiFactoryMap.get(godName).get();
        }

        throw new UnknownGodException();
    }

}
