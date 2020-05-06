package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ArtemisTest {

    @Test
    public void artemisCheckMultipleMoveTest() {

        Board.clearInstances();
        Match.clearInstances();

        Artemis artemis = new Artemis();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), match);
        Worker worker = player.getWorkerFirst();
        Worker otherWorker = player.getWorkerSecond();

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

        Board.clearInstances();
        Match.clearInstances();

        Artemis artemis = new Artemis();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(),  match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        assertTrue(artemis.checkMove(worker, match.getMatchBoard().getCell(0, 1)));
        artemis.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        assertFalse(artemis.checkMove(worker, match.getMatchBoard().getCell(0, 0)));

    }


    @Test
    public void artemisEndTurnTest() {
        Board.clearInstances();
        Match.clearInstances();

        Artemis artemis = new Artemis();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), match);
        Worker worker = player.getWorkerFirst();

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
        artemis.endTurn(player);

        //Now I check that I can move again (so the values are correctly reinitialized
        assertTrue(artemis.checkMove(worker, match.getMatchBoard().getCell(0, 3)));
    }
}
