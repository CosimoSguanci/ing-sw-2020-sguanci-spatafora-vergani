package it.polimi.ingsw.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum PrintableColour {
    RED, GREEN, BLUE, YELLOW, PURPLE;

    public static final String RESET = "\u001B[0m";

    public static final String BOLD = "\u001B[1m";

    public static boolean isValidColor(String colour) {
        try {
            Enum.valueOf(PrintableColour.class, colour.toUpperCase());
            return true;
        } catch(IllegalArgumentException e) {
            return false;
        }
    }

    public static List<PrintableColour> getColorList() {
        ArrayList<PrintableColour> colors = new ArrayList<>();
        Collections.addAll(colors, PrintableColour.values());
        return colors;
    }
}
