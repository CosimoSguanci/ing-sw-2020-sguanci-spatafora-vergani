package it.polimi.ingsw;

import static org.junit.Assert.*;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    @Test
    public void testGetSetColor() {
        System.out.println("testing getColor() and setColor()...");
        
        Player instanceTest = new Player("Roberto", "RobS");
        String colorTest = "green";
        instanceTest.setColor(colorTest);
        assertEquals("green", instanceTest.getColor());
        System.out.println("Test successfully completed.");
    }

    @Test
    public void testGetSetWorkerFirst() {
        System.out.println("testing getWorkerFirst() and setWorkerFirst()...");

        Player instancePlayerTest = new Player("Roberto", "RobS");
        Worker instanceWorkerTest = new Worker(instancePlayerTest);
        instancePlayerTest.setWorkerFirst(instanceWorkerTest);
        assertEquals(instanceWorkerTest, instancePlayerTest.getWorkerFirst());

        System.out.println("Test successfully completed.");
    }

    @Test
    public void testGetSetWorkerSecond() {
        System.out.println("testing getWorkerSecond() and setWorkerSecond()...");

        Player instancePlayerTest = new Player("Roberto", "RobS");
        Worker instanceWorkerTest = new Worker(instancePlayerTest);
        instancePlayerTest.setWorkerSecond(instanceWorkerTest);
        assertEquals(instanceWorkerTest, instancePlayerTest.getWorkerSecond());

        System.out.println("Test successfully completed.");
    }

    @Test
    public void testGetSetGod() {
        System.out.println("testing getGod() and setGod()...");

        Player instancePlayerTest = new Player("Roberto", "RobS");
        God instanceGodTest = new God("Apollo", "God of Music", "Your Move: Your Worker may move into an opponent Worker's space by forcing their Worker to the space yours just vacated.");
        instancePlayerTest.setGod(instanceGodTest);
        assertEquals(instanceGodTest, instancePlayerTest.getGod());

        System.out.println("Test successfully completed.");
    }

}