package it.polimi.ingsw;

import static org.junit.Assert.*;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;


public class WorkerTest {

    @Test
    public void testGetPosition() throws Exception {

        System.out.println("testing getPosition() and setInitialPosition()...");

        Match testMatch = new Match(2);
        Board testBoard = new Board();
        Player testPlayer = new Player("Roberto", "RobS", testMatch);
        Worker testWorker = new Worker(testPlayer, testBoard);

        testWorker.setInitialPosition(0,0);
        assertEquals(testBoard.getCell(0,0), testWorker.getPosition());

        System.out.println("Test successfully completed.");
    }

    @Test
    public void testMove() throws Exception {

        Match testMatch = new Match(2);
        Board testBoard = new Board();
        Player testPlayer = new Player("Roberto", "RobS", testMatch);
        Worker testWorker = new Worker(testPlayer, testBoard);

        System.out.println("testing move() with a valid movement...");

        testWorker.setInitialPosition(0,0);
        testWorker.move(testBoard.getCell(0,1));

        assertEquals(testBoard.getCell(0,1), testWorker.getPosition());
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testCellNotAdjacentMove() throws Exception {

        Match testMatch = new Match(2);
        Board testBoard = new Board();
        Player testPlayer = new Player("Roberto", "RobS", testMatch);
        Worker testWorker = new Worker(testPlayer, testBoard);

        System.out.println("testing move() with an invalid movement: an adjacent cell must be requested for movement...");

        testWorker.setInitialPosition(0,0);

        Exception exception = assertThrows(Exception.class, () -> {testWorker.move(testBoard.getCell(0,2));});
        System.out.println("Test successfully completed.");
    }

    @Test
    public void testCellNotEmptyMove() throws Exception {

        Match testMatch = new Match(2);
        Board testBoard = new Board();
        Player testPlayer = new Player("Roberto", "RobS", testMatch);
        Worker testWorker = new Worker(testPlayer, testBoard);
        Worker testOccupantWorker = new Worker(testPlayer, testBoard);

        System.out.println("testing move() with an invalid movement: an empty cell must be requested for movement...");

        testWorker.setInitialPosition(0,0);
        testOccupantWorker.setInitialPosition(0,1);

        Exception exception = assertThrows(Exception.class, () -> {testWorker.move(testBoard.getCell(0,1));});
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testDomeMove() throws Exception {

        Match testMatch = new Match(2);
        Board testBoard = new Board();
        Player testPlayer = new Player("Roberto", "RobS", testMatch);
        Worker testWorker = new Worker(testPlayer, testBoard);

        System.out.println("testing move() with an invalid movement: workers can't move in a DOME...");

        testWorker.setInitialPosition(0,0);
        testBoard.getCell(0,1).setLevel(BlockType.DOME);

        Exception exception = assertThrows(Exception.class, () -> {testWorker.move(testBoard.getCell(0,1));});
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testCannotMove() throws Exception {

        Match testMatch = new Match(2);
        Board testBoard = new Board();
        Player testPlayer = new Player("Roberto", "RobS", testMatch);
        Worker testWorker = new Worker(testPlayer, testBoard);

        System.out.println("testing move() with an invalid movement: workers can't move up...");

        testWorker.setInitialPosition(0,0);
        testBoard.getCell(0,1).setLevel(BlockType.LEVEL_ONE);
        testPlayer.match.setCanMove(false);

        Exception exception = assertThrows(Exception.class, () -> {testWorker.move(testBoard.getCell(0,1));});
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testBuild() throws Exception {

        Match testMatch = new Match(2);
        Board testBoard = new Board();
        Player testPlayer = new Player("Roberto", "RobS", testMatch);
        Worker testWorker = new Worker(testPlayer, testBoard);

        System.out.println("testing build() with a valid request...");

        testWorker.setInitialPosition(0,0);
        testWorker.build(testBoard.getCell(0,1));
        assertEquals(BlockType.LEVEL_ONE , testBoard.getCell(0,1).getLevel());

        testWorker.build(testBoard.getCell(0,1));
        assertEquals(BlockType.LEVEL_TWO, testBoard.getCell(0,1).getLevel());

        testWorker.build(testBoard.getCell(0,1));
        assertEquals(BlockType.LEVEL_THREE, testBoard.getCell(0,1).getLevel());

        testWorker.build(testBoard.getCell(0,1));
        assertEquals(BlockType.DOME, testBoard.getCell(0,1).getLevel());

        System.out.println("Test successfully completed.");
    }


    @Test
    public void testCellNotAdjacentBuild() throws Exception {

        Match testMatch = new Match(2);
        Board testBoard = new Board();
        Player testPlayer = new Player("Roberto", "RobS", testMatch);
        Worker testWorker = new Worker(testPlayer, testBoard);

        System.out.println("testing build() with an invalid movement: an adjacent cell must be requested to build in...");

        testWorker.setInitialPosition(0,0);

        assertThrows(Exception.class, () -> {testWorker.build(testBoard.getCell(0,2));});
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testCellNotEmptyBuild() throws Exception {

        Match testMatch = new Match(2);
        Board testBoard = new Board();
        Player testPlayer = new Player("Roberto", "RobS", testMatch);
        Worker testWorker = new Worker(testPlayer, testBoard);
        Worker testSecondWorker = new Worker(testPlayer, testBoard);

        System.out.println("testing build() with an invalid movement: an empty cell must be requested to build in...");

        testWorker.setInitialPosition(0,0);
        testSecondWorker.setInitialPosition(0,1);

        assertThrows(Exception.class, () -> {testWorker.build(testBoard.getCell(0,1));});
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testDomeBuild() throws Exception {

        Match testMatch = new Match(2);
        Board testBoard = new Board();
        Player testPlayer = new Player("Roberto", "RobS", testMatch);
        Worker testWorker = new Worker(testPlayer, testBoard);

        System.out.println("testing build() with an invalid movement: workers can't build in a dome...");

        testWorker.setInitialPosition(0,0);
        testBoard.getCell(0,1).setLevel(BlockType.DOME);

        Exception exception = assertThrows(Exception.class, () -> {testWorker.build(testBoard.getCell(0,1));});
        System.out.println("Test successfully completed.");
    }

}
