package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.exceptions.CellNotEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class WorkerTest {

    private Board board;
    private Player player;
    private Worker worker;

    @BeforeEach
    public void initWorkerProperties() {
        int playersNum = 2;
        Match match = new Match(playersNum);
        this.board = match.getMatchBoard();
        this.player = new Player("Roberto", new Model(match), match);
        this.worker = new Worker(player, board, Command.WORKER_FIRST);
    }

    @Test
    public void testGetPosition() {

        System.out.println("testing getPosition() and setInitialPosition()...");

        Worker testFirstWorker = new Worker(player, board, Command.WORKER_FIRST);
        Worker testSecondWorker = new Worker(player, board, Command.WORKER_SECOND);

        testFirstWorker.setInitialPosition(0, 0);
        assertEquals(board.getCell(0, 0), testFirstWorker.getPosition());

        System.out.println("Test successfully completed.");

        assertThrows(CellNotEmptyException.class, () -> testSecondWorker.setInitialPosition(0, 0));
    }

    @Test
    public void testMove() {

        System.out.println("testing move() with a valid movement...");

        worker.setInitialPosition(0, 0);
        assertTrue(worker.standardCheckMove(board.getCell(0, 1)));
        worker.move(board.getCell(0, 1));

        assertEquals(board.getCell(0, 1), worker.getPosition());
        assertEquals(board.getCell(0, 1).getWorker(), worker);
        assertNull(board.getCell(0, 0).getWorker());
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testCellNotAdjacentMove() {

        System.out.println("testing move() with an invalid movement: an adjacent cell must be requested for movement...");

        worker.setInitialPosition(0, 0);

        assertFalse(worker.standardCheckMove(board.getCell(0, 2)));
        assertEquals(board.getCell(0, 0).getWorker(), worker);
        assertNotSame(board.getCell(0, 2).getWorker(), worker);
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testCellNotEmptyMove() {

        Worker testOccupantWorker = new Worker(player, board, Command.WORKER_FIRST);

        System.out.println("testing move() with an invalid movement: an empty cell must be requested for movement...");

        worker.setInitialPosition(0, 0);
        testOccupantWorker.setInitialPosition(0, 1);

        assertFalse(worker.standardCheckMove(board.getCell(0, 1)));
        assertEquals(board.getCell(0, 0).getWorker(), worker);
        assertEquals(board.getCell(0, 1).getWorker(), testOccupantWorker);
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testDomeMove() {

        System.out.println("testing move() with an invalid movement: workers can't move in a DOME...");

        worker.setInitialPosition(0, 0);
        board.getCell(0, 1).setLevel(BlockType.DOME);

        assertFalse(worker.standardCheckMove(board.getCell(0, 1)));
        assertEquals(board.getCell(0, 0).getWorker(), worker);
        assertSame(board.getCell(0, 1).getLevel(), BlockType.DOME);
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testCannotMove2LevelUp() {

        System.out.println("testing move() with an invalid movement: workers can't move more that one level up...");

        worker.setInitialPosition(0, 0);
        board.getCell(0, 1).setLevel(BlockType.LEVEL_TWO);

        assertFalse(worker.standardCheckMove(board.getCell(0, 1)));
        assertEquals(board.getCell(0, 0).getWorker(), worker);
        assertSame(board.getCell(0, 1).getLevel(), BlockType.LEVEL_TWO);
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testBuild() {

        System.out.println("testing build() with a valid request...");

        worker.setInitialPosition(0, 0);

        worker.move(board.getCell(0, 1));
        assertTrue(worker.standardCheckBuild(board.getCell(0, 2), null));
        Cell previousPosition = worker.getPosition();
        worker.build(board.getCell(0, 2), null);
        assertEquals(BlockType.LEVEL_ONE, board.getCell(0, 2).getLevel());
        assertNull(board.getCell(0, 2).getWorker());
        assertSame(worker.getPosition(), previousPosition);

        worker.reinitializeBuiltMoved();
        worker.setHasMoved();
        assertTrue(worker.standardCheckBuild(board.getCell(0, 2), null));
        previousPosition = worker.getPosition();
        worker.build(board.getCell(0, 2), null);
        assertEquals(BlockType.LEVEL_TWO, board.getCell(0, 2).getLevel());
        assertNull(board.getCell(0, 2).getWorker());
        assertSame(worker.getPosition(), previousPosition);

        worker.reinitializeBuiltMoved();
        worker.setHasMoved();
        assertTrue(worker.standardCheckBuild(board.getCell(0, 2), null));
        previousPosition = worker.getPosition();
        worker.build(board.getCell(0, 2), null);
        assertEquals(BlockType.LEVEL_THREE, board.getCell(0, 2).getLevel());
        assertNull(board.getCell(0, 2).getWorker());
        assertSame(worker.getPosition(), previousPosition);

        worker.reinitializeBuiltMoved();
        worker.setHasMoved();
        assertTrue(worker.standardCheckBuild(board.getCell(0, 2), null));
        previousPosition = worker.getPosition();
        worker.build(board.getCell(0, 2), null);
        assertEquals(BlockType.DOME, board.getCell(0, 2).getLevel());
        assertNull(board.getCell(0, 2).getWorker());
        assertSame(worker.getPosition(), previousPosition);

        System.out.println("Test successfully completed.");
    }


    @Test
    public void testCellNotAdjacentBuild() {

        System.out.println("testing build() with an invalid movement: an adjacent cell must be requested to build in...");

        worker.setInitialPosition(0, 0);
        BlockType previousLevel = board.getCell(0, 1).getLevel();

        assertFalse(worker.standardCheckBuild(board.getCell(0, 2), null));
        assertSame(board.getCell(0, 1).getLevel(), previousLevel);
        System.out.println("Test successfully completed.");
    }


    @Test
    public void testCellNotEmptyBuild() {

        Worker testWorker = new Worker(player, board, Command.WORKER_FIRST);
        Worker testSecondWorker = new Worker(player, board, Command.WORKER_SECOND);

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

        System.out.println("testing build() with an invalid movement: workers can't build in a dome...");

        worker.setInitialPosition(0, 0);
        board.getCell(0, 1).setLevel(BlockType.DOME);

        assertFalse(worker.standardCheckBuild(board.getCell(0, 1), null));
        assertSame(board.getCell(0, 1).getLevel(), BlockType.DOME);
        System.out.println("Test successfully completed.");
    }
}
