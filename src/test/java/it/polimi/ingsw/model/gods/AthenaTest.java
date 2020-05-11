package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AthenaTest {
    @Test
    public void athenaCheckMoveConstraintsTest()  {


        Match.clearInstances();

        Athena athena = new Athena();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(),  match);
        Worker worker = player.getWorkerFirst();
        Player oppositePlayer = new Player(UUID.randomUUID().toString(),  match);
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

        Match.clearInstances();

        Athena athena = new Athena();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(),  match);
        Worker worker = player.getWorkerFirst();

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
}
