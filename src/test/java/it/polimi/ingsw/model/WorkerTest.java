package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.exceptions.CellNotEmptyException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class WorkerTest {

    @Test
    public void testGetPosition() {

        System.out.println("testing getPosition() and setInitialPosition()...");

        int playersNum = 2;
        Match match = new Match(playersNum);
        Board board = match.getMatchBoard();
        Player testPlayer = new Player("Roberto", new Model(match), match);
        Worker testFirstWorker = new Worker(testPlayer, board, Command.WORKER_FIRST);
        Worker testSecondWorker = new Worker(testPlayer, board, Command.WORKER_SECOND);

        testFirstWorker.setInitialPosition(0, 0);
        assertEquals(board.getCell(0, 0), testFirstWorker.getPosition());

        System.out.println("Test successfully completed.");

        assertThrows(CellNotEmptyException.class, () -> testSecondWorker.setInitialPosition(0, 0));
    }

    @Test
    public void testMove() {

        int playersNum = 2;
        Match match = new Match(playersNum);
        Board board = match.getMatchBoard();
        Player testPlayer = new Player("Roberto", new Model(match), match);
        Worker testWorker = new Worker(testPlayer, board, Command.WORKER_FIRST);

        System.out.println("testing move() with a valid movement...");

        testWorker.setInitialPosition(0, 0);
        assertTrue(testWorker.standardCheckMove(board.getCell(0, 1)));
        testWorker.move(board.getCell(0, 1));

        assertEquals(board.getCell(0, 1), testWorker.getPosition());
        assertEquals(board.getCell(0, 1).getWorker(), testWorker);
        assertNull(board.getCell(0, 0).getWorker());
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testCellNotAdjacentMove() {

        int playersNum = 2;
        Match match = new Match(playersNum);
        Board board = match.getMatchBoard();
        Player testPlayer = new Player("Roberto", new Model(match), match);
        Worker testWorker = new Worker(testPlayer, board, Command.WORKER_FIRST);

        System.out.println("testing move() with an invalid movement: an adjacent cell must be requested for movement...");

        testWorker.setInitialPosition(0, 0);

        assertFalse(testWorker.standardCheckMove(board.getCell(0, 2)));
        assertEquals(board.getCell(0, 0).getWorker(), testWorker);
        assertNotSame(board.getCell(0, 2).getWorker(), testWorker);
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testCellNotEmptyMove() {

        int playersNum = 2;
        Match match = new Match(playersNum);
        Board board = match.getMatchBoard();
        Player testPlayer = new Player("Roberto", new Model(match), match);
        Worker testWorker = new Worker(testPlayer, board, Command.WORKER_FIRST);
        Worker testOccupantWorker = new Worker(testPlayer, board, Command.WORKER_FIRST);

        System.out.println("testing move() with an invalid movement: an empty cell must be requested for movement...");

        testWorker.setInitialPosition(0, 0);
        testOccupantWorker.setInitialPosition(0, 1);

        assertFalse(testWorker.standardCheckMove(board.getCell(0, 1)));
        assertEquals(board.getCell(0, 0).getWorker(), testWorker);
        assertEquals(board.getCell(0, 1).getWorker(), testOccupantWorker);
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testDomeMove() {

        int playersNum = 2;
        Match match = new Match(playersNum);
        Board board = match.getMatchBoard();
        Player testPlayer = new Player("Roberto", new Model(match), match);
        Worker testWorker = new Worker(testPlayer, board, Command.WORKER_FIRST);

        System.out.println("testing move() with an invalid movement: workers can't move in a DOME...");

        testWorker.setInitialPosition(0, 0);
        board.getCell(0, 1).setLevel(BlockType.DOME);

        assertFalse(testWorker.standardCheckMove(board.getCell(0, 1)));
        assertEquals(board.getCell(0, 0).getWorker(), testWorker);
        assertSame(board.getCell(0, 1).getLevel(), BlockType.DOME);
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testCannotMove2LevelUp() {

        int playersNum = 2;
        Match match = new Match(playersNum);
        Board board = match.getMatchBoard();
        Player testPlayer = new Player("Roberto", new Model(match), match);
        Worker testWorker = new Worker(testPlayer, board, Command.WORKER_FIRST);

        System.out.println("testing move() with an invalid movement: workers can't move more that one level up...");

        testWorker.setInitialPosition(0, 0);
        board.getCell(0, 1).setLevel(BlockType.LEVEL_TWO);

        assertFalse(testWorker.standardCheckMove(board.getCell(0, 1)));
        assertEquals(board.getCell(0, 0).getWorker(), testWorker);
        assertSame(board.getCell(0, 1).getLevel(), BlockType.LEVEL_TWO);
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testBuild() {

        int playersNum = 2;
        Match match = new Match(playersNum);
        Board board = match.getMatchBoard();
        Player testPlayer = new Player("Roberto", new Model(match), match);
        Worker testWorker = new Worker(testPlayer, board, Command.WORKER_FIRST);

        System.out.println("testing build() with a valid request...");

        testWorker.setInitialPosition(0, 0);

        testWorker.move(board.getCell(0, 1));
        assertTrue(testWorker.standardCheckBuild(board.getCell(0, 2), null));
        Cell previousPosition = testWorker.getPosition();
        testWorker.build(board.getCell(0, 2), null);
        assertEquals(BlockType.LEVEL_ONE, board.getCell(0, 2).getLevel());
        assertNull(board.getCell(0, 2).getWorker());
        assertSame(testWorker.getPosition(), previousPosition);

        testWorker.reinitializeBuiltMoved();
        testWorker.setHasMoved();
        assertTrue(testWorker.standardCheckBuild(board.getCell(0, 2), null));
        previousPosition = testWorker.getPosition();
        testWorker.build(board.getCell(0, 2), null);
        assertEquals(BlockType.LEVEL_TWO, board.getCell(0, 2).getLevel());
        assertNull(board.getCell(0, 2).getWorker());
        assertSame(testWorker.getPosition(), previousPosition);

        testWorker.reinitializeBuiltMoved();
        testWorker.setHasMoved();
        assertTrue(testWorker.standardCheckBuild(board.getCell(0, 2), null));
        previousPosition = testWorker.getPosition();
        testWorker.build(board.getCell(0, 2), null);
        assertEquals(BlockType.LEVEL_THREE, board.getCell(0, 2).getLevel());
        assertNull(board.getCell(0, 2).getWorker());
        assertSame(testWorker.getPosition(), previousPosition);

        testWorker.reinitializeBuiltMoved();
        testWorker.setHasMoved();
        assertTrue(testWorker.standardCheckBuild(board.getCell(0, 2), null));
        previousPosition = testWorker.getPosition();
        testWorker.build(board.getCell(0, 2), null);
        assertEquals(BlockType.DOME, board.getCell(0, 2).getLevel());
        assertNull(board.getCell(0, 2).getWorker());
        assertSame(testWorker.getPosition(), previousPosition);

        /*
        testWorker.setHasMoved(); //without if and move
        assertTrue(testWorker.standardCheckBuild(board.getCell(0,1)));
        Cell previousPosition = testWorker.getPosition();
        testWorker.build(board.getCell(0,1));
        assertEquals(BlockType.LEVEL_ONE , board.getCell(0,1).getLevel());
        assertNull(board.getCell(0,1).getWorker());
        assertSame(testWorker.getPosition(), previousPosition);
        */

        System.out.println("Test successfully completed.");
    }


    @Test
    public void testCellNotAdjacentBuild() {

        int playersNum = 2;
        Match match = new Match(playersNum);
        Board board = match.getMatchBoard();
        Player testPlayer = new Player("Roberto", new Model(match), match);
        Worker testWorker = new Worker(testPlayer, board, Command.WORKER_FIRST);

        System.out.println("testing build() with an invalid movement: an adjacent cell must be requested to build in...");

        testWorker.setInitialPosition(0, 0);
        BlockType previousLevel = board.getCell(0, 1).getLevel();

        assertFalse(testWorker.standardCheckBuild(board.getCell(0, 2), null));
        assertSame(board.getCell(0, 1).getLevel(), previousLevel);
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testCellNotEmptyBuild() {

        int playersNum = 2;
        Match match = new Match(playersNum);
        Board board = match.getMatchBoard();
        Player testPlayer = new Player("Roberto", new Model(match), match);
        Worker testWorker = new Worker(testPlayer, board, Command.WORKER_FIRST);
        Worker testSecondWorker = new Worker(testPlayer, board, Command.WORKER_SECOND);

        System.out.println("testing build() with an invalid movement: an empty cell must be requested to build in...");

        testWorker.setInitialPosition(0, 0);
        BlockType previousLevel = board.getCell(0, 1).getLevel();
        testSecondWorker.setInitialPosition(0, 1);

        assertFalse(testWorker.standardCheckBuild(board.getCell(0, 1), null));
        assertSame(board.getCell(0, 1).getLevel(), previousLevel);
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testDomeBuild() {

        int playersNum = 2;
        Match match = new Match(playersNum);
        Board board = match.getMatchBoard();
        Player testPlayer = new Player("Roberto", new Model(match), match);
        Worker testWorker = new Worker(testPlayer, board, Command.WORKER_FIRST);

        System.out.println("testing build() with an invalid movement: workers can't build in a dome...");

        testWorker.setInitialPosition(0, 0);
        board.getCell(0, 1).setLevel(BlockType.DOME);

        assertFalse(testWorker.standardCheckBuild(board.getCell(0, 1), null));
        assertSame(board.getCell(0, 1).getLevel(), BlockType.DOME);
        System.out.println("Test successfully completed.");
    }

/* TESTS INVALID COMMAND TO BUILD
    @Test
    public void testNotValidBuild()  {

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
