package it.polimi.ingsw.model;

import java.awt.*;
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

    /**
     * This method converts a color given as parameter according to enum
     * defined, and it is used to have the correct correspondence Ansi color.
     * @param color is the PrintableColor you want to calculate the Ansi code
     * @return the string of the respective Ansi color.
     */
    public static String convertColorToAnsi (PrintableColor color) {
        switch (color) {
            case RED:
                return "\u001B[31m";
            case BLUE:
                return "\u001B[34m";
            case GREEN:
                return "\u001B[32m";
            case YELLOW:
                return "\u001B[33m";
            case PURPLE:
                return "\u001B[35m";
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Color convertToColor (PrintableColor color) {
        switch (color) {
            case RED:
                return Color.RED;
            case BLUE:
                return Color.BLUE;
            case GREEN:
                return Color.GREEN.darker().darker();
            case YELLOW:
                return Color.ORANGE;
            case PURPLE:
                return Color.MAGENTA.darker().darker();
            default:
                throw new IllegalArgumentException();
        }
    }
}
