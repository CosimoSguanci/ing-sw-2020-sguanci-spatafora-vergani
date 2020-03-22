package it.polimi.ingsw;

import static org.junit.Assert.*;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import org.junit.jupiter.api.Test;

public class WorkerTest {

    @Test
    public void testGetPosition() {
        System.out.println("testing getPosition() and setInitialPosition()...");

        Player instancePlayerTest = new Player("Roberto", "RobS");
        Worker instanceWorkerTest = new Worker(instancePlayerTest);
        Cell instanceInitialCellPosition = new Cell();
        instanceWorkerTest.setInitialPosition(instanceInitialCellPosition);
        assertEquals(instanceInitialCellPosition, instanceWorkerTest.getPosition());

        System.out.println("Test successfully completed.");
    }

    @Test
    public void testMove() {

    }

    @Test
    public void testBuild() {

    }

}
