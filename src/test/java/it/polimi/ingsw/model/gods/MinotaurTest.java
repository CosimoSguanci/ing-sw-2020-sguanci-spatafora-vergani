package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MinotaurTest {
    @Test
    public void minotaurCheckMoveTest() {
        Minotaur minotaur = new Minotaur();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker worker = player.getWorkerFirst();
        Player oppositePlayer = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker oppositeWorker = oppositePlayer.getWorkerFirst();

        worker.setInitialPosition(0, 0);
        oppositeWorker.setInitialPosition(0, 1);

        assertTrue(minotaur.checkMove(worker, match.getMatchBoard().getCell(0, 1)));

        match.getMatchBoard().getCell(0, 2).setLevel(BlockType.DOME); // Occupy Worker 2's backward position

        assertFalse(minotaur.checkMove(worker, match.getMatchBoard().getCell(0, 1)));

        match.getMatchBoard().getCell(0, 2).setLevel(BlockType.GROUND);

        worker.move(match.getMatchBoard().getCell(2, 2));
        oppositeWorker.move(match.getMatchBoard().getCell(1, 1));

        worker.reinitializeBuiltMoved();
        oppositeWorker.reinitializeBuiltMoved();

        assertTrue(minotaur.checkMove(worker, match.getMatchBoard().getCell(1, 1)));

        worker.move(match.getMatchBoard().getCell(2, 2));
        oppositeWorker.move(match.getMatchBoard().getCell(1, 2));

        worker.reinitializeBuiltMoved();
        oppositeWorker.reinitializeBuiltMoved();

        assertTrue(minotaur.checkMove(worker, match.getMatchBoard().getCell(1, 2)));

        worker.move(match.getMatchBoard().getCell(2, 2));
        oppositeWorker.move(match.getMatchBoard().getCell(2, 1));

        worker.reinitializeBuiltMoved();
        oppositeWorker.reinitializeBuiltMoved();

        assertTrue(minotaur.checkMove(worker, match.getMatchBoard().getCell(2, 1)));

        worker.move(match.getMatchBoard().getCell(1, 2));
        oppositeWorker.move(match.getMatchBoard().getCell(2, 2));

        worker.reinitializeBuiltMoved();
        oppositeWorker.reinitializeBuiltMoved();

        assertTrue(minotaur.checkMove(worker, match.getMatchBoard().getCell(2, 2)));
    }

    @Test
    public void minotaurExecuteMoveTest() {
        Minotaur minotaur = new Minotaur();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker worker = player.getWorkerFirst();
        Player oppositePlayer = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker oppositeWorker = oppositePlayer.getWorkerFirst();

        worker.setInitialPosition(0, 0);
        oppositeWorker.setInitialPosition(0, 1);

        // Not Diagonal Test
        minotaur.checkMove(worker, match.getMatchBoard().getCell(0, 1)); // needed to compute backward cell
        minotaur.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        assertEquals(match.getMatchBoard().getCell(0, 1), worker.getPosition());
        assertEquals(match.getMatchBoard().getCell(0, 2), oppositeWorker.getPosition()); // Forced one space straight backwards

        // Diagonal Test
        oppositeWorker.move(match.getMatchBoard().getCell(1, 2));
        minotaur.checkMove(worker, match.getMatchBoard().getCell(1, 2)); // needed to compute backward cell
        minotaur.executeMove(worker, match.getMatchBoard().getCell(1, 2));
        assertEquals(match.getMatchBoard().getCell(1, 2), worker.getPosition());
        assertEquals(match.getMatchBoard().getCell(2, 3), oppositeWorker.getPosition()); // Forced one space straight backwards
    }

    @Test
    public void minotaurCannotMoveTest() {
        Minotaur minotaur = new Minotaur();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker workerFirst = player.getWorkerFirst();
        Worker workerSecond = player.getWorkerSecond();

        workerFirst.setInitialPosition(0, 0);
        workerSecond.setInitialPosition(0, 4);

        match.getMatchBoard().getCell(0, 1).setLevel(BlockType.DOME);
        match.getMatchBoard().getCell(1, 1).setLevel(BlockType.DOME);
        match.getMatchBoard().getCell(1, 0).setLevel(BlockType.DOME);

        match.getMatchBoard().getCell(0, 3).setLevel(BlockType.DOME);
        match.getMatchBoard().getCell(1, 3).setLevel(BlockType.DOME);
        match.getMatchBoard().getCell(1, 4).setLevel(BlockType.DOME);

        assertFalse(minotaur.canMove(match.getMatchBoard(), player));
    }

    @Test
    public void minotaurCanMoveBecauseEmptyTest() {
        Minotaur minotaur = new Minotaur();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker workerFirst = player.getWorkerFirst();
        Worker workerSecond = player.getWorkerSecond();

        workerFirst.setInitialPosition(0, 0);
        workerSecond.setInitialPosition(0, 4);

        match.getMatchBoard().getCell(1, 1).setLevel(BlockType.DOME);
        match.getMatchBoard().getCell(1, 0).setLevel(BlockType.DOME);

        match.getMatchBoard().getCell(0, 3).setLevel(BlockType.DOME);
        match.getMatchBoard().getCell(1, 3).setLevel(BlockType.DOME);
        match.getMatchBoard().getCell(1, 4).setLevel(BlockType.DOME);

        assertTrue(minotaur.canMove(match.getMatchBoard(), player));
    }

    @Test
    public void minotaurCanMoveBecauseMoveOpponentTest() {
        Minotaur minotaur = new Minotaur();

        Match match = new Match(2);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker workerFirst = player.getWorkerFirst();
        Worker workerSecond = player.getWorkerSecond();

        Player player2 = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker workerFirst_2 = player2.getWorkerFirst();
        Worker workerSecond_2 = player2.getWorkerSecond();

        workerFirst.setInitialPosition(0, 0);
        workerSecond.setInitialPosition(4, 4);

        workerFirst_2.setInitialPosition(0, 1);
        workerSecond_2.setInitialPosition(3, 4);

        match.getMatchBoard().getCell(1, 1).setLevel(BlockType.DOME);
        match.getMatchBoard().getCell(1, 0).setLevel(BlockType.DOME);

        assertTrue(minotaur.canMove(match.getMatchBoard(), player));
    }

}