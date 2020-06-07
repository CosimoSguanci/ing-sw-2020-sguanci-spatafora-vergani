package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class PrintableColorTest {

    @Test
    public void isValidColorTest() {
        assertTrue(PrintableColor.isValidColor("red"));
        assertTrue(PrintableColor.isValidColor("BLUE"));
        assertTrue(PrintableColor.isValidColor("YelLow"));
        assertTrue(PrintableColor.isValidColor("GreeN"));
        assertTrue(PrintableColor.isValidColor("Purple"));
        assertFalse(PrintableColor.isValidColor("Redd"));
    }

    @Test
    public void convertColorToAnsiTest() {
        assertEquals(PrintableColor.RED_ANSI, PrintableColor.convertColorToAnsi(PrintableColor.RED));
        assertEquals(PrintableColor.GREEN_ANSI, PrintableColor.convertColorToAnsi(PrintableColor.GREEN));
        assertEquals(PrintableColor.BLUE_ANSI, PrintableColor.convertColorToAnsi(PrintableColor.BLUE));
        assertEquals(PrintableColor.PURPLE_ANSI, PrintableColor.convertColorToAnsi(PrintableColor.PURPLE));
        assertEquals(PrintableColor.YELLOW_ANSI, PrintableColor.convertColorToAnsi(PrintableColor.YELLOW));
    }

    @Test
    public void convertColorTest() {
        assertEquals(Color.RED, PrintableColor.convertToColor(PrintableColor.RED));
        assertEquals(Color.GREEN.darker().darker(), PrintableColor.convertToColor(PrintableColor.GREEN));
        assertEquals(Color.ORANGE, PrintableColor.convertToColor(PrintableColor.YELLOW));
        assertEquals(Color.BLUE, PrintableColor.convertToColor(PrintableColor.BLUE));
        assertEquals(Color.MAGENTA.darker().darker(), PrintableColor.convertToColor(PrintableColor.PURPLE));
    }
}
