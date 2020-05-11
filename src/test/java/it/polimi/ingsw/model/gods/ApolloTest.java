package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ApolloTest {

    @Test
    public void apolloCheckMoveTest()  {

         // TODO Use @BeforeEach
        Match.clearInstances();

        Apollo apollo = new Apollo();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), match);
        Worker worker = player.getWorkerFirst();
        Player oppositePlayer = new Player(UUID.randomUUID().toString(), match);
        Worker oppositeWorker = oppositePlayer.getWorkerFirst();

        worker.setInitialPosition(0, 0);
        oppositeWorker.setInitialPosition(0, 1);

        assertTrue(apollo.checkMove(worker, match.getMatchBoard().getCell(0, 1)));
        assertFalse(apollo.checkMove(worker, match.getMatchBoard().getCell(0, 2)));
    }

    @Test
    public void apolloExecuteMoveTest()  {


        Match.clearInstances();

        Apollo apollo = new Apollo();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), match);
        Worker worker = player.getWorkerFirst();
        Player oppositePlayer = new Player(UUID.randomUUID().toString(), match);
        Worker oppositeWorker = oppositePlayer.getWorkerFirst();

        worker.setInitialPosition(0, 0);
        oppositeWorker.setInitialPosition(0, 1);

        apollo.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        assertEquals(match.getMatchBoard().getCell(0, 1), worker.getPosition());
        assertEquals(match.getMatchBoard().getCell(0, 0), oppositeWorker.getPosition());

        assertEquals(worker, match.getMatchBoard().getCell(0, 1).getWorker());
        assertEquals(oppositeWorker, match.getMatchBoard().getCell(0, 0).getWorker());
    }

}
