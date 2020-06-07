package it.polimi.ingsw.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum PrintableColor {
    RED, GREEN, BLUE, YELLOW, PURPLE;

    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";

    static final String RED_ANSI = "\u001B[31m";
    static final String BLUE_ANSI = "\u001B[34m";
    static final String GREEN_ANSI = "\u001B[32m";
    static final String YELLOW_ANSI = "\u001B[33m";
    static final String PURPLE_ANSI = "\u001B[35m";


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
    public static String convertColorToAnsi(PrintableColor color) {
        switch (color) {
            case RED:
                return RED_ANSI;
            case BLUE:
                return BLUE_ANSI;
            case GREEN:
                return GREEN_ANSI;
            case YELLOW:
                return YELLOW_ANSI;
            default:
                return PURPLE_ANSI;
        }
    }

    public static Color convertToColor(PrintableColor color) {
        switch (color) {
            case RED:
                return Color.RED;
            case BLUE:
                return Color.BLUE;
            case GREEN:
                return Color.GREEN.darker().darker();
            case YELLOW:
                return Color.ORANGE;
            default:
                return Color.MAGENTA.darker().darker();
        }
    }
}
