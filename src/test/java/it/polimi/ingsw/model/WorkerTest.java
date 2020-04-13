package it.polimi.ingsw.model;

import static org.junit.Assert.*;

import it.polimi.ingsw.model.*;
import org.junit.Test;


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
        assertTrue(testWorker.standardCheckMove(testBoard.getCell(0,1)));
        testWorker.move(testBoard.getCell(0,1));

        assertEquals(testBoard.getCell(0,1), testWorker.getPosition());
        assertEquals(testBoard.getCell(0, 1).getWorker(), testWorker);
        assertNull(testBoard.getCell(0, 0).getWorker());
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

        assertFalse(testWorker.standardCheckMove(testBoard.getCell(0,2)));
        assertEquals(testBoard.getCell(0,0).getWorker(), testWorker);
        assertNotSame(testBoard.getCell(0, 2).getWorker(), testWorker);
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

        assertFalse(testWorker.standardCheckMove(testBoard.getCell(0,1)));
        assertEquals(testBoard.getCell(0,0).getWorker(), testWorker);
        assertEquals(testBoard.getCell(0,1).getWorker(), testOccupantWorker);
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

        assertFalse(testWorker.standardCheckMove(testBoard.getCell(0,1)));
        assertEquals(testBoard.getCell(0,0).getWorker(), testWorker);
        assertSame(testBoard.getCell(0, 1).getLevel(), BlockType.DOME);
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

        assertFalse(testWorker.standardCheckMove(testBoard.getCell(0,1)));
        assertEquals(testBoard.getCell(0,0).getWorker(), testWorker);
        assertFalse(testPlayer.match.getCanMove());
        assertSame(testBoard.getCell(0, 1).getLevel(), BlockType.LEVEL_ONE);
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testCannotMove2LevelUp() throws Exception {

        Match testMatch = new Match(2);
        Board testBoard = new Board();
        Player testPlayer = new Player("Roberto", "RobS", testMatch);
        Worker testWorker = new Worker(testPlayer, testBoard);

        System.out.println("testing move() with an invalid movement: workers can't move more that one level up...");

        testWorker.setInitialPosition(0,0);
        testBoard.getCell(0,1).setLevel(BlockType.LEVEL_TWO);

        assertFalse(testWorker.standardCheckMove(testBoard.getCell(0,1)));
        assertEquals(testBoard.getCell(0,0).getWorker(), testWorker);
        assertSame(testBoard.getCell(0, 1).getLevel(), BlockType.LEVEL_TWO);
        System.out.println("Test successfully completed.");
    }

/*
    @Test
    public void testNotValidMove() throws Exception {

        Match testMatch = new Match(2);
        Board testBoard = new Board();
        Player testPlayer = new Player("Roberto", "RobS", testMatch);
        Worker testWorker = new Worker(testPlayer, testBoard);
        Worker testOccupantWorker = new Worker(testPlayer, testBoard);

        System.out.println("testing move() with invalid commands...");
        testWorker.setInitialPosition(0,0);

        //NotAdjacent
        assertFalse(testWorker.standardCheckMove(testBoard.getCell(0,0))); //case 'first' and 'second' are the same cell, so they are not adjacent
        assertFalse(testWorker.standardCheckMove(testBoard.getCell(0,2)));
        assertEquals(testBoard.getCell(0,0).getWorker(), testWorker);
        assertNotSame(testBoard.getCell(0, 2).getWorker(), testWorker);

        //NotEmpty
        testOccupantWorker.setInitialPosition(0,1);
        assertFalse(testWorker.standardCheckMove(testBoard.getCell(0,1)));
        assertEquals(testBoard.getCell(0,0).getWorker(), testWorker);
        assertEquals(testBoard.getCell(0,1).getWorker(), testOccupantWorker);

        //CannotMoveInDome
        testBoard.getCell(1,0).setLevel(BlockType.DOME);
        assertFalse(testWorker.standardCheckMove(testBoard.getCell(1,0)));
        assertEquals(testBoard.getCell(0,0).getWorker(), testWorker);
        assertSame(testBoard.getCell(0, 1).getLevel(), BlockType.DOME);

        //CannotMoveFlag
        testPlayer.match.setCanMove(false);
        testBoard.getCell(1,1).setLevel(BlockType.LEVEL_ONE);
        assertFalse(testWorker.standardCheckMove(testBoard.getCell(1,1)));
        assertEquals(testBoard.getCell(0,0).getWorker(), testWorker);
        assertFalse(testPlayer.match.getCanMove());
        assertSame(testBoard.getCell(0, 1).getLevel(), BlockType.LEVEL_ONE);

        //CannotMove2orMoreLevelUp
        testBoard.getCell(1,0).setLevel(BlockType.LEVEL_TWO);
        assertFalse(testWorker.standardCheckMove(testBoard.getCell(1,0)));
        assertEquals(testBoard.getCell(0,0).getWorker(), testWorker);
        assertSame(testBoard.getCell(0, 1).getLevel(), BlockType.LEVEL_TWO);

        System.out.println("Test successfully completed.");
    }
*/

    @Test
    public void testBuild() throws Exception {

        Match testMatch = new Match(2);
        Board testBoard = new Board();
        Player testPlayer = new Player("Roberto", "RobS", testMatch);
        Worker testWorker = new Worker(testPlayer, testBoard);

        System.out.println("testing build() with a valid request...");

        testWorker.setInitialPosition(0,0);
        assertTrue(testWorker.standardCheckBuild(testBoard.getCell(0,1)));
        Cell previousPosition = testWorker.getPosition();
        testWorker.build(testBoard.getCell(0,1));
        assertEquals(BlockType.LEVEL_ONE , testBoard.getCell(0,1).getLevel());
        assertNull(testBoard.getCell(0,1).getWorker());
        assertSame(testWorker.getPosition(), previousPosition);

        assertTrue(testWorker.standardCheckBuild(testBoard.getCell(0,1)));
        previousPosition = testWorker.getPosition();
        testWorker.build(testBoard.getCell(0,1));
        assertEquals(BlockType.LEVEL_TWO, testBoard.getCell(0,1).getLevel());
        assertNull(testBoard.getCell(0,1).getWorker());
        assertSame(testWorker.getPosition(), previousPosition);

        assertTrue(testWorker.standardCheckBuild(testBoard.getCell(0,1)));
        previousPosition = testWorker.getPosition();
        testWorker.build(testBoard.getCell(0,1));
        assertEquals(BlockType.LEVEL_THREE, testBoard.getCell(0,1).getLevel());
        assertNull(testBoard.getCell(0,1).getWorker());
        assertSame(testWorker.getPosition(), previousPosition);

        assertTrue(testWorker.standardCheckBuild(testBoard.getCell(0,1)));
        previousPosition = testWorker.getPosition();
        testWorker.build(testBoard.getCell(0,1));
        assertEquals(BlockType.DOME, testBoard.getCell(0,1).getLevel());
        assertNull(testBoard.getCell(0,1).getWorker());
        assertSame(testWorker.getPosition(), previousPosition);

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
        BlockType previousLevel = testBoard.getCell(0,1).getLevel();

        assertFalse(testWorker.standardCheckBuild(testBoard.getCell(0,2)));
        assertSame(testBoard.getCell(0,1).getLevel(), previousLevel);
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
        BlockType previousLevel = testBoard.getCell(0,1).getLevel();
        testSecondWorker.setInitialPosition(0,1);

        assertFalse(testWorker.standardCheckBuild(testBoard.getCell(0,1)));
        assertSame(testBoard.getCell(0,1).getLevel(), previousLevel);
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

        assertFalse(testWorker.standardCheckBuild(testBoard.getCell(0,1)));
        assertSame(testBoard.getCell(0,1).getLevel(), BlockType.DOME);
        System.out.println("Test successfully completed.");
    }

/* TESTS INVALID COMMAND TO BUILD
    @Test
    public void testNotValidBuild() throws Exception {

        Match testMatch = new Match(2);
        Board testBoard = new Board();
        Player testPlayer = new Player("Roberto", "RobS", testMatch);
        Worker testWorker = new Worker(testPlayer, testBoard);
        Worker testSecondWorker = new Worker(testPlayer, testBoard);

        System.out.println("testing build() with invalid commands...");

        testWorker.setInitialPosition(0,0);

        //NotAdjacent
        assertFalse(testWorker.standardCheckBuild(testBoard.getCell(0,2)));


        testSecondWorker.setInitialPosition(0,1);

        //NotEmptyCell
        assertFalse(testWorker.standardCheckBuild(testBoard.getCell(0,1)));

        //DomeAlreadyBuilt
        testBoard.getCell(0,1).setLevel(BlockType.DOME);
        assertFalse(testWorker.standardCheckBuild(testBoard.getCell(0,1)));

        System.out.println("Test successfully completed.");
    }
*/
}
