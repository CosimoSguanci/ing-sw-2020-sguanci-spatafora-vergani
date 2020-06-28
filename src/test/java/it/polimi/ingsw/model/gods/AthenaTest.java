package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AthenaTest {

    private Athena athena;
    private Match match;
    private Player player;
    private Worker worker;

    @BeforeEach
    public void initGameProperties() {

        this.athena = new Athena();
        this.match = new Match(2);
        this.player = new Player(UUID.randomUUID().toString(), new Model(match), match);
        this.worker = player.getWorkerFirst();
    }

    @Test
    public void athenaCheckMoveConstraintsTest() {

        Player oppositePlayer = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker oppositeWorker = oppositePlayer.getWorkerFirst();

        worker.setInitialPosition(0, 0);
        oppositeWorker.setInitialPosition(0, 2);

        match.getMatchBoard().getCell(0, 1).setLevel(BlockType.LEVEL_ONE); // Goes one level up to trigger Athena constraints
        match.getMatchBoard().getCell(0, 3).setLevel(BlockType.LEVEL_ONE);

        athena.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        assertFalse(athena.checkMoveConstraints(oppositeWorker, match.getMatchBoard().getCell(0, 3))); // Should constraints be checked inside move/executeMove?

        assertTrue(athena.checkMoveConstraints(oppositeWorker, match.getMatchBoard().getCell(1, 2))); // Should constraints be checked inside move/executeMove?

    }


    @Test
    public void athenaEndTurnTest() {

        worker.setInitialPosition(0, 0);

        assertTrue(athena.checkMove(worker, match.getMatchBoard().getCell(0, 1)));
        athena.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        // cannot move again because the worker already moved
        assertFalse(athena.checkMove(worker, match.getMatchBoard().getCell(0, 2)));

        assertTrue(athena.checkBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_ONE));
        athena.executeBuild(worker, match.getMatchBoard().getCell(0, 2), BlockType.LEVEL_ONE);

        // cannot move again because the worker already built a level in this turn
        assertFalse(athena.checkMove(worker, match.getMatchBoard().getCell(0, 2)));

        assertTrue(athena.checkEndTurn());
        athena.endPlayerTurn(player);

        //Now I check that I can move again (so the values are correctly reinitialized
        assertTrue(athena.checkMove(worker, match.getMatchBoard().getCell(0, 2)));
    }

    @Test
    public void onTurnStartedTest() {

        Player oppositePlayer = new Player(UUID.randomUUID().toString(), new Model(match), match);
        Worker oppositeWorker = oppositePlayer.getWorkerFirst();

        worker.setInitialPosition(0, 0);
        match.getMatchBoard().getCell(0, 1).setLevel(BlockType.LEVEL_ONE);
        assertTrue(athena.checkMove(worker, match.getMatchBoard().getCell(0, 1)));
        athena.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        oppositeWorker.setInitialPosition(3, 3);
        match.getMatchBoard().getCell(3, 4).setLevel(BlockType.LEVEL_ONE);

        assertFalse(athena.checkMoveConstraints(oppositeWorker, match.getMatchBoard().getCell(3, 4)));
        athena.onTurnStarted(player);

        assertTrue(athena.checkMoveConstraints(oppositeWorker, match.getMatchBoard().getCell(3, 4)));
    }
}
