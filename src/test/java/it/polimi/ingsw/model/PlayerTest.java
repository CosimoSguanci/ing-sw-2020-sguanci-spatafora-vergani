package it.polimi.ingsw.model;


import it.polimi.ingsw.model.gods.Apollo;
import it.polimi.ingsw.model.gods.GodStrategy;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {

    @Test
    public void testGetSetColor() {
        System.out.println("testing getColor() and setColor()...");

        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match =new Match(playersNum);
        Player instanceTest = new Player("Roberto", new Model(match), match);
        PrintableColor colorTest = PrintableColor.GREEN;
        instanceTest.setColor(colorTest);
        assertEquals(PrintableColor.GREEN, instanceTest.getColor());
        System.out.println("Test successfully completed.");
    }

    @Test
    public void testGetGodChooser() {
        System.out.println("testing setAsGodChooser() and isGodChooser()...");

        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match =new Match(playersNum);
        Player instanceTest = new Player("Roberto", new Model(match), match);
        instanceTest.setAsGodChooser();
        assertTrue(instanceTest.isGodChooser());
        System.out.println("Test successfully completed.");
    }

    @Test
    public void testGetSetWorkerFirst()  {
        System.out.println("testing getWorkerFirst() and setWorkerFirst()...");

        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match =new Match(playersNum);
        Board board = match.getMatchBoard();
        Player instancePlayerTest = new Player("Roberto", new Model(match), match);
        Worker instanceWorkerTest = instancePlayerTest.getWorkerFirst();
        assertEquals(instanceWorkerTest, instancePlayerTest.getWorkerFirst());

        System.out.println("Test successfully completed.");
    }

    @Test
    public void testGetSetWorkerSecond()  {
        System.out.println("testing getWorkerSecond() and setWorkerSecond()...");

        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match =new Match(playersNum);
        Board board = match.getMatchBoard();
        Player instancePlayerTest = new Player("Roberto", new Model(match), match);
        Worker instanceWorkerTest = instancePlayerTest.getWorkerSecond();
        assertEquals(instanceWorkerTest, instancePlayerTest.getWorkerSecond());

        System.out.println("Test successfully completed.");
    }

    @Test
    public void testGetSetGodStrategy()  {
        System.out.println("testing getGod() and setGod()...");

        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match =new Match(playersNum);
        Player instancePlayerTest = new Player("Roberto", new Model(match), match);

        GodStrategy instanceGodTest = new Apollo();
        instancePlayerTest.setGodStrategy(instanceGodTest);
        assertEquals(instanceGodTest, instancePlayerTest.getGodStrategy());

        System.out.println("Test successfully completed.");
    }

}