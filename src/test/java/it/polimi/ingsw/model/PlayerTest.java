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
        Match match = Match.getInstance(key, playersNum);
        Player instanceTest = new Player("Roberto", "RobS", match);
        String colorTest = "green";
        instanceTest.setColor(colorTest);
        assertEquals("green", instanceTest.getColor());
        System.out.println("Test successfully completed.");
    }

    @Test
    public void testGetGodChooser() {
        System.out.println("testing setAsGodChooser() and isGodChooser()...");

        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Player instanceTest = new Player("Roberto", "RobS", match);
        instanceTest.setAsGodChooser();
        assertTrue(instanceTest.isGodChooser());
        System.out.println("Test successfully completed.");
    }

    @Test
    public void testGetSetWorkerFirst()  {
        System.out.println("testing getWorkerFirst() and setWorkerFirst()...");

        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Board board = Board.getInstance(key);;
        Player instancePlayerTest = new Player("Roberto", "RobS", match);
        Worker instanceWorkerTest = new Worker(instancePlayerTest, board);
        instancePlayerTest.setWorkerFirst(instanceWorkerTest);
        assertEquals(instanceWorkerTest, instancePlayerTest.getWorkerFirst());

        System.out.println("Test successfully completed.");
    }

    @Test
    public void testGetSetWorkerSecond()  {
        System.out.println("testing getWorkerSecond() and setWorkerSecond()...");

        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Board board = Board.getInstance(key);;
        Player instancePlayerTest = new Player("Roberto", "RobS", match);
        Worker instanceWorkerTest = new Worker(instancePlayerTest, board);
        instancePlayerTest.setWorkerSecond(instanceWorkerTest);
        assertEquals(instanceWorkerTest, instancePlayerTest.getWorkerSecond());

        System.out.println("Test successfully completed.");
    }

    @Test
    public void testGetSetGodStrategy()  {
        System.out.println("testing getGod() and setGod()...");

        int playersNum = 2;
        String key = UUID.randomUUID().toString();
        Match match = Match.getInstance(key, playersNum);
        Apollo apollo = new Apollo();
        Player instancePlayerTest = new Player("Roberto", "RobS", match);
        /*God instanceGodTest = new God("Apollo", "God of Music", "Your Move: Your Worker may move into an opponent Worker's space by forcing their Worker to the space yours just vacated.", apollo);
        instancePlayerTest.setGod(instanceGodTest);
        assertEquals(instanceGodTest, instancePlayerTest.getGod());*/

        GodStrategy instanceGodTest = new Apollo();
        instancePlayerTest.setGodStrategy(instanceGodTest);
        assertEquals(instanceGodTest, instancePlayerTest.getGodStrategy());

        System.out.println("Test successfully completed.");
    }

}