package it.polimi.ingsw;

import static org.junit.Assert.*;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.gods.Apollo;
import it.polimi.ingsw.model.gods.strategies.GodStrategy;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    @Test
    public void testGetSetColor() throws Exception {
        System.out.println("testing getColor() and setColor()...");

        Match match = new Match(2);
        Player instanceTest = new Player("Roberto", "RobS", match);
        String colorTest = "green";
        instanceTest.setColor(colorTest);
        assertEquals("green", instanceTest.getColor());
        System.out.println("Test successfully completed.");
    }

    @Test
    public void testGetSetWorkerFirst() throws Exception {
        System.out.println("testing getWorkerFirst() and setWorkerFirst()...");

        Match match = new Match(2);
        Board board = new Board();
        Player instancePlayerTest = new Player("Roberto", "RobS", match);
        Worker instanceWorkerTest = new Worker(instancePlayerTest, board);
        instancePlayerTest.setWorkerFirst(instanceWorkerTest);
        assertEquals(instanceWorkerTest, instancePlayerTest.getWorkerFirst());

        System.out.println("Test successfully completed.");
    }

    @Test
    public void testGetSetWorkerSecond() throws Exception {
        System.out.println("testing getWorkerSecond() and setWorkerSecond()...");

        Match match = new Match(2);
        Board board = new Board();
        Player instancePlayerTest = new Player("Roberto", "RobS", match);
        Worker instanceWorkerTest = new Worker(instancePlayerTest, board);
        instancePlayerTest.setWorkerSecond(instanceWorkerTest);
        assertEquals(instanceWorkerTest, instancePlayerTest.getWorkerSecond());

        System.out.println("Test successfully completed.");
    }

    @Test
    public void testGetSetGod() throws Exception {
        System.out.println("testing getGod() and setGod()...");

        Match match = new Match(2);
        Apollo apollo = new Apollo();
        Player instancePlayerTest = new Player("Roberto", "RobS", match);
        God instanceGodTest = new God("Apollo", "God of Music", "Your Move: Your Worker may move into an opponent Worker's space by forcing their Worker to the space yours just vacated.", apollo);
        instancePlayerTest.setGod(instanceGodTest);
        assertEquals(instanceGodTest, instancePlayerTest.getGod());

        System.out.println("Test successfully completed.");
    }

}