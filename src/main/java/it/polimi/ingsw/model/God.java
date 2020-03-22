package it.polimi.ingsw.model;

public class God {
    public final String name;
    public final String godDescription;
    public final String ruleDescription;
    public final GodStrategy rule;

    public God(String name, String godDescription, String ruleDescription) {
        this.name = name;
        this.godDescription = godDescription;
        this.ruleDescription = ruleDescription;
    }

}
