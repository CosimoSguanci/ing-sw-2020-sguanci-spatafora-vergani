package it.polimi.ingsw.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum PrintableColor {
    RED, GREEN, BLUE, YELLOW, PURPLE;

    public static final String RESET = "\u001B[0m";

    public static final String BOLD = "\u001B[1m";

    public static boolean isValidColor(String color) {
        try {
            Enum.valueOf(PrintableColor.class, color.toUpperCase());
            return true;
        } catch(IllegalArgumentException e) {
            return false;
        }
    }

    public static List<PrintableColor> getColorList() {
        ArrayList<PrintableColor> colors = new ArrayList<>();
        Collections.addAll(colors, PrintableColor.values());
        return colors;
    }
}
