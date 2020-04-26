package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PrometheusTest {

    @Test
    public void prometheusBuildThenMoveTest()  {

        Board.clearInstances();
        Match.clearInstances();

        Prometheus prometheus = new Prometheus();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = new Worker(player, match.getMatchBoard());

        worker.setInitialPosition(0, 0);

        assertTrue(prometheus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        prometheus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertTrue(prometheus.checkMove(worker, match.getMatchBoard().getCell(1, 1)));
        prometheus.executeMove(worker, match.getMatchBoard().getCell(1, 1));
    }

    @Test
    public void prometheusCannotMoveUpIfBuiltBeforeMovingTest()  {

        Board.clearInstances();
        Match.clearInstances();

        Prometheus prometheus = new Prometheus();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = new Worker(player, match.getMatchBoard());

        worker.setInitialPosition(0, 0);

        assertTrue(prometheus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        prometheus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertFalse(prometheus.checkMove(worker, match.getMatchBoard().getCell(0, 1)));

    }

    @Test
    public void prometheusCannotMultipleBuildIfMoveBeforeBuildTest()  {

        Board.clearInstances();
        Match.clearInstances();

        Prometheus prometheus = new Prometheus();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = new Worker(player, match.getMatchBoard());

        worker.setInitialPosition(0, 0);

        assertTrue(prometheus.checkMove(worker, match.getMatchBoard().getCell(0, 1)));
        prometheus.executeMove(worker, match.getMatchBoard().getCell(0, 1));

        assertTrue(prometheus.checkBuild(worker, match.getMatchBoard().getCell(1, 1), BlockType.LEVEL_ONE));
        prometheus.executeBuild(worker, match.getMatchBoard().getCell(1, 1), BlockType.LEVEL_ONE);

        assertFalse(prometheus.checkBuild(worker, match.getMatchBoard().getCell(1, 1), BlockType.LEVEL_TWO));
    }

    @Test
    public void getGodInfoTest() {
        Prometheus prometheus = new Prometheus();

        Map<String, String> info = prometheus.getGodInfo();

        assertEquals(info.get("name"), Prometheus.NAME);
        assertEquals(info.get("description"), Prometheus.DESCRIPTION);
        assertEquals(info.get("power_description"), Prometheus.POWER_DESCRIPTION);
    }

    @Test
    public void prometheusEndTurnTest() {
        Board.clearInstances();
        Match.clearInstances();

        Prometheus prometheus = new Prometheus();

        Match match = Match.getInstance(UUID.randomUUID().toString(), 2);
        Player player = new Player(UUID.randomUUID().toString(), "nickname", match);
        Worker worker = player.getWorkerFirst();

        worker.setInitialPosition(0, 0);

        assertTrue(prometheus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE));
        prometheus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_ONE);

        assertTrue(prometheus.checkMove(worker, match.getMatchBoard().getCell(1, 1)));
        prometheus.executeMove(worker, match.getMatchBoard().getCell(1, 1));

        // cannot move again because the limit number of movements was reached
        assertFalse(prometheus.checkMove(worker, match.getMatchBoard().getCell(0, 0)));

        assertTrue(prometheus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_TWO));
        prometheus.executeBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_TWO);

        assertFalse(prometheus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_THREE));

        assertTrue(prometheus.checkEndTurn());
        prometheus.endTurn(player);

        //Now I check that I can move/build again (so the values are correctly reinitialized
        assertTrue(prometheus.checkBuild(worker, match.getMatchBoard().getCell(0, 1), BlockType.LEVEL_THREE));
    }
}