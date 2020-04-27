package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ApolloTest {

    @Test
    public void apolloCheckMoveTest()  {

        Board.clearInstances(); // TODO Use @BeforeEach
        Match.clearInstances();

        Apollo apollo = new Apollo();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player1 = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker1 = new Worker(player1, match.getMatchBoard());
        Player player2 = new Player(UUID.randomUUID().toString(), "nickname2", match);
        Worker worker2 = new Worker(player2, match.getMatchBoard());

        worker1.setInitialPosition(0, 0);
        worker2.setInitialPosition(0, 1);

        assertTrue(apollo.checkMove(worker1, match.getMatchBoard().getCell(0, 1)));
        assertFalse(apollo.checkMove(worker1, match.getMatchBoard().getCell(0, 2)));
    }

    @Test
    public void apolloExecuteMoveTest()  {

        Board.clearInstances();
        Match.clearInstances();

        Apollo apollo = new Apollo();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player1 = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker1 = new Worker(player1, match.getMatchBoard());
        Player player2 = new Player(UUID.randomUUID().toString(), "nickname2", match);
        Worker worker2 = new Worker(player2, match.getMatchBoard());

        worker1.setInitialPosition(0, 0);
        worker2.setInitialPosition(0, 1);

        apollo.executeMove(worker1, match.getMatchBoard().getCell(0, 1));

        assertEquals(match.getMatchBoard().getCell(0, 1), worker1.getPosition());
        assertEquals(match.getMatchBoard().getCell(0, 0), worker2.getPosition());

    }

    @Test
    public void getGodInfoTest() {
        Apollo apollo = new Apollo();

        Map<String, String> info = apollo.getGodInfo();

        assertEquals(info.get("name"), Apollo.NAME);
        assertEquals(info.get("description"), Apollo.DESCRIPTION);
        assertEquals(info.get("power_description"), Apollo.POWER_DESCRIPTION);
    }

}
