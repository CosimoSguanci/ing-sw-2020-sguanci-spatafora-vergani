package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.exceptions.UnknownGodException;
import it.polimi.ingsw.model.gods.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Steps to add a new God to the game:
 * 1) Add class and relative implementation in model.gods package
 * 2) Add INFO + Factory Instantiation here
 * DONE
 */
public final class GodsUtils {

    private GodsUtils(){}

    public static final String GOD_NAME = "god_name";
    public static final String GOD_DESCRIPTION = "god_description";
    public static final String POWER_DESCRIPTION = "power_description";

    private static final Map<String, Map<String, String>> godsInfo;
    private static final Map<String, Supplier<GodStrategy>> godsFactoryMap;

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
    }


    public static boolean isValidGod(String godName) {
        godName = godName.toLowerCase();
        return godsInfo.containsKey(godName);
    }

    public static Map<String, String> parseGodName(String godName) throws UnknownGodException {
        godName = godName.toLowerCase();
        if(godsInfo.containsKey(godName)) {
            return godsInfo.get(godName);
        }

        throw new UnknownGodException();
    }

    public static GodStrategy godsFactory(String godName) throws UnknownGodException {
        godName = godName.toLowerCase();
        if(godsFactoryMap.containsKey(godName)) {
            return godsFactoryMap.get(godName).get();
        }

        throw new UnknownGodException();
    }

    public static Map<String, Map<String, String>> getGodsInfo() {
        return godsInfo;
    }
}
