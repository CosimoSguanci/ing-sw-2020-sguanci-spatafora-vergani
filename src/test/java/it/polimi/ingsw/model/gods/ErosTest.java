package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ErosTest {

    private Eros eros;
    private Match match;
    private Player player;

    @BeforeEach
    public void initGameProperties() {

        this.eros = new Eros();
        this.match = new Match(2);
        this.player = new Player(UUID.randomUUID().toString(), new Model(match), match);
    }

    @Test
    public void erosWinConditionThreePlayersTest() {

        Eros eros = new Eros();

        Match match = new Match(3);
        Player player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker workerFirst = player.getWorkerFirst();
        Worker workerSecond = player.getWorkerSecond();

        workerFirst.setInitialPosition(0, 0);
        workerSecond.setInitialPosition(0, 2);

        assertFalse(eros.checkWinCondition(workerFirst));

        eros.executeMove(workerFirst, match.getMatchBoard().getCell(0, 1));

        assertTrue(eros.checkWinCondition(workerFirst));
    }

    @Test
    public void erosWinConditionTwoPlayersTest() {

        Worker workerFirst = player.getWorkerFirst();
        Worker workerSecond = player.getWorkerSecond();

        workerFirst.setInitialPosition(0, 0);
        workerSecond.setInitialPosition(0, 2);

        eros.executeBuild(workerFirst, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);
        eros.executeMove(workerFirst, match.getMatchBoard().getCell(0, 1));

        assertFalse(eros.checkWinCondition(workerFirst));

        eros.executeBuild(workerSecond, match.getMatchBoard().getCell(1, 2), BlockType.LEVEL_ONE);
        eros.executeMove(workerSecond, match.getMatchBoard().getCell(1, 2));

        assertTrue(eros.checkWinCondition(workerFirst));

    }

    @Test
    public void erosCheckGamePreparationTest() {
        Worker workerFirst = player.getWorkerFirst();
        Worker workerSecond = player.getWorkerFirst();

        // not opposite borders -> not ok with Eros constraints
        assertFalse(eros.checkGamePreparation(workerFirst, match.getMatchBoard().getCell(0, 1), workerSecond, match.getMatchBoard().getCell(0, 2)));

        assertTrue(eros.checkGamePreparation(workerFirst, match.getMatchBoard().getCell(0, 1), workerSecond, match.getMatchBoard().getCell(4, 1)));
    }


    @Test
    public void erosEndTurnTest() {

        Worker worker = player.getWorkerFirst();
        Worker otherWorker = player.getWorkerSecond();

        worker.setInitialPosition(0, 0);
        otherWorker.setInitialPosition(3, 2);

        assertTrue(eros.checkMove(worker, match.getMatchBoard().getCell(0, 1)));
        eros.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        // cannot move again because the worker already moved
        assertFalse(eros.checkMove(worker, match.getMatchBoard().getCell(0, 2)));

        assertTrue(eros.checkBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_ONE));
        eros.executeBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_ONE);

        // cannot move again because the worker already built a level in this turn
        assertFalse(eros.checkMove(worker, match.getMatchBoard().getCell(0, 2)));

        assertTrue(eros.checkEndTurn());
        eros.endPlayerTurn(player);

        //Now I check that I can move again (so the values are correctly reinitialized
        assertTrue(eros.checkMove(worker, match.getMatchBoard().getCell(0, 2)));
    }
}