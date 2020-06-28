package it.polimi.ingsw.model;


import it.polimi.ingsw.model.gods.Apollo;
import it.polimi.ingsw.model.gods.GodStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {

    private Player player;

    @BeforeEach
    public void initPlayerProperties() {
        int playersNum = 2;
        Match match = new Match(playersNum);
        this.player = new Player("Roberto", new Model(match), match);
    }

    @Test
    public void testGetSetColor() {
        System.out.println("testing getColor() and setColor()...");

        PrintableColor colorTest = PrintableColor.GREEN;
        player.setColor(colorTest);
        assertEquals(PrintableColor.GREEN, player.getColor());
        System.out.println("Test successfully completed.");
    }

    @Test
    public void testGetGodChooser() {
        System.out.println("testing setAsGodChooser() and isGodChooser()...");

        player.setAsGodChooser();
        assertTrue(player.isGodChooser());
        System.out.println("Test successfully completed.");
    }

    @Test
    public void testGetSetWorkerFirst() {
        System.out.println("testing getWorkerFirst() and setWorkerFirst()...");

        Worker instanceWorkerTest = player.getWorkerFirst();
        assertEquals(instanceWorkerTest, player.getWorkerFirst());

        System.out.println("Test successfully completed.");
    }

    @Test
    public void testGetSetWorkerSecond() {
        System.out.println("testing getWorkerSecond() and setWorkerSecond()...");

        Worker instanceWorkerTest = player.getWorkerSecond();
        assertEquals(instanceWorkerTest, player.getWorkerSecond());

        System.out.println("Test successfully completed.");
    }

    @Test
    public void testGetSetGodStrategy() {
        System.out.println("testing getGod() and setGod()...");

        GodStrategy instanceGodTest = new Apollo();
        player.setGodStrategy(instanceGodTest);
        assertEquals(instanceGodTest, player.getGodStrategy());

        System.out.println("Test successfully completed.");
    }

}