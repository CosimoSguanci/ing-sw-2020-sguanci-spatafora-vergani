package it.polimi.ingsw.model;

import it.polimi.ingsw.model.gods.GodStrategy;


/**
 * This class contains general information about a single God.
 * It consists of attributes that describe God and its rule.
 *
 * @author Roberto Spatafora
 */

public class God  {
    public final String name;
    public final String godDescription;
    public final String ruleDescription;
    public final GodStrategy godStrategy;

    /**
     * This is the builder of the class. When a God is created is known its name,
     * its description and a description of what he is in power of.
     */
    public God(String name, String godDescription, String ruleDescription, GodStrategy godStrategy) {
        this.name = name;
        this.godDescription = godDescription;
        this.ruleDescription = ruleDescription;
        this.godStrategy = godStrategy;
    }

}
