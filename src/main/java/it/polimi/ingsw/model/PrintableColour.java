package it.polimi.ingsw.model;

public enum PrintableColour {
    RED("red"), GREEN("green"), BLUE("blue");
    public static final String RESET = "\u001B[0m";

    private String colour;

    PrintableColour(String colour) {
        this.colour = colour;
    }

    public String getColour() {
        return this.colour;
    }

    @Override
    public String toString() {
        return colour;
    }
}
