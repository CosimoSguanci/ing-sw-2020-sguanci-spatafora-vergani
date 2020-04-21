package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AthenaTest {
    @Test
    public void athenaCheckMoveConstraintsTest()  {

        Board.clearInstances();
        Match.clearInstances();

        Athena athena = new Athena();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = new Worker(player, match.getMatchBoard());
        Player oppositePlayer = new Player(UUID.randomUUID().toString(), "oppositeNickname", match);
        Worker oppositeWorker = new Worker(oppositePlayer, match.getMatchBoard());

        worker.setInitialPosition(0, 0);
        oppositeWorker.setInitialPosition(0, 2);

        match.getMatchBoard().getCell(0, 1).setLevel(BlockType.LEVEL_ONE); // Goes one level up to trigger Athena constraints
        match.getMatchBoard().getCell(0, 3).setLevel(BlockType.LEVEL_ONE);

        athena.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        assertFalse(athena.checkMoveConstraints(oppositeWorker, match.getMatchBoard().getCell(0, 3))); // Should constraints be checked inside move/executeMove?

        assertTrue(athena.checkMoveConstraints(oppositeWorker, match.getMatchBoard().getCell(1, 2))); // Should constraints be checked inside move/executeMove?

    }
}
