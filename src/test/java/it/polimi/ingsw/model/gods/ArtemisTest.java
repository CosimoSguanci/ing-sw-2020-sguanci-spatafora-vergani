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
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = new Worker(player, match.getMatchBoard());

        worker.setInitialPosition(0, 0);

        assertTrue(artemis.checkMove(worker, match.getMatchBoard().getCell(0, 1)));
        artemis.executeMove(worker, match.getMatchBoard().getCell(0, 1));

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
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = new Worker(player, match.getMatchBoard());

        worker.setInitialPosition(0, 0);

        assertTrue(artemis.checkMove(worker, match.getMatchBoard().getCell(0, 1)));
        artemis.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        assertFalse(artemis.checkMove(worker, match.getMatchBoard().getCell(0, 0)));

    }

    @Test
    public void getGodInfoTest() {
        Artemis artemis = new Artemis();

        Map<String, String> info = artemis.getGodInfo();

        assertEquals(info.get("name"), Artemis.NAME);
        assertEquals(info.get("description"), Artemis.DESCRIPTION);
        assertEquals(info.get("power_description"), Artemis.POWER_DESCRIPTION);
    }


}
