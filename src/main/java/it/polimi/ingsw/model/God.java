package it.polimi.ingsw.model;

/** This class contains general information about a single God.
 *  It consists of attributes that describe God and its rule.
 *  */
public class God {
    public final String name;
    public final String godDescription;
    public final String ruleDescription;
    public final GodStrategy rule;

    /**
     * This is the builder of the class. When a God is created is known its name,
     * its description and a description of what he is in power of.
     */
    public God(String name, String godDescription, String ruleDescription, GodStrategy rule) {
        this.name = name;
        this.godDescription = godDescription;
        this.ruleDescription = ruleDescription;
        this.rule = rule;
    }

}
