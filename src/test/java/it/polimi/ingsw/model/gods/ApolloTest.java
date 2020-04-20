package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ApolloTest {

    @Test
    public void apolloCheckMoveTest() throws Exception {
        Apollo apollo = new Apollo();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player1 = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker1 = new Worker(player1, match.getMatchBoard());
        Player player2 = new Player(UUID.randomUUID().toString(), "nickname2", match);
        Worker worker2 = new Worker(player2, match.getMatchBoard());

        /*player.setWorkerFirst(worker);
        player.setGod(new God("name", "God Description", "Rule Description", apollo));*/

        worker1.setInitialPosition(0, 0);
        worker2.setInitialPosition(0, 1);

        assertTrue(apollo.checkMove(worker1, match.getMatchBoard().getCell(0, 1)));
        assertFalse(apollo.checkMove(worker1, match.getMatchBoard().getCell(0, 2)));

        Board.clearInstances();
        Match.clearInstances();

    }

    @Test
    public void apolloExecuteMoveTest() throws Exception {
        Apollo apollo = new Apollo();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player1 = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker1 = new Worker(player1, match.getMatchBoard());
        Player player2 = new Player(UUID.randomUUID().toString(), "nickname2", match);
        Worker worker2 = new Worker(player2, match.getMatchBoard());

        /*player.setWorkerFirst(worker);
        player.setGod(new God("name", "God Description", "Rule Description", apollo));*/

        worker1.setInitialPosition(0, 0);
        worker2.setInitialPosition(0, 1);

        apollo.executeMove(worker1, match.getMatchBoard().getCell(0, 1));

        assertEquals(match.getMatchBoard().getCell(0, 1), worker1.getPosition());
        assertEquals(match.getMatchBoard().getCell(0, 0), worker2.getPosition());

        Board.clearInstances();
        Match.clearInstances();

    }

}
