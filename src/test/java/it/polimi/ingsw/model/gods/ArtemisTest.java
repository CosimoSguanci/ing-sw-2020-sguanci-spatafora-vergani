package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArtemisTest {

    private Artemis artemis;
    private Match match;
    private Player player;
    private Worker worker;
    private Worker otherWorker;

    @BeforeEach
    public void initGameProperties() {

        this.artemis = new Artemis();
        this.match = new Match(2);
        this.player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        this.worker = player.getWorkerFirst();
        this.otherWorker = player.getWorkerSecond();
    }

    @Test
    public void artemisCheckMultipleMoveTest() {

        worker.setInitialPosition(0, 0);
        otherWorker.setInitialPosition(3, 2);

        assertTrue(artemis.checkMove(worker, match.getMatchBoard().getCell(0, 1)));
        artemis.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        assertFalse(artemis.checkMove(otherWorker, match.getMatchBoard().getCell(3, 3)));

        assertTrue(artemis.checkMove(worker, match.getMatchBoard().getCell(0, 2)));
        artemis.executeMove(worker, match.getMatchBoard().getCell(0, 2));

        assertFalse(artemis.checkMove(worker, match.getMatchBoard().getCell(0, 3)));
    }

    @Test
    public void artemisCheckMultipleMoveInitialPositionTest() {

        worker.setInitialPosition(0, 0);

        assertTrue(artemis.checkMove(worker, match.getMatchBoard().getCell(0, 1)));
        artemis.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        assertFalse(artemis.checkMove(worker, match.getMatchBoard().getCell(0, 0)));

    }


    @Test
    public void artemisEndTurnTest() {

        worker.setInitialPosition(0, 0);

        assertTrue(artemis.checkMove(worker, match.getMatchBoard().getCell(0, 1)));
        artemis.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        assertTrue(artemis.checkMove(worker, match.getMatchBoard().getCell(0, 2)));
        artemis.executeMove(worker, match.getMatchBoard().getCell(0, 2));

        // cannot move again because the limit number of movements was reached
        assertFalse(artemis.checkMove(worker, match.getMatchBoard().getCell(0, 3)));

        assertTrue(artemis.checkBuild(worker, match.getMatchBoard().getCell(0, 3), BlockType.LEVEL_ONE));
        artemis.executeBuild(worker, match.getMatchBoard().getCell(0, 3), BlockType.LEVEL_ONE);

        // cannot move again because the worker already built a level in this turn
        assertFalse(artemis.checkMove(worker, match.getMatchBoard().getCell(0, 3)));

        assertTrue(artemis.checkEndTurn());
        artemis.endPlayerTurn(player);

        //Now I check that I can move again (so the values are correctly reinitialized
        assertTrue(artemis.checkMove(worker, match.getMatchBoard().getCell(0, 3)));
    }

    @Test
    public void artemisCannotBuildTest() {

        worker.setInitialPosition(0, 1);
        otherWorker.setInitialPosition(3, 2);

        assertTrue(artemis.checkMove(worker, match.getMatchBoard().getCell(0, 0)));
        artemis.executeMove(worker, match.getMatchBoard().getCell(0, 0));

        match.getMatchBoard().getCell(0, 1).setLevel(BlockType.DOME);
        match.getMatchBoard().getCell(1, 1).setLevel(BlockType.DOME);
        match.getMatchBoard().getCell(1, 0).setLevel(BlockType.DOME);

        assertFalse(artemis.canBuild(match.getMatchBoard(), worker));


        match.getMatchBoard().getCell(1, 1).setLevel(BlockType.LEVEL_ONE);
        assertTrue(artemis.checkMove(worker, match.getMatchBoard().getCell(1, 1)));
        artemis.executeMove(worker, match.getMatchBoard().getCell(1, 1));

        match.getMatchBoard().getAvailableBuildCells(worker).stream().filter(cell -> cell.isAdjacentTo(worker.getPosition())).forEach(cell -> cell.setLevel(BlockType.DOME));

        assertFalse(artemis.canBuild(match.getMatchBoard(), worker));

        artemis.endPlayerTurn(player);

        worker.move(match.getMatchBoard().getCell(3, 3));

    }
}
