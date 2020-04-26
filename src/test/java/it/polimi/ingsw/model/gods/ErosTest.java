package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

// TODO Game Preparation Test
public class ErosTest {
    @Test
    public void erosWinConditionThreePlayersTest()  {

        Board.clearInstances();
        Match.clearInstances();

        Eros eros = new Eros();

        Match match = Match.getInstance(String.valueOf(Thread.currentThread().getId()), 3);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker workerFirst = new Worker(player, match.getMatchBoard());
        Worker workerSecond = new Worker(player, match.getMatchBoard());
        player.setWorkerFirst(workerFirst);
        player.setWorkerSecond(workerSecond);

        workerFirst.setInitialPosition(0, 0);
        workerSecond.setInitialPosition(0, 2);

        assertFalse(eros.checkWinCondition(workerFirst));

        eros.executeMove(workerFirst, match.getMatchBoard().getCell(0, 1));

        assertTrue(eros.checkWinCondition(workerFirst));

    }

    @Test
    public void erosWinConditionTwoPlayersTest()  {

        Board.clearInstances();
        Match.clearInstances();

        Eros eros = new Eros();

        Match match = Match.getInstance(String.valueOf(Thread.currentThread().getId()), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker workerFirst = new Worker(player, match.getMatchBoard());
        Worker workerSecond = new Worker(player, match.getMatchBoard());
        player.setWorkerFirst(workerFirst);
        player.setWorkerSecond(workerSecond);

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
        Board.clearInstances();
        Match.clearInstances();

        Eros eros = new Eros();

        Match match = Match.getInstance(String.valueOf(Thread.currentThread().getId()), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker workerFirst = new Worker(player, match.getMatchBoard());
        Worker workerSecond = new Worker(player, match.getMatchBoard());
        player.setWorkerFirst(workerFirst);
        player.setWorkerSecond(workerSecond); // TODO Change tests, because now Workers are instantiated inside Player's constructor

        // not opposite borders -> not ok with Eros constraints
        assertFalse(eros.checkGamePreparation(workerFirst, match.getMatchBoard().getCell(0, 1), workerSecond, match.getMatchBoard().getCell(0,2)));

        assertTrue(eros.checkGamePreparation(workerFirst, match.getMatchBoard().getCell(0, 1), workerSecond, match.getMatchBoard().getCell(4,1)));
    }

    @Test
    public void getGodInfoTest() {
        Eros eros = new Eros();

        Map<String, String> info = eros.getGodInfo();

        assertEquals(info.get("name"), Eros.NAME);
        assertEquals(info.get("description"), Eros.DESCRIPTION);
        assertEquals(info.get("power_description"), Eros.POWER_DESCRIPTION);
    }
}